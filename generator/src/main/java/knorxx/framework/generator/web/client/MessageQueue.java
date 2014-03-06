package knorxx.framework.generator.web.client;

import knorxx.framework.generator.web.client.messagequeue.MessageQueueCallbacks;
import knorxx.framework.generator.web.client.messagequeue.MessageQueueConnection;

/**
 *
 * @author sj
 */
public abstract class MessageQueue<T> {
    
    public MessageQueueConnection<T> subscribe(MessageQueueCallbacks<T> callbacks) {
        return null;
    }
}
