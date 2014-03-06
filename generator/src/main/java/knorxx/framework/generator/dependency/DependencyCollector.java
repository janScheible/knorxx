package knorxx.framework.generator.dependency;

import com.google.common.base.Optional;
import java.util.Set;
import knorxx.framework.generator.JavaFile;
import knorxx.framework.generator.JavaFileOnClasspath;
import knorxx.framework.generator.JavaFileWithSource;
import static knorxx.framework.generator.util.JavaIdentifierUtils.removeInnerClassesAndInvalidClassNames;

/**
 * Chainable abstract super class for all dependecy collectors.
 *
 * @author sj
 */
public abstract class DependencyCollector {

    private Optional<DependencyCollector> nextCollector = Optional.absent();

    public DependencyCollector() {
    }

    public DependencyCollector(DependencyCollector nextCollector) {
        this.nextCollector = Optional.of(nextCollector);
    }
    
    public Set<String> collect(JavaFileOnClasspath<?> javaFile, ClassLoader classLoader) {
        return genericCollect(javaFile, classLoader);
    }

    public Set<String> collect(JavaFileWithSource<?> javaFile, ClassLoader classLoader) {
        return genericCollect(javaFile, classLoader);
    }

    private Set<String> genericCollect(JavaFile<?> javaFile, ClassLoader classLoader) {
        Set<String> result = collectInternal(javaFile, classLoader);

        if (nextCollector.isPresent()) {
            result.addAll(nextCollector.get().genericCollect(javaFile, classLoader));
        }

        result.remove(javaFile.getJavaClassName());
        return removeInnerClassesAndInvalidClassNames(result);
    }

    protected abstract Set<String> collectInternal(JavaFile<?> javaFile, ClassLoader classLoader);
}
