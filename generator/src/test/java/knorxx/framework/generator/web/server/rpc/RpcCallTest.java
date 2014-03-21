package knorxx.framework.generator.web.server.rpc;

import knorxx.framework.generator.web.server.json.GsonHelper;
import knorxx.framework.generator.web.server.json.JsonHelper;
import static knorxx.framework.generator.web.server.rpc.RpcCall.*;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class RpcCallTest {
    
    @Test
    public void rpcCallFromJson() {
        String jsonInput = 
                "{" +
                    "\"" + SERVICE_NAME_PROPERTY + "\" : \"dummy.test.Service\"," +
                    "\"" + METHOD_NAME_PROPERTY + "\" : \"getAllButOne\"," +
                    "\"" + CSRF_PROTECTION_TOKEN_PROPERTY + "\" : \"CSRF_PROTECTION_TOKEN\"," +
                    "\"" + ARGUMENTS_PROPERTY + "\" : [12, \"haha\"]" +
                "}";
        
        RpcCall rpcCallFromJson = new RpcCall(jsonInput);
        
        RpcCall rpcCallFromRpcCall = new RpcCall(rpcCallFromJson.getServiceName(), rpcCallFromJson.getMethodName(),
                rpcCallFromJson.getCsrfProtectionToken(), rpcCallFromJson.getArgumentsJsons());
        
        JsonHelper jsonHelper = new GsonHelper();
        Assert.assertEquals(jsonHelper.toJson(rpcCallFromRpcCall), jsonInput);
    }
}