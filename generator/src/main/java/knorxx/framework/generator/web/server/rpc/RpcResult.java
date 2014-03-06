/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package knorxx.framework.generator.web.server.rpc;

import knorxx.framework.generator.web.server.json.CustomJsonSerializer;

/**
 *
 * @author sj
 */
public class RpcResult implements CustomJsonSerializer {

    public final static String JSON_RESULT_PROPERTY = "result";
    public final static String STATUS_PROPERTY = "status";
    
    
    public enum Status {
        SUCCESS, EXCEPTION
    };
    
    private final Status status;
    private final String jsonResult;

    public RpcResult(Status status, String jsonResult) {
        this.status = status;
        this.jsonResult = jsonResult;
    }

    public String getJsonResult() {
        return jsonResult;
    }

    public Status getStatus() {
        return status;
    }
    
    @Override
    public String toJson() {
        return "{"
                + "\"" + JSON_RESULT_PROPERTY + "\" : " + jsonResult + ","
                + "\"" + STATUS_PROPERTY + "\" : \"" + status.name() + "\""
            + "}";
    }
}
