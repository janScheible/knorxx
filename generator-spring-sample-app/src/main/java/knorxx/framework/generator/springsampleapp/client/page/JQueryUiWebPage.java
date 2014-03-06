package knorxx.framework.generator.springsampleapp.client.page;

import static knorxx.framework.generator.springsampleapp.client.appearance.Appearance.HEADING_STYLE;
import knorxx.framework.generator.springsampleapp.client.util.AbstractWebPage;
import knorxx.framework.generator.springsampleapp.server.arranger.JQueryUiPageArranger;
import knorxx.framework.generator.web.client.webpage.annotation.WebPageArranger;
import org.springframework.stereotype.Component;
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
@Component
@WebPageArranger(JQueryUiPageArranger.class)
public class JQueryUiWebPage extends AbstractWebPage {
    
    public static final String MY_MODEL_KEY = "test";
    public static final String MY_CONTENT_ID = "myContent";
    
    @Override
    public void onLoad() {
        $(TITLE_ID).text("JQuery UI WebPage").addClass(HEADING_STYLE);
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
