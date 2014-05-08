package knorxx.framework.generator.dependency;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.TreeSet;
import knorxx.framework.generator.JavaFile;
import static knorxx.framework.generator.util.JavaIdentifierUtils.getJavaClassNestingLevel;
import static knorxx.framework.generator.util.JavaIdentifierUtils.isInnerClass;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.commons.RemappingClassAdapter;

/**
 *
 * @author sj
 */
public class ByteCodeDependencyCollector extends DependencyCollector {

    public ByteCodeDependencyCollector() {
    }

    public ByteCodeDependencyCollector(DependencyCollector nextCollector) {
        super(nextCollector);
    }

    @Override
    protected Set<String> collectInternal(JavaFile<?> javaFile, ClassLoader classLoader) {
        return collectInternal(javaFile, javaFile, classLoader);
    }
    
    private Set<String> collectInternal(JavaFile<?> rootJavaFile, JavaFile<?> currentJavaFile, ClassLoader classLoader) {
        final Set<String> result = new TreeSet<>();

        InputStream classInputStream = currentJavaFile.getClassInputStream();
        try {
            ClassReader classReader = new ClassReader(classInputStream);
            
            // 1. Collect the types of member variables and method parameters
            classReader.accept(new RemappingClassAdapter(new ClassVisitor(Opcodes.ASM5) {
            }, new Remapper() {
                @Override
                public String mapFieldName(String owner, String name, String desc) {
                    addType(owner, result);
                    return super.mapFieldName(owner, name, desc);
                }

                @Override
                public String mapMethodName(String owner, String name, String desc) {
                    addType(owner, result);
                    return super.mapMethodName(owner, name, desc);
                }

                @Override
                public String mapDesc(final String desc) {
                    if (desc.startsWith("L")) {
                        addType(desc.substring(1, desc.length() - 1), result);
                    }
                    return super.mapDesc(desc);
                }

                @Override
                public String[] mapTypes(final String[] types) {
                    for (final String type : types) {
                        addType(type, result);
                    }
                    return super.mapTypes(types);
                }

                @Override
                public String mapType(final String type) {
                    addType(type, result);
                    return type;
                }
            }), ClassReader.EXPAND_FRAMES);
            
            // 2. Collect the classes on which methods are called on
            classReader.accept(new ClassVisitor(Opcodes.ASM5) {

                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);;
                    return new MethodVisitor(Opcodes.ASM5, methodVisitor) {

                        @Override
                        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                            addType(owner, result);
                            super.visitMethodInsn(opcode, owner, name, desc, itf);
                        }
                    };
                }
                
            }, ClassReader.EXPAND_FRAMES);
            
            int currentNestingLevel = getJavaClassNestingLevel(currentJavaFile.getJavaClassName());
            
            for(String innerClass : new TreeSet<>(result)) {
                if(isInnerClass(innerClass)) {
                    if(!currentJavaFile.getJavaClassName().equals(innerClass) && 
                            innerClass.startsWith(rootJavaFile.getJavaClassName()) &&
                            getJavaClassNestingLevel(innerClass) > currentNestingLevel) {
                        result.addAll(collectInternal(rootJavaFile, currentJavaFile.getInnerClass(innerClass.substring(innerClass.lastIndexOf("$") + 1) , classLoader), classLoader));
                    }
                }
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Can't load and analyze the Java class in '" + currentJavaFile + "'!", ex);
        } finally {
            try {
                classInputStream.close();
            } catch (IOException ex) {
                // do nothing... we are already in the error case...
            }
        }

        return result;
    }
    
    private void addType(final String type, Set<String> result) {
        final String className = type.replace('/', '.');
        result.add(className);
    }
}
