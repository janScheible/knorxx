package knorxx.framework.generator.reloading;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

/**
 *
 * @author sj
 */
public abstract class ClassChangeVisitor extends ClassVisitor {

    private final ClassWriter classWriter;

    public ClassChangeVisitor(ClassWriter classWriter) {
        super(Opcodes.ASM5, classWriter);
        this.classWriter = classWriter;
    }

    public byte[] toByteArray() {
        return classWriter.toByteArray();
    }
    
    public byte[] apply(byte[] classData) {
        ClassReader classReader = new ClassReader(classData);
        classReader.accept(this, 0);  
        return toByteArray();
    }
}
