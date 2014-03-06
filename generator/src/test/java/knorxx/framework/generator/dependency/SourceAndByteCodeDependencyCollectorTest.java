package knorxx.framework.generator.dependency;

import java.io.IOException;
import java.util.Set;
import knorxx.framework.generator.BaseGeneratorTestWithMultipleClassLoader;
import knorxx.framework.generator.JavaFileWithSource;
import knorxx.framework.generator.dependency.testclass.sourceandbytecode.All;
import knorxx.framework.generator.dependency.testclass.sourceandbytecode.ConstantHolder;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class SourceAndByteCodeDependencyCollectorTest extends BaseGeneratorTestWithMultipleClassLoader {

    public SourceAndByteCodeDependencyCollectorTest(ClassLoader currentClassLoader) {
        super(currentClassLoader);
    }
    
    @Test
    public void combination() throws IOException {
        JavaFileWithSource<All> allJavaFile = new JavaFileWithSource<>(All.class, getTestGenerationRoots());

        ByteCodeDependencyCollector dependencyCollector = new ByteCodeDependencyCollector(new JavaSourceDependencyCollector());
        Set<String> dependencies = dependencyCollector.collect(allJavaFile, getCurrentClassLoader());

        assertThat(dependencies, hasItems(new String[]{"java.awt.Color", ConstantHolder.class.getName()}));
    }
}
