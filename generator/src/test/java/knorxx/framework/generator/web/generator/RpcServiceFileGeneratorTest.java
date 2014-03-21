package knorxx.framework.generator.web.generator;

import knorxx.framework.generator.single.SingleFileGeneratorException;
import knorxx.framework.generator.web.generator.testclass.TestService;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class RpcServiceFileGeneratorTest {

    @Test
    public void generate() throws SingleFileGeneratorException {
        RpcServiceFileGenerator generator = new RpcServiceFileGenerator("/csrfProtectionCookiePath", "/rpcUrl");
        String source = generator.generate(TestService.class).getSource();
        
        assertThat(source, containsString(".prototype.getById"));
        assertThat(source, containsString(TestService.class.getName()));
        assertThat(source, containsString("JSON.parse"));
    }
}
