package knorxx.framework.generator.springsampleapp.client.page;

import knorxx.framework.generator.springsampleapp.client.appearance.Appearance;
import knorxx.framework.generator.springsampleapp.client.util.AbstractWebPage;
import org.springframework.stereotype.Component;
import static org.stjs.javascript.jquery.GlobalJQuery.$;
import org.stjs.javascript.jquery.JQueryCore;

/**
 * @author sj
 */
@Component
public class SimpleWebPage extends AbstractWebPage {
    
    public final static String HEADING = "Simple WebPage";

    @Override
    public void onLoad() {
        $(TITLE_ID).text(HEADING).addClass(Appearance.HEADING_STYLE);
        $(CONTENT_ID).append($("<div></div>").text("JavaScript... tss... ;-)").addClass(Appearance.HIGHLIGHT_STYLE));
        
        $(CONTENT_ID).append($("<h2>TODOs</h2>"));

        JQueryCore list = $("<ul></ul>");        
        list.append("<li>SourceMaps???</li>");
        list.append("<li>Support of promises??? (e.g. https://github.com/kriskowal/q)</li>");        
         
        $(CONTENT_ID).append(list);
    }
}
