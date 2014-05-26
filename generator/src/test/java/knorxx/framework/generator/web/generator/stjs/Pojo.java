package knorxx.framework.generator.web.generator.stjs;

import org.stjs.javascript.annotation.STJSBridge;
import org.stjs.javascript.annotation.Template;

/**
 *
 * @author sj
 */
@STJSBridge
public class Pojo {

    @Template("propertyAccess")
    public int getValue() {
        throw new UnsupportedOperationException();
    }
    
    @Template("propertyAccess")
    public void setValue(int value) {        
    }
}
