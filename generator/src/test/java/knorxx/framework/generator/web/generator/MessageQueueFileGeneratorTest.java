package knorxx.framework.generator.web.generator;

import knorxx.framework.generator.single.SingleFileGeneratorException;
import knorxx.framework.generator.web.generator.testclass.TestMessageQueue;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class MessageQueueFileGeneratorTest {
    
    @Test
    public void generate() throws SingleFileGeneratorException {
        MessageQueueFileGenerator generator = new MessageQueueFileGenerator();
        String source = generator.generate(TestMessageQueue.class).getSource();
        
        assertThat(source, containsString(".prototype.subscribe"));
        assertThat(source, containsString("'websocket'"));
        assertThat(source, containsString("JSON.stringify"));
    }
}
