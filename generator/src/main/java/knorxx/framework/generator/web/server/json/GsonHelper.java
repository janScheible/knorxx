package knorxx.framework.generator.web.server.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Base class for a Gson implementation of JsonHelper. Custom type adapters can be added by subclassing this 
 * class. Only a converter for DateTime is registerd by default.
 * 
 * @author sj
 */
public class GsonHelper implements JsonHelper {
    
    private final Gson gson;

    public GsonHelper(GsonBuilder gsonBuilder) {
        gsonBuilder.registerTypeAdapter(DateTime.class, new JsonSerializer<DateTime>() {
            @Override
            public JsonElement serialize(DateTime src, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(ISODateTimeFormat.dateTime().print(src));
            }
        });
        
        gson = gsonBuilder.create();
    }

    public GsonHelper() {
        this(new GsonBuilder());
    }

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
