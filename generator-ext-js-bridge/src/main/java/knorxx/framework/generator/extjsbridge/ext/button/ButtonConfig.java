package knorxx.framework.generator.extjsbridge.ext.button;

import knorxx.framework.generator.extjsbridge.Config;
import org.stjs.javascript.annotation.SyntheticType;
import org.stjs.javascript.annotation.Template;

/**
 *
 * @author sj
 */
@SyntheticType
public class ButtonConfig extends Config {
    
    private String text;

    @Template("propertyAccess")
    public String getText() {
        return text;
    }

    @Template("propertyAccess")
    public void setText(String text) {
        this.text = text;
    }
}
