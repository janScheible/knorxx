package knorxx.framework.generator.springsampleapp.server.service;

import java.io.IOException;
import java.util.Date;
import knorxx.framework.generator.springadapter.KnorxxDispatcherServletInitializer;
import knorxx.framework.generator.springadapter.atmosphere.WebApplicationContextRetriever;
import knorxx.framework.generator.springsampleapp.server.model.Data;
import knorxx.framework.generator.web.client.MessageQueue;
import knorxx.framework.generator.web.server.json.JsonHelper;
import org.atmosphere.config.managed.Decoder;
import org.atmosphere.config.managed.Encoder;
import org.atmosphere.config.service.Disconnect;
import org.atmosphere.config.service.Get;
import org.atmosphere.config.service.ManagedService;
import org.atmosphere.config.service.Message;
import org.atmosphere.config.service.Post;
import org.atmosphere.config.service.Put;
import org.atmosphere.config.service.Ready;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Source: http://async-io.org/tutorial.html
 * @author sj
 */
@ManagedService(path = KnorxxDispatcherServletInitializer.ATMOSPHERE_URL + "/chat") // RedisBroadcaster.class for a distributed environment
public class ChatQueue extends MessageQueue<Data> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    JsonHelper jsonHelperInAtmosphereInjectionTest;
    
    @Ready
    public void onReady(final AtmosphereResource resource) {
        logger.info("Browser {} connected.", resource.uuid());
    }

    @Disconnect
    public void onDisconnect(AtmosphereResourceEvent event) {
        if (event.isCancelled()) {
            logger.info("Browser {} unexpectedly disconnected", event.getResource().uuid());
        } else if (event.isClosedByClient()) {
            logger.info("Browser {} closed the connection", event.getResource().uuid());
        }
    }
    
    @Post
    public void onPost(AtmosphereResource resource) {
        logger.info("Post");
    }
    
    @Put
    public void onPut(AtmosphereResource resource) {
        logger.info("Put");
    }

    @Get
    public void onGet(AtmosphereResource resource) {
        logger.info("Get");
    }
    
    @Message(encoders = {GsonEncoder.class}, decoders = {GsonDecoder.class})
    public Data onMessage(Data data) throws IOException {
        data.setTime(new Date().getTime());
        logger.info("{} just send {}", data.getAuthor(), data.getMessage());
        return data;
    }
    
    public static class GsonEncoder extends WebApplicationContextRetriever implements Encoder<Data, String> {

        @Override
        public String encode(Data data) {
            return getWebApplicationContext().getBean(JsonHelper.class).toJson(data);
        }
    }
    
    public static class GsonDecoder extends WebApplicationContextRetriever implements Decoder<String, Data> {
        
        @Override
        public Data decode(String message) {
            return getWebApplicationContext().getBean(JsonHelper.class).fromJson(message, Data.class);
        }
    }
}
