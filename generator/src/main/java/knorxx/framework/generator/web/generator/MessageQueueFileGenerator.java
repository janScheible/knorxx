package knorxx.framework.generator.web.generator;

import knorxx.framework.generator.single.JavaScriptResult;
import knorxx.framework.generator.single.SingleFileGeneratorException;
import static knorxx.framework.generator.util.JavaIdentifierUtils.hasSuperclassOrImplementsInterface;
import knorxx.framework.generator.web.client.JsonHelper;
import knorxx.framework.generator.web.client.MessageQueue;
import knorxx.framework.generator.web.generator.stjs.StjsJavaScriptClassBuilder;
import org.atmosphere.config.service.ManagedService;

/**
 *
 * @author sj
 */
public class MessageQueueFileGenerator extends SpecialFileGenerator {

    @Override
    public JavaScriptResult generate(Class<?> javaClass) throws SingleFileGeneratorException {
        String messageQueueUrl = javaClass.getAnnotation(ManagedService.class).path();
        
        StjsJavaScriptClassBuilder builder = new StjsJavaScriptClassBuilder(javaClass)
            .constructor(javaClass)
            ._constructor()
            .function(javaClass, "subscribe", "callbacks")
                .code("var request = {};").newLine()
                .code("$.extend(request, callbacks);").newLine()
                .newLine()
                .code("request.url =").literal(messageQueueUrl).semicolon().newLine()
                .code("request.contentType = ").literal("application/json").semicolon().newLine()
                .code("request.trackMessageLength = ").literal(true).semicolon().newLine()
                .code("request.shared = ").literal(false).semicolon().newLine()
                .code("request.transport = ").literal("websocket").semicolon().newLine()
                .code("request.fallbackTransport = ").literal("long-polling").semicolon().newLine()
                .newLine()
                .if$("callbacks.onMessage")
                    .code("request.onMessage = ").anonymousFunction("response")
                        .code("response.data = JSON.parse(response.responseBody, %s);", 
                                JsonHelper.PARSE_REVIVER_FUNCTION).newLine()
                        .code("delete response.responseBody;").newLine()
                        .code("callbacks.onMessage(response);").newLine()
                    ._function()
                ._if()
                .newLine()
                .code("var connection = $.atmosphere.subscribe(request);").newLine()
                .code("var oldPushFunction = connection.push;").newLine()
                .code("connection.push = ").anonymousFunction("data")
                    .code("oldPushFunction.call(connection, JSON.stringify(data, %s));", 
                            JsonHelper.STRINGIFY_REPLACER_FUNCTION).newLine()
                ._function()
                .newLine()
                .code("return connection;").newLine()                
            ._function();
        
        return new JavaScriptResult(builder.create());
    }

    @Override
    public boolean isGeneratable(Class<?> javaClass) {
        return !javaClass.getName().equals(MessageQueue.class.getName()) && 
                hasSuperclassOrImplementsInterface(javaClass, MessageQueue.class.getName());
    }
}
