package knorxx.framework.generator.javaeesampleapp.server.util;

import javax.inject.Inject;
import knorxx.framework.generator.javaeesampleapp.server.model.Data;
import knorxx.framework.generator.web.server.json.JsonHelper;
import org.atmosphere.config.managed.Encoder;

/**
 *
 * @author sj
 */
public class DataGsonEncoder implements Encoder<Data, String> {

    @Inject
    JsonHelper jsonHelper;
        
    @Override
    public String encode(Data data) {
        return jsonHelper.toJson(data);        
    }    
}
