package knorxx.framework.generator.web.generator;

import knorxx.framework.generator.single.SingleFileGeneratorException;
import knorxx.framework.generator.web.generator.testclass.TestCssDefinition;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class CssDefinitionFileGeneratorTest {

    @Test
    public void generate() throws SingleFileGeneratorException {
        CssDefinitionFileGenerator generator = new CssDefinitionFileGenerator();
        CssResult result = generator.generate(TestCssDefinition.class);

        assertThat(result.getSource(), containsString(".HEADING"));
        assertThat(result.getSource(), containsString("'heading'"));
        assertThat(result.getSource(), containsString(".COLOR_HIGHLIGHT"));
        assertThat(result.getSource(), containsString("'colorHighlight'"));
        
        assertThat(result.getCssDefinitions(), hasKey(".heading"));
        assertThat(result.getCssDefinitions(), hasKey(".colorHighlight"));
    }
}
