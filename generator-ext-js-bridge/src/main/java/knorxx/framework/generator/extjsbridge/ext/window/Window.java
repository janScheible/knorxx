package knorxx.framework.generator.extjsbridge.ext.window;

import knorxx.framework.generator.extjsbridge.ext.Component;
import org.stjs.javascript.Array;
import org.stjs.javascript.annotation.Namespace;
import org.stjs.javascript.annotation.STJSBridge;

/**
 *
 * @author sj
 */
@STJSBridge
@Namespace("Ext.window")
public class Window extends Component {

    public Window(WindowConfig config) {
    }

    public void show() {
    }
    
    public Component add(Component component) {
        return null;
    }
    
    public Array<Component> add(Array<Component> component) {
        return null;
    }
}
