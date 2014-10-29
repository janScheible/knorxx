package knorxx.framework.generator.single;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.sun.source.tree.CompilationUnitTree;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import javax.lang.model.type.TypeMirror;
import knorxx.framework.generator.JavaFileWithSource;
import knorxx.framework.generator.reloading.ReloadingClassLoader;
import knorxx.framework.generator.single.api.ApiChangeException;
import knorxx.framework.generator.single.api.ApiMethodNameChangeException;
import knorxx.framework.generator.single.api.ApiMethodParamterChangeException;
import knorxx.framework.generator.single.api.ApiReflectionException;
import knorxx.framework.generator.util.JavaIdentifierUtils;
import org.stjs.generator.GenerationContext;
import org.stjs.generator.Generator;
import org.stjs.generator.GeneratorConfiguration;
import org.stjs.generator.GeneratorConfigurationBuilder;
import org.stjs.generator.javascript.JavaScriptBuilder;
import org.stjs.generator.javascript.rhino.RhinoJavaScriptBuilder;
import org.stjs.generator.name.DefaultJavaScriptNameProvider;
import org.stjs.generator.name.JavaScriptNameProvider;
import org.stjs.generator.plugin.GenerationPlugins;
import org.stjs.generator.transformation.ByteCodeLoader;
import org.stjs.generator.transformation.SourceCodeLoader;
import org.stjs.javascript.Window;
import org.stjs.javascript.annotation.Namespace;
import org.stjs.javascript.annotation.STJSBridge;
import org.stjs.javascript.annotation.SyntheticType;

/**
 *
 * @author sj
 */
public class StjsSingleFileGenerator extends SingleFileGenerator {
    
    @Override
    public JavaScriptResult generate(JavaFileWithSource<?> javaFile, final ClassLoader classLoader,
            Set<String> allowedPackages) throws SingleFileGeneratorException {
        JavaFileWithExposedSourceFile javaFileWithExposedSourceFile = new JavaFileWithExposedSourceFile(javaFile);
        Class<?> clazz = javaFile.getJavaClass();
        
        GeneratorConfigurationBuilder configurationBuilder = new GeneratorConfigurationBuilder();
        configurationBuilder.allowedPackage(javaFileWithExposedSourceFile.getJavaClass().getPackage().getName());
        for(String allowedPackage : allowedPackages) {
            configurationBuilder.allowedPackage(allowedPackage);
        }
        configurationBuilder.sourceEncoding("UTF-8");
        configurationBuilder.generateSourceMap(true);
		
		configurationBuilder.sourceCodeLoader(new SourceCodeLoader() {
			@Override
			public String load(Class clazz, String javaSourceCode) {
				Annotation namespace = clazz.getAnnotation(Namespace.class);
				// TODO Add more things... (e.g. templates?)
				if (namespace != null && !javaSourceCode.contains("@Namespace")) {
					// TODO This is more like a hack... ;-(
					javaSourceCode = javaSourceCode.replaceFirst(Pattern.quote("public "),
							"@org.stjs.javascript.annotation.Namespace(\""
							+ ((Namespace) namespace).value()
							+ "\")\npublic ");
				}

				return javaSourceCode;
			}
		});
		
		configurationBuilder.byteCodeLoader(new ByteCodeLoader() {
			@Override
			public InputStream load(String javaClassName, URI uri) throws IOException {
				if (classLoader instanceof ReloadingClassLoader) {
					Optional<byte[]> byteCode = ((ReloadingClassLoader) classLoader).getByteCode(javaClassName);
					if(byteCode.isPresent()) {
						return new ByteArrayInputStream(byteCode.get());
					}
				}

				return uri.toURL().openStream();
			}
		});
        
        JavaScriptNameProvider names = new DefaultJavaScriptNameProvider() {

			@Override
			public String getTypeName(GenerationContext<?> context, TypeMirror type) {
				String typeName = super.getTypeName(context, type);
				
				// NOTE Using toString() here seems to work currently but is super hacky and might break in the future... ;-(
				if(!type.toString().equals(typeName) && !type.toString().contains("$")) {
					try {
						String className = type.toString();
						className = className.contains("<") ? className.substring(0, className.indexOf("<")) : className;
						Class javaClass = classLoader.loadClass(className);
						
						if(JavaIdentifierUtils.hasSuperclassOrImplementsInterface(javaClass, Enum.class.getName())) {
							return type.toString();
						}
					} catch (ClassNotFoundException ex) {
						context.addError(context.getCurrentPath().getLeaf(), "Error while resolving the class '" + 
								type.toString() + "' for namespace generation of an external enum type.");
					}
				}
				
				return typeName;
			}
		};
		GenerationPlugins<Object> currentClassPlugins = new GenerationPlugins<>().forClass(clazz);
        
        GeneratorConfiguration configuration = configurationBuilder.build();        

        Map<GenerationContext.AnnotationCacheKey, Object> cacheAnnotations = Maps.newHashMap();
		GenerationContext<Object> context = new GenerationContext<>(javaFileWithExposedSourceFile.getSourceFile(), 
                        configuration, names, null, classLoader, cacheAnnotations, (JavaScriptBuilder) new RhinoJavaScriptBuilder());
		
		try {
			clazz = classLoader.loadClass(javaFile.getJavaClassName());
		} catch (ClassNotFoundException ex) {
			throw new SingleFileGeneratorException(ex, javaFileWithExposedSourceFile.getSourceFile());
		}
		CompilationUnitTree cu = parseAndResolve(clazz, javaFileWithExposedSourceFile.getSourceFile(), context, classLoader, 
                configuration.getSourceEncoding(), configuration.getSourceCodeLoader(), configuration.getByteCodeLoader());

		// check the code
		currentClassPlugins.getCheckVisitor().scan(cu, (GenerationContext) context);
		context.getChecks().check();

		// generate the javascript code
		Object javascriptRoot = currentClassPlugins.getWriterVisitor().scan(cu, context);
		// check for any error arriving during writing
		context.getChecks().check();

        // write JavaScript
        StringWriter javaScriptStringWriter = new StringWriter();
        context.writeJavaScript(javascriptRoot, javaScriptStringWriter);
        javaScriptStringWriter.flush();     
        
        // write SourceMap
        StringWriter sourceMapStringWriter = new StringWriter();
        try {
            context.writeSourceMap(sourceMapStringWriter);
        } catch(IOException ex) {
            throw new IllegalStateException("An error occured while writing the source map for '" + 
                    javaFile.getJavaClassName() + "'.", ex);
        }
        sourceMapStringWriter.flush();

        return new JavaScriptResult(stripSourceMappingUrl(javaScriptStringWriter.toString()), sourceMapStringWriter.toString());
    }
    
