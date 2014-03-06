package knorxx.framework.generator.javaeesampleapp.server.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import javax.inject.Singleton;
import knorxx.framework.generator.web.server.json.CustomJsonSerializer;
import knorxx.framework.generator.web.server.json.JsonHelper;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

/**
 *
 * @author sj
 */
@Singleton
public class GsonHelper implements JsonHelper {
    
    private final Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new JsonSerializer<DateTime>() {
        @Override
        public JsonElement serialize(DateTime src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(ISODateTimeFormat.dateTime().print(src));
        }
    }).create();
    
    @Override
    public <T> T fromJson(Object element, Class<T> type) {
        if(element instanceof JsonElement) {
            return gson.fromJson((JsonElement)element, type);
        } else {
            return fromJson(element.toString(), type);
        }
    }

    @Override
    public <T> T fromJson(String json, Class<T> type) {
        return gson.fromJson(json, type);
    }
    
    @Override
    public String toJson(Object data, Class<?> type) {
        return gson.toJson(data, type);
    }

    @Override
    public String toJson(Object data) {
        return gson.toJson(data);
    }

    @Override
    public String toJson(CustomJsonSerializer customJsonSerializer) {
        return customJsonSerializer.toJson();
    }
}
