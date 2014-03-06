package knorxx.framework.generator.web.generator.testclass;

import knorxx.framework.generator.web.client.MessageQueue;
import org.atmosphere.config.service.ManagedService;

/**
 *
 * @author sj
 */
@ManagedService(path = "/atmosphere/testQueue")
public class TestMessageQueue extends MessageQueue<String> {
    
}
