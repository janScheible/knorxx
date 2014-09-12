package knorxx.framework.generator.springsampleapp.client.util;

import knorxx.framework.generator.springsampleapp.client.appearance.Appearance;
import knorxx.framework.generator.web.client.WebPage;
import static org.stjs.javascript.Global.window;
import org.stjs.javascript.dom.Element;
import org.stjs.javascript.jquery.Event;
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
        
        $(window).load((Event event, Element element) -> {
			$(CONTAINER_ID).addClass(Appearance.STYLE_SCOPE_CLASS)
				.append((Element)$("<table/>").css("border", "3px solid #42B4E6")
					.append((Element)$("<tr/>")
						.append((Element)$("<td/>")
							.append((Element)$("<div></div>").attr("id", MENU_ID.substring(1)))
							.append((Element)$("<h1></div>").attr("id", TITLE_ID.substring(1)))
							.append((Element)$("<div></div>").attr("id", CONTENT_ID.substring(1)))
						)
					)
				);
			
            MenuBuilder.build($(MENU_ID));
            that.onLoad();
            return true;
        });
    }
    
    public abstract void onLoad();
}
