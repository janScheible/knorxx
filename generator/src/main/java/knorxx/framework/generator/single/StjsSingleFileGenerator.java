package knorxx.framework.generator.single;

import japa.parser.ast.CompilationUnit;
import java.io.File;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import knorxx.framework.generator.JavaFileWithSource;
import knorxx.framework.generator.single.api.ApiMethodNameChangeException;
import knorxx.framework.generator.single.api.ApiMethodParamterChangeException;
import knorxx.framework.generator.single.api.ApiReflectionException;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.Generator;
import org.stjs.generator.GeneratorConfiguration;
import org.stjs.generator.GeneratorConfigurationBuilder;
import org.stjs.generator.type.ClassLoaderWrapper;
import org.stjs.generator.writer.JavascriptWriterVisitor;
import org.stjs.javascript.Window;
import org.stjs.javascript.annotation.STJSBridge;
import org.stjs.javascript.annotation.SyntheticType;

/**
 *
 * @author sj
 */
public class StjsSingleFileGenerator extends SingleFileGenerator {

    @Override
    public JavaScriptResult generate(JavaFileWithSource<?> javaFile, ClassLoader classLoader, 
            Set<String> allowedPackages) throws SingleFileGeneratorException {
        JavaFileWithExposedSourceFile javaFileWithExposedSourceFile = new JavaFileWithExposedSourceFile(javaFile);
        
        GeneratorConfigurationBuilder configurationBuilder = new GeneratorConfigurationBuilder();
        configurationBuilder.allowedPackage(javaFileWithExposedSourceFile.getJavaClass().getPackage().getName());
        for(String allowedPackage : allowedPackages) {
            configurationBuilder.allowedPackage(allowedPackage);
        }
        
        GeneratorConfiguration configuration = configurationBuilder.build();

        ClassLoaderWrapper classLoaderWrapper = new ClassLoaderWrapper(classLoader,
                configuration.getAllowedPackages(), configuration.getAllowedJavaLangClasses());

        GenerationContext context = new GenerationContext(javaFileWithExposedSourceFile.getSourceFile(), configuration);
        CompilationUnit cu = parseAndResolve(classLoaderWrapper, javaFileWithExposedSourceFile.getSourceFile(), context);

        JavascriptWriterVisitor generatorVisitor = new JavascriptWriterVisitor(classLoader, false);
        generatorVisitor.visit(cu, context);

        StringWriter stringWriter = new StringWriter();
        stringWriter.write(generatorVisitor.getGeneratedSource());
        stringWriter.flush();

        return new JavaScriptResult(stringWriter.toString());
    }

    @Override
    public boolean isGeneratable(Class<?> javaClass) {
        return super.isGeneratable(javaClass) && 
                javaClass.getAnnotation(STJSBridge.class) == null &&
                javaClass.getAnnotation(SyntheticType.class) == null &&
                !javaClass.getPackage().getName().startsWith(Window.class.getPackage().getName());
    }
    
    /**
     * This special subclass of JavaFileWithSource exposes the underlying file object of the Java source file.
     * Normally we NEVER want that because we prefere InputStream for the shake of easy testability.
     */
    private static final class JavaFileWithExposedSourceFile<T> extends JavaFileWithSource<T> {

        public JavaFileWithExposedSourceFile(JavaFileWithSource<T> javaFileWithSource) {
            super(javaFileWithSource);
        }

        public File getSourceFile() {
            if(sourceFile == null) {
                throw new IllegalStateException("The Java class '" + getJavaClassName() + "' is not accessible via file!");
            }
            
            return sourceFile;
        }
    }    
    
    private CompilationUnit parseAndResolve(ClassLoaderWrapper builtProjectClassLoader, File inputFile, 
            GenerationContext context) throws SingleFileGeneratorException {
        Generator generator = new Generator();

        try {
            for (Method method : generator.getClass().getDeclaredMethods()) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (method.getName().equals("parseAndResolve")) {
                    if(parameterTypes.length == 4 && parameterTypes[0].equals(ClassLoaderWrapper.class) &&
                            parameterTypes[1].equals(File.class) && parameterTypes[2].equals(GenerationContext.class) &&
                            parameterTypes[3].equals(String.class)) {
                        method.setAccessible(true);
                        
                        return (CompilationUnit) method.invoke(generator, builtProjectClassLoader, inputFile, context, "UTF-8");
                    }
                    
                    throw new ApiMethodParamterChangeException("The method 'parseAndResolve' in the class '" + 
                            Generator.class.getName() + "has different parameters than [] now!");
                }
            }
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException ex) {
            throw new ApiReflectionException("Error while accessing the method 'parseAndResolve' of '" + 
                Generator.class.getName() + "' via reflection!", ex);
        } catch(InvocationTargetException ex) {
            throw new SingleFileGeneratorException(ex.getCause(), inputFile);
        }

        throw new ApiMethodNameChangeException("Can't find a method with the name 'parseAndResolve' in the class '" + 
                Generator.class.getName() + "'!");                
    }
}
