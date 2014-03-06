package knorxx.framework.generator.web.server.rpc;

import com.google.common.base.Joiner;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
import knorxx.framework.generator.web.server.json.CustomJsonSerializer;

/**
 *
 * @author sj
 */
public class RpcCall implements CustomJsonSerializer {
    
    public final static String SERVICE_NAME_PROPERTY = "serviceName";
    public final static String METHOD_NAME_PROPERTY = "methodName";
    public final static String ARGUMENTS_PROPERTY = "arguments";
    
    private final String serviceName;
    private final String methodName;
    
    private final List<String> argumentsJsons;

    public RpcCall(String serviceName, String methodName, List<String> argumentsJsons) {
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.argumentsJsons = argumentsJsons;
    }

    public RpcCall(String callJson) {
        JsonParser jp = new JsonParser();
        JsonElement data =  jp.parse(callJson);
        
        serviceName = data.getAsJsonObject().get(SERVICE_NAME_PROPERTY).getAsString();
        methodName = data.getAsJsonObject().get(METHOD_NAME_PROPERTY).getAsString();
        
        JsonArray argumentsArray = data.getAsJsonObject().get(ARGUMENTS_PROPERTY).getAsJsonArray();
        argumentsJsons = new ArrayList<>(); 
        for(JsonElement element : argumentsArray) {
            argumentsJsons.add(element.toString());
        }
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<String> getArgumentsJsons() {
        return argumentsJsons;
    }
    
    @Override
    public String toJson() {
        return "{" +
                    "\"" + SERVICE_NAME_PROPERTY + "\" : \"" + serviceName + "\"," +
                    "\"" + METHOD_NAME_PROPERTY + "\" : \"" + methodName + "\"," +
                    "\"" + ARGUMENTS_PROPERTY + "\" : [" + Joiner.on(", ").join(argumentsJsons) + "]" +
                "}";
    }
}
