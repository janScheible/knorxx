package knorxx.framework.generator.reloading;

import com.google.common.base.Optional;
import java.util.ArrayList;
import knorxx.framework.generator.BaseGeneratorTest;
import knorxx.framework.generator.testclass.TestWebPage;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class ReloadingClassLoaderTest extends BaseGeneratorTest {

    @Test
    public void testReloading() throws ClassNotFoundException {
        Class originalTestWebPageClass = getCurrentClassLoader().loadClass(TestWebPage.class.getName());
        
        ReloadingClassLoader reloadingClassLoader = new ReloadingClassLoader(getTestGenerationRoots(), 
                getCurrentClassLoader(), new ArrayList(), Optional.<ReloadPredicate>absent());
        Class newTestWebPageClass = reloadingClassLoader.loadClass(TestWebPage.class.getName());
        
        assertFalse(originalTestWebPageClass.equals(newTestWebPageClass));
        assertTrue(originalTestWebPageClass.getName().equals(newTestWebPageClass.getName()));        
    }    
}
