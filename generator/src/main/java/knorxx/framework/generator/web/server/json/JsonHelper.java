package knorxx.framework.generator.web.server.json;

/**
 *
 * @author sj
 */
public interface JsonHelper {

    <T> T fromJson(String json, Class<T> type);
    <T> T fromJson(Object element, Class<T> type);
    
    String toJson(Object data, Class<?> type);    
    String toJson(Object data);
    String toJson(CustomJsonSerializer customJsonSerializer);
}
