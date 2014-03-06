package knorxx.framework.generator.web.client.messagequeue;

import org.stjs.javascript.annotation.SyntheticType;

/**
 *
 * @author sj
 */
@SyntheticType
public class MessageQueueRequest<T> extends MessageQueueCallbacks<T> {
    
    public String url;
    public String contentType;
    public boolean trackMessageLength;
    public boolean shared;
    public String transport;
    public String fallbackTransport;
}
