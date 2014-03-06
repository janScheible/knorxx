package knorxx.framework.generator.dependency;

import com.google.common.collect.Sets;
import java.util.Set;
import knorxx.framework.generator.BaseGeneratorTest;
import knorxx.framework.generator.JavaFile;
import knorxx.framework.generator.JavaFileOnClasspath;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class DependencyCollectorTest extends BaseGeneratorTest {

    @Test
    public void testChainability() {        
        final String firstClassName = Integer.class.getName();
        final String secondClassName = Float.class.getName();
        final String thirdClassName = Byte.class.getName();

        DependencyCollector third = new DependencyCollector() {
            @Override
            protected Set<String> collectInternal(JavaFile<?> javaFile, ClassLoader classLoader) {
                return Sets.newHashSet(thirdClassName);
            }
        };
        
        DependencyCollector second = new DependencyCollector(third) {
            @Override
            protected Set<String> collectInternal(JavaFile<?> javaFile, ClassLoader classLoader) {
                return Sets.newHashSet(secondClassName);
            }
        };
        
        DependencyCollector first = new DependencyCollector(second) {
            @Override
            protected Set<String> collectInternal(JavaFile<?> javaFile, ClassLoader classLoader) {
                return Sets.newHashSet(firstClassName);
            } 
        };
        
        Set<String> dependencies = first.collect(new JavaFileOnClasspath<>(String.class), getCurrentClassLoader());
        
        assertTrue(dependencies.contains(firstClassName));
        assertTrue(dependencies.contains(secondClassName));
        assertTrue(dependencies.contains(thirdClassName));        
    }
}
