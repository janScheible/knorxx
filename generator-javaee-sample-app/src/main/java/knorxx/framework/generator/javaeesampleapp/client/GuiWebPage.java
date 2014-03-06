package knorxx.framework.generator.javaeesampleapp.client;

import static knorxx.framework.generator.javaeesampleapp.client.appearance.Appearance.HEADING;
import knorxx.framework.generator.javaeesampleapp.client.util.AbstractWebPage;
import knorxx.framework.generator.javaeesampleapp.server.arranger.GuiPageArranger;
import knorxx.framework.generator.web.client.webpage.annotation.WebPageArranger;
import org.stjs.javascript.Date;
import static org.stjs.javascript.Global.alert;
import org.stjs.javascript.dom.Element;
import org.stjs.javascript.jquery.Event;
import org.stjs.javascript.jquery.EventHandler;
import static org.stjs.javascript.jquery.GlobalJQueryUI.$;
import org.stjs.javascript.jquery.plugins.ButtonOptions;

/**
 *
 * @author sj
 */
@WebPageArranger(GuiPageArranger.class)
public class GuiWebPage extends AbstractWebPage {
    
    public static final String MY_MODEL_KEY = "test";
    public static final String MY_CONTENT_ID = "myContent";
    
    @Override
    public void onLoad() {
        $(TITLE_ID).text("Gui WebPage").addClass(HEADING);
        ButtonOptions buttonOptions = new ButtonOptions();
        buttonOptions.label = ((Date)getModel().$get(MY_MODEL_KEY)).toGMTString();
        $("#" + MY_CONTENT_ID).button(buttonOptions).click(new EventHandler() {
            @Override
            public boolean onEvent(Event ev, Element THIS) {
                alert("clicked :-)");
                return true;
            }
        });
    }    
}
