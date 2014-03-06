package knorxx.framework.generator.web.generator.testclass;

import javax.servlet.http.HttpServletRequest;
import knorxx.framework.generator.web.client.RpcService;
import org.stjs.javascript.functions.Callback1;

/**
 *
 * @author sj
 */
public class TestService implements RpcService {
    
    public String getById(HttpServletRequest request, long id, Callback1<String> callback, Object scope) {
        return null;        
    }
}
