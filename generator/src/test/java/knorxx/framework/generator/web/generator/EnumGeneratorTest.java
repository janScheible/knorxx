package knorxx.framework.generator.web.generator;

import knorxx.framework.generator.single.SingleFileGeneratorException;
import knorxx.framework.generator.web.generator.testclass.TestEnum;
import knorxx.framework.generator.web.generator.testclass.TestEnumWithValues;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class EnumGeneratorTest {

	@Test
    public void generateWithoutValues() throws SingleFileGeneratorException {
		EnumGenerator generator = new EnumGenerator();
		String source = generator.generate(TestEnum.class).getSource();
		
		assertThat(source, containsString("stjs.enumeration"));
		assertThat(source, containsString("'FIRST'"));
		assertThat(source, containsString("'SECOND'"));
		assertThat(source, containsString("'THIRD'"));
	}
	
	@Test
    public void generateWithValues() throws SingleFileGeneratorException {
		EnumGenerator generator = new EnumGenerator();
		String source = generator.generate(TestEnumWithValues.class).getSource();
		
		assertThat(source, containsString("return 'Test'"));
		assertThat(source, containsString("return 'Bla'"));
		assertThat(source, containsString("return 42"));
		assertThat(source, containsString("return 17"));
		assertThat(source, containsString("return true"));
		assertThat(source, containsString("return false"));
	}
}

