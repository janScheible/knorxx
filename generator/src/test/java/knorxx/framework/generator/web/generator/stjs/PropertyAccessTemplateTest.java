package knorxx.framework.generator.web.generator.stjs;

import org.junit.Test;
import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;

/**
 *
 * @author sj
 */
public class PropertyAccessTemplateTest {
    
    @Test
    public void testPropertyGet() {
        assertCodeContains(PropertyAccessGet.class, "=pojo.value");
    }
    
    @Test
    public void testPropertyIs() {
        assertCodeContains(PropertyAccessIs.class, "=pojo.valid");
    }	
	
    @Test
    public void testPropertySet() {
        assertCodeContains(PropertyAccessSet.class, "pojo.value=");
    }	
}
