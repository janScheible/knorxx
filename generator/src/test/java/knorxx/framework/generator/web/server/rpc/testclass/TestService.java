package knorxx.framework.generator.web.server.rpc.testclass;

import javax.servlet.http.HttpServletRequest;
import knorxx.framework.generator.web.client.RpcService;
import org.stjs.javascript.functions.Callback1;

/**
 *
 * @author sj
 */
public class TestService implements RpcService {
    
    public static final String GET_BY_ID_METHOD_NAME = "getById";
    public static final String GET_BY_ID_RESULT = "haha";
    
    public static final String THROW_EXCEPTION_METHOD_NAME = "throwException";
    public static final String THROW_EXCEPTION_NAME = "java.lang.IllegalStateException";
    
    public String getById(HttpServletRequest request, long id, Callback1<String> callback, Object scope) {
        return GET_BY_ID_RESULT;
    }
    
    public String throwException(Callback1<String> callback, Object scope) {
        throw new IllegalStateException();
    }
}
