package knorxx.framework.generator.javaeesampleapp.server.model;

import knorxx.framework.generator.web.client.messagequeue.annotation.QueueMessage;

/**
 *
 * @author sj
 */
@QueueMessage
public class Data {
    
    private String message;
    private String author;
    private long time;

    public Data() {
        this("", "");
    }
    
    public Data(String message, String author) {
        this.message = message;
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public String getMessage() {
        return message;
    }

    public long getTime() {
        return time;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
