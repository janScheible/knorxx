package knorxx.framework.generator;

import java.util.Set;
import knorxx.framework.generator.single.SingleResult;
import org.joda.time.DateTime;

/**
 *
 * @author sj
 */
public class GenerationResult implements NamedResult {
    
    private final JavaFileWithSource<?> javaFile;
    private final DateTime generationDateTime;
    private final Set<String> dependencies;
    private final SingleResult singleResult;

    protected GenerationResult(JavaFileWithSource<?> javaFile, DateTime generationDateTime, 
            Set<String> dependencies, SingleResult singleResult) {
        this.javaFile = javaFile;
        this.generationDateTime = generationDateTime;
        this.dependencies = dependencies;
        this.singleResult = singleResult;
    }

    public JavaFileWithSource<?> getJavaFile() {
        return javaFile;
    }

    public DateTime getGenerationDateTime() {
        return generationDateTime;
    }

    public SingleResult getSingleResult() {
        return singleResult;
    }

    public Set<String> getDependencies() {
        return dependencies;
    }
    
    @Override
    public String getName() {
        return javaFile.getJavaClassName();
    }
    
    @Override
    public String toString() {
        return getName();
    }
}
