package knorxx.framework.generator.web.server.rpc;

import knorxx.framework.generator.web.server.errorhandling.ErrorCodeGenerator;
import knorxx.framework.generator.web.server.json.JsonHelper;

/**
 *
 * @author sj
 */
public class ErrorCodeExceptionMarshaller extends ExceptionMarshaller {
    
    private final ErrorCodeGenerator errorCodeGenerator;

    public ErrorCodeExceptionMarshaller(ErrorCodeGenerator errorCodeGenerator) {
        this.errorCodeGenerator = errorCodeGenerator;
    }
    
    @Override
    public String marshall(Throwable throwable, JsonHelper jsonHelper) {
        return jsonHelper.toJson(errorCodeGenerator.generate());
    }
}
