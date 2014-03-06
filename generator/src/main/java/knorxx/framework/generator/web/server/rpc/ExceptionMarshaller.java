package knorxx.framework.generator.web.server.rpc;

import knorxx.framework.generator.web.server.json.JsonHelper;

/**
 *
 * @author sj
 */
public abstract class ExceptionMarshaller {
    
    public abstract String marshall(Throwable throwable, JsonHelper jsonHelper);
}