    private String stripSourceMappingUrl(String javaScriptSource) {
        int sourceMappingUrlIndex = javaScriptSource.lastIndexOf("//@ sourceMappingURL=");
        
        if(sourceMappingUrlIndex < 0) {
            throw new ApiChangeException("The source mapping URL is missing in the generated file!");
        }
        
        return javaScriptSource.substring(0, sourceMappingUrlIndex).trim();
    }

    @Override
    public boolean isGeneratable(Class<?> javaClass) {
        return super.isGeneratable(javaClass) && 
				!javaClass.isAnnotation() && 
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
    
    private CompilationUnitTree parseAndResolve(Class clazz, File inputFile, GenerationContext<?> context, ClassLoader builtProjectClassLoader,
			String sourceEncoding, SourceCodeLoader sourceCodeLoader, ByteCodeLoader byteCodeLoader) throws SingleFileGeneratorException {
        Generator generator = new Generator();

        try {
            for (Method method : generator.getClass().getDeclaredMethods()) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (method.getName().equals("parseAndResolve")) {
                    if(parameterTypes.length == 7 && parameterTypes[0].equals(Class.class) &&
							parameterTypes[1].equals(File.class) &&
                            parameterTypes[2].equals(GenerationContext.class) && 
                            parameterTypes[3].equals(ClassLoader.class) &&
                            parameterTypes[4].equals(String.class) &&
							parameterTypes[5].equals(SourceCodeLoader.class) &&
							parameterTypes[6].equals(ByteCodeLoader.class)) {
                        method.setAccessible(true);
                        
                        return (CompilationUnitTree) method.invoke(generator, clazz, inputFile, context, 
								builtProjectClassLoader, sourceEncoding, sourceCodeLoader, byteCodeLoader);
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
