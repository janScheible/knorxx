package knorxx.framework.generator.springsampleapp.client.page;

import static knorxx.framework.generator.springsampleapp.client.appearance.Appearance.HEADING_STYLE;
import knorxx.framework.generator.springsampleapp.client.util.AbstractWebPage;
import knorxx.framework.generator.springsampleapp.server.model.Data;
import knorxx.framework.generator.springsampleapp.server.service.ChatQueue;
import knorxx.framework.generator.web.client.messagequeue.MessageQueueCallbacks;
import knorxx.framework.generator.web.client.messagequeue.MessageQueueConnection;
import knorxx.framework.generator.web.client.messagequeue.MessageQueueResponse;
import org.springframework.stereotype.Component;
import org.stjs.javascript.Date;
import org.stjs.javascript.dom.Element;
import org.stjs.javascript.jquery.Event;
import static org.stjs.javascript.jquery.GlobalJQuery.$;
import org.stjs.javascript.jquery.JQuery;

/**
 * @author sj
 */
@Component
public class ChatWebPage extends AbstractWebPage {
    
    private static ChatQueue chatQueue = new ChatQueue();
    
    private JQuery content;
    private JQuery input;
    private JQuery status;

    private MessageQueueConnection<Data> connection;
    
    private String myName = null;
    private String author = null;
    private boolean logged = false;
    
    @Override
    public void onLoad() {
        createGui();
        subscribeQueue();
    }

    private void createGui() {
        final ChatWebPage that = this;
        
        $(TITLE_ID).text("Chat WebPage").addClass(HEADING_STYLE);

        $(CONTENT_ID).append(content = $("<div id=\"chatContent\"></div>"));
        $(CONTENT_ID).append(input = $("<input id=\"chatInput\" type=\"text\"></input>"));
        $(CONTENT_ID).append(status = $("<div id=\"chatStatus\"></div>"));

        input.keydown((Event event, Element THIS) ->  {
			that.onInputKeyDown(event);
			return true;
        });                
    }

    private void subscribeQueue() {
        final ChatWebPage that = this;
        
        MessageQueueCallbacks<Data> callbacks = new MessageQueueCallbacks<Data>();

        callbacks.onOpen = (MessageQueueResponse response) -> {
			that.onOpen(response);
        };

        callbacks.onMessage = (MessageQueueResponse<Data> response) -> {
			that.onMessage(response);
        };

        callbacks.onClose = (MessageQueueResponse response) -> {
			that.onClose(response);
        };

        connection = chatQueue.subscribe(callbacks);
    }
    
    private void onOpen(MessageQueueResponse response) {
        content.html("<p>Atmosphere connected using " + response.transport + "</p>");
        input.removeAttr("disabled").focus();
        status.text("Choose name:");
    }

    private void onMessage(MessageQueueResponse<Data> response) {
        // We need to be logged first.
        if(myName == null) {
            return;
        }
        
        if(!logged) {
            logged = true;
            status.text(myName + ": ").css("color", "blue");
            input.removeAttr("disabled").focus();
        } else {
            input.removeAttr("disabled");
            addMessage(response.data, (response.data.getAuthor() == author) ? "blue" : "black");
        }
    }
    
    private void onInputKeyDown(Event event) {
        if(event.keyCode == 13) {
            String msg = (String) $(input).val();
            if(author == null) {
                author = msg;
            }
            
            Data data = new Data();
            data.setAuthor(author);
            data.setMessage(msg);
            input.val("");
            connection.push(data);
            
            input.attr("disabled", "disabled");
            if(myName == null) {
                myName = msg;
            }
        }        
    }
    
    private void addMessage(Data data, String color) {
        Date datetime = new Date(data.getTime());
        content.append("<p><span style=\"color:" + color + "\">" + data.getAuthor() + "</span> @ " +
                +(datetime.getHours() < 10 ? '0' + datetime.getHours() : datetime.getHours()) + ':'
                + (datetime.getMinutes() < 10 ? '0' + datetime.getMinutes() : datetime.getMinutes())
                + ": " + data.getMessage() + "</p>");
    }

    private void onClose(MessageQueueResponse response) {
        logged = false;
    }
}
