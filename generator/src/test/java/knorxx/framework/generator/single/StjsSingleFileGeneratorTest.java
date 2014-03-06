package knorxx.framework.generator.single;

import knorxx.framework.generator.single.api.ApiChangeException;
import java.util.HashSet;
import knorxx.framework.generator.BaseGeneratorTest;
import knorxx.framework.generator.JavaFileWithSource;
import knorxx.framework.generator.single.testclass.Empty;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class StjsSingleFileGeneratorTest extends BaseGeneratorTest {
    
    @Test
    public void apiCompatibility() throws SingleFileGeneratorException {
        JavaFileWithSource<Empty> testJavaFile = new JavaFileWithSource<>(Empty.class, getTestGenerationRoots());
        SingleFileGenerator generator = new StjsSingleFileGenerator();
        
        try {
            generator.generate(testJavaFile, getCurrentClassLoader(), new HashSet<String>());
        } catch(ApiChangeException ex) {
            assertFalse("The st-js API changed: " + ex, true);
        }
    }
}
