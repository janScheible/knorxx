package knorxx.framework.generator.web.generator.stjs;

import org.junit.Test;
import static org.stjs.generator.utils.GeneratorTestHelper.assertCodeContains;

/**
 *
 * @author sj
 */
public class TypedNewClassWriterTest {

	@Test
    public void testPropertyGet() {
		assertCodeContains(SyntheticClass.class, "={\"@type\": \"" + SyntheticClass.class.getName() + "\"}");
    }
}
