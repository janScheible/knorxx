package knorxx.framework.generator.web.server.rpc;

import com.google.common.base.Strings;
import java.io.PrintWriter;
import java.io.StringWriter;
import knorxx.framework.generator.web.server.json.JsonHelper;

/**
 *
 * @author sj
 */
public class VerboseExceptionMarshaller extends ExceptionMarshaller {
    
    public final static String MESSAGE_PROPERTY = "message";
    public final static String STACK_TRACE_PROPERTY = "stackTrace";
    public final static String NAME_PROPERTY = "name";
    public final static String LINE_NUMER_PROPERTY = "lineNumber";

    @Override
    public String marshall(Throwable throwable, JsonHelper jsonHelper) {
        return jsonHelper.toJson(new MarshalledException(throwable));
    }
    
    private final static class MarshalledException {

        private final String message;
        private final String stackTrace;
        private final String name;
        private final int lineNumber;
        
        private MarshalledException(Throwable throwable) {
            this.message = throwable.getClass().getName() + (Strings.isNullOrEmpty(throwable.getMessage()) ? "" : 
                    (": " + throwable.getMessage()));
            
            StringWriter stackTraceWriter = new StringWriter();
            PrintWriter stackTracePrintWriter = new PrintWriter(stackTraceWriter);
            throwable.printStackTrace(stackTracePrintWriter);
            this.stackTrace = stackTraceWriter.toString();
            
            this.name = throwable.getStackTrace()[0].getClassName();
            this.lineNumber = throwable.getStackTrace()[0].getLineNumber();
        }

        public String getMessage() {
            return message;
        }

        public String getStackTrace() {
            return stackTrace;
        }

        public String getName() {
            return name;
        }

        public int getLineNumber() {
            return lineNumber;
        }
    }
}
