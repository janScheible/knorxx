package knorxx.framework.generator.javaeesampleapp.client.util;

import knorxx.framework.generator.web.client.WebPage;
import static org.stjs.javascript.Global.window;
import org.stjs.javascript.dom.Element;
import org.stjs.javascript.jquery.Event;
import org.stjs.javascript.jquery.EventHandler;
import static org.stjs.javascript.jquery.GlobalJQuery.$;

/**
 *
 * @author sj
 */
public abstract class AbstractWebPage extends WebPage {

	public static final String MENU_ID = "#menu";
    public static final String TITLE_ID = "#title";
    public static final String CONTENT_ID = "#content";
	
    @Override
    public void render() {
        final AbstractWebPage that = this;
        
        $(window).load(new EventHandler() {
            @Override
            public boolean onEvent(Event event, Element element) {
                MenuBuilder.build($(MENU_ID));
                that.onLoad();
                return true;
            }
        });
    }
    
    public abstract void onLoad();
}
