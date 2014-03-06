package knorxx.framework.generator.web.client;

import org.stjs.javascript.Date;
import static org.stjs.javascript.Global.typeof;
import org.stjs.javascript.annotation.Namespace;

/**
 * This file contains a reviver for JSON.parse(...) and a replacer for JSON.stringify(...). These two functions must
 * ALWAYS be used when dealing with json. This allows a consistent serialization/deserialization process.
 * 
 * Thes adaptions in this file must match to your implementation of the JsonHelper class in Java!!!
 * 
 * @author sj
 */
@Namespace("knorxx.framework.generator.web.client")
public class JsonHelper {
    
    public static final String INSTANCE_NAME = "knorxxJsonHelper";
    
    public static final String PARSE_REVIVER_FUNCTION = INSTANCE_NAME + ".parseReviver";
    public static final String STRINGIFY_REPLACER_FUNCTION = INSTANCE_NAME + ".stringifyReplacer";
    
    public Object parseReviver(String key, Object value) {
        if (typeof(value) == "string" && ((String) value).length() == 29) {
            try {
                Date valueAsDate = new Date((String) value);
                if (valueAsDate.toString() != "Invalid Date") {
                    return valueAsDate;
                }
            } catch (Exception exception) {
                // do nothing... it's simply not a date... ;-)
            }
        }

        return value;
    }

    public Object stringifyReplacer(String key, Object value) {
        return value;
    }
}
