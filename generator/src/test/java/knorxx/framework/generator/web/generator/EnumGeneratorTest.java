package knorxx.framework.generator.web.generator;

import knorxx.framework.generator.single.SingleFileGeneratorException;
import knorxx.framework.generator.web.generator.testclass.TestEnum;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class EnumGeneratorTest {

	@Test
    public void generate() throws SingleFileGeneratorException {
		EnumGenerator generator = new EnumGenerator();
		String source = generator.generate(TestEnum.class).getSource();
		
		assertThat(source, containsString("stjs.enumeration"));
		assertThat(source, containsString("'FIRST'"));
		assertThat(source, containsString("'SECOND'"));
		assertThat(source, containsString("'THIRD'"));
	}
}

