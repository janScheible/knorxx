package knorxx.framework.generator.web.client.messagequeue;

import org.stjs.javascript.annotation.SyntheticType;

/**
 *
 * @author sj
 */
@SyntheticType
public class MessageQueueResponse<T> {
    
    public String transport;
    public T data;
}
