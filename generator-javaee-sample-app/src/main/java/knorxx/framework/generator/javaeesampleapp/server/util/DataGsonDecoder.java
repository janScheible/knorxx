package knorxx.framework.generator.javaeesampleapp.server.util;

import javax.inject.Inject;
import knorxx.framework.generator.javaeesampleapp.server.model.Data;
import knorxx.framework.generator.web.server.json.JsonHelper;
import org.atmosphere.config.managed.Decoder;

/**
 *
 * @author sj
 */
public class DataGsonDecoder implements Decoder<String, Data>  {
    
    @Inject
    JsonHelper jsonHelper;

    @Override
    public Data decode(String message) {
        return jsonHelper.fromJson(message, Data.class);
    }    
}
