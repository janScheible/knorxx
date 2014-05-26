package knorxx.framework.generator.reloading;

import com.google.common.base.Optional;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import knorxx.framework.generator.GenerationRoots;
import knorxx.framework.generator.JavaFile;
import knorxx.framework.generator.JavaFileOnClasspath;
import static knorxx.framework.generator.util.JavaIdentifierUtils.isJavaCoreClass;

/**
 *
 * @author sj
 */
public class ReloadingClassLoader extends ClassLoader {

    private final GenerationRoots generationRoots;
    private Map<String, JavaClass> classCache = new HashMap<>();
    private final List<AnnotationDescription> annotationDescriptions;
    private final ReloadPredicate reloadPredicate;    

    /**
     * @param reloadPredicate If no predicate is supplied only class with available source are reloaded.
     */
    public ReloadingClassLoader(GenerationRoots generationRoots, ClassLoader parent, 
            List<AnnotationDescription> annotationDescriptions, Optional<? extends ReloadPredicate> reloadPredicate) {
        super(parent);

        this.generationRoots = generationRoots;
        this.annotationDescriptions = annotationDescriptions;
        this.reloadPredicate = reloadPredicate.isPresent() ? reloadPredicate.get() : new ReloadPredicate.HasSource();
    }

    @Override
    public Class loadClass(String name) throws ClassNotFoundException {
        try {
            JavaFile javaFile;
            JavaClass javaClass = classCache.get(name);

            if (javaClass == null) {
                Optional<NotYetLoadedJavaFile> javaFileWithSource = NotYetLoadedJavaFile.create(name, generationRoots);
                
                if (javaFileWithSource.isPresent()) {
                    javaFile = javaFileWithSource.get();
                } else {
                    javaFile = new JavaFileOnClasspath(getParent().loadClass(name));
                }

                if (isJavaCoreClass(name)) {
                    javaClass = new JavaClass(javaFile.getJavaClass());
                } else {
                    Class<?> originalClass = (javaFile instanceof NotYetLoadedJavaFile) ? getParent().loadClass(name) : javaFile.getJavaClass();
                    if (reloadPredicate.apply(originalClass, javaFile instanceof NotYetLoadedJavaFile)) {
                        byte[] classData = javaFile.readClass();
                        AddAnnotationClassChangeVisitor addAnnotationClassChangeVisitor = new AddAnnotationClassChangeVisitor(originalClass, annotationDescriptions);
                        byte[] classDataWithAnnotations = addAnnotationClassChangeVisitor.apply(classData);
                        javaClass = new JavaClass(defineClass(name, classDataWithAnnotations, 0, classDataWithAnnotations.length), classDataWithAnnotations);
                    } else {
                        javaClass = new JavaClass(originalClass);
                    }
                }

                classCache.put(name, javaClass);
            }

            return javaClass.getJavaClass();
        } catch (ClassNotFoundException | IOException | ClassFormatError ex) {
            throw new ClassNotFoundException("Error while reloading the class '" + name + "'.", ex);
        }
    }

	public Optional<byte[]> getByteCode(String javaClassName) {
        JavaClass javaClass = classCache.get(javaClassName);
        
        if(javaClass == null) {
			try {
				loadClass(javaClassName);
			} catch (ClassNotFoundException ex) {
				throw new IllegalStateException("Can't load the class '" + javaClassName + "'!", ex);
			}
            javaClass = classCache.get(javaClassName);
        }
		
		if(javaClass == null || !javaClass.hasClassData()) {
			return Optional.absent();
		} else {
			return Optional.of(javaClass.getClassData());
		}
	}
}
