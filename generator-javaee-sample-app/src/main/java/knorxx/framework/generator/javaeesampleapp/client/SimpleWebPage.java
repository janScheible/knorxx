package knorxx.framework.generator.javaeesampleapp.client;

import static knorxx.framework.generator.javaeesampleapp.client.appearance.Appearance.HEADING;
import static knorxx.framework.generator.javaeesampleapp.client.appearance.Appearance.HIGHLIGHT;
import knorxx.framework.generator.javaeesampleapp.client.util.AbstractWebPage;
import static org.stjs.javascript.jquery.GlobalJQuery.$;
import org.stjs.javascript.jquery.JQueryCore;

/**
 * @author sj
 */
public class SimpleWebPage extends AbstractWebPage {

    @Override
    public void onLoad() {
        $(TITLE_ID).text("Simple WebPage").addClass(HEADING);
        $(CONTENT_ID).append($("<div></div>").text("JavaScript... tss... ;-)").addClass(HIGHLIGHT));
        
        $(CONTENT_ID).append($("<h2>TODOs</h2>"));
                
        JQueryCore list = $("<ul></ul>");        
        list.append("<li>Exception handling</li>");
        list.append("<li>TomEE Maven plugin support</li>");
        list.append("<li>SourceMaps</li>");
        list.append("<li>ExtJS Usage</li>");
        list.append("<li>Auto add @Template(\"toProperty\") and @SyntheticType to model classes</li>");
        $(CONTENT_ID).append(list);
    }
}
