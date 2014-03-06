package knorxx.framework.generator.springsampleapp.client.page.admin;

import static knorxx.framework.generator.springsampleapp.client.appearance.Appearance.HEADING_STYLE;
import static knorxx.framework.generator.springsampleapp.client.appearance.Appearance.HIGHLIGHT_STYLE;
import knorxx.framework.generator.springsampleapp.client.util.AbstractWebPage;
import org.springframework.stereotype.Controller;
import static org.stjs.javascript.jquery.GlobalJQuery.$;

/**
 *
 * @author sj
 */
@Controller
public class AdminWebPage extends AbstractWebPage {

    @Override
    public void onLoad() {
        $(TITLE_ID).text("Admin WebPage").addClass(HEADING_STYLE);
        $(CONTENT_ID).append($("<div></div>").text("Nothing special here... just demonstrating a page in a sub directory. :-)").addClass(HIGHLIGHT_STYLE));
    }    
}
