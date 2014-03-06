package knorxx.framework.generator.web.client.messagequeue;

import org.stjs.javascript.annotation.SyntheticType;
import org.stjs.javascript.functions.Callback1;
import org.stjs.javascript.functions.Callback2;

/**
 *
 * @author sj
 */
@SyntheticType
public class MessageQueueCallbacks<T> {
    
    public Callback1<MessageQueueResponse> onOpen;
    public Callback2<String, MessageQueueRequest> onTransportFailure;
    public Callback1<MessageQueueRequest> onError;
    public Callback1<MessageQueueResponse<T>> onMessage;
    public Callback1<MessageQueueResponse> onClose;
}
