package knorxx.framework.generator;

import java.util.Arrays;
import java.util.Collection;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 *
 * @author sj
 */
@RunWith(Parameterized.class)
public abstract class BaseGeneratorTestWithMultipleClassLoader extends BaseGeneratorTest {

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        BaseGeneratorTest baseGeneratorTest = new BaseGeneratorTest() {
        };
        Object[][] data = new Object[][]{{baseGeneratorTest.getTestClassLoader()},
            {baseGeneratorTest.getReloadingTestClassLoader()}};
        return Arrays.asList(data);
    }
    
    private ClassLoader currentClassLoader = getTestClassLoader();

    public BaseGeneratorTestWithMultipleClassLoader(ClassLoader currentClassLoader) {
        this.currentClassLoader = currentClassLoader;
    }
    
    @Override
    protected ClassLoader getCurrentClassLoader() {
        return currentClassLoader;
    }
}
