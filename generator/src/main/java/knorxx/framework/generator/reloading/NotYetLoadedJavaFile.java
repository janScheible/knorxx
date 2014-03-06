package knorxx.framework.generator.reloading;

import com.google.common.base.Optional;
import knorxx.framework.generator.GenerationRoots;
import knorxx.framework.generator.JavaFileWithSource;
import knorxx.framework.generator.util.JavaIdentifierUtils;

/**
 *
 * @author sj
 */
public class NotYetLoadedJavaFile extends JavaFileWithSource<Void> {
    
    private final String javaClassName;

    private NotYetLoadedJavaFile(String javaClassName, GenerationRoots generationRoots) {
        super(Void.class, javaClassName, generationRoots);
        
        this.javaClassName = javaClassName;
    }
    
    public static Optional<NotYetLoadedJavaFile> create(String javaClassName, GenerationRoots generationRoots) {
        // we have to use this factory pattern because suddenly we want no exception in the case of a not existing class file
        try {
            NotYetLoadedJavaFile notYetLoadedJavaFile = new NotYetLoadedJavaFile(javaClassName, generationRoots);
            return Optional.of(notYetLoadedJavaFile);
        } catch(IllegalStateException ex) {
            return Optional.absent();
        }
    }

    @Override
    public Class<Void> getJavaClass() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getJavaClassName() {
        return javaClassName;
    }

    @Override
    public String getJavaClassSimpleName() {
        return JavaIdentifierUtils.getJavaClassSimpleName(javaClassName);
    }
}
