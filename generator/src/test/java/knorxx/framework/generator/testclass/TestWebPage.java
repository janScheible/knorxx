package knorxx.framework.generator.testclass;

import static org.stjs.javascript.Global.alert;
import static org.stjs.javascript.Global.window;
import org.stjs.javascript.annotation.Namespace;
import org.stjs.javascript.dom.Element;
import org.stjs.javascript.jquery.Event;
import org.stjs.javascript.jquery.EventHandler;
import static org.stjs.javascript.jquery.GlobalJQuery.$;

/**
 *
 * @author sj
 */
@Namespace("test.sample")
public class TestWebPage extends BaseWebPage {
    
    public static final String MESSAGE = "Buh!";
    
    @Override
    public void render() {
        final TestWebPage that = this;
        
        $(window).load(new EventHandler() {
            @Override
            public boolean onEvent(Event event, Element element) {
                that.onLoad();
                return true;
            }
        });
    }

    private void onLoad() {
        alert(MESSAGE);
    }
}
