package knorxx.framework.generator.javaeesampleapp.server.service;

import java.io.IOException;
import java.util.Date;
import knorxx.framework.generator.javaeesampleapp.server.model.Data;
import knorxx.framework.generator.javaeesampleapp.server.util.DataGsonDecoder;
import knorxx.framework.generator.javaeesampleapp.server.util.DataGsonEncoder;
import knorxx.framework.generator.web.client.MessageQueue;
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

/**
 * Source: http://async-io.org/tutorial.html
 * @author sj
 */
@ManagedService(path = "/atmosphere/chat") // RedisBroadcaster.class for a distributed environment
public class ChatQueue extends MessageQueue<Data> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
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
    
    @Message(encoders = {DataGsonEncoder.class}, decoders = {DataGsonDecoder.class})
    public Data onMessage(Data data) throws IOException {
        data.setTime(new Date().getTime());
        logger.info("{} just send {}", data.getAuthor(), data.getMessage());
        return data;
    }
}
