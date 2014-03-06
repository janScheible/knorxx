package knorxx.framework.generator.javaeesampleapp.client.admin;

import static knorxx.framework.generator.javaeesampleapp.client.appearance.Appearance.HEADING;
import static knorxx.framework.generator.javaeesampleapp.client.appearance.Appearance.HIGHLIGHT;
import knorxx.framework.generator.javaeesampleapp.client.util.AbstractWebPage;
import static org.stjs.javascript.jquery.GlobalJQuery.$;

/**
 *
 * @author sj
 */
public class AdminWebPage extends AbstractWebPage {

    @Override
    public void onLoad() {
        $(TITLE_ID).text("Admin WebPage").addClass(HEADING);
        $(CONTENT_ID).append($("<div></div>").text("Nothing special here... just demonstrating a page in a sub directory. :-)").addClass(HIGHLIGHT));
    }    
}
