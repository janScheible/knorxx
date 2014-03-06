package knorxx.framework.generator.javaeesampleapp.server.arranger;

import knorxx.framework.generator.web.client.webpage.PageArranger;
import static knorxx.framework.generator.javaeesampleapp.client.GuiWebPage.MY_CONTENT_ID;
import static knorxx.framework.generator.javaeesampleapp.client.GuiWebPage.MY_MODEL_KEY;
import java.io.IOException;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import org.joda.time.DateTime;
import static org.rendersnake.HtmlAttributesFactory.id;
import org.rendersnake.HtmlCanvas;

/**
 *
 * @author sj
 */
@RequestScoped
public class GuiPageArranger extends PageArranger {
    
    @Override
    public void renderOn(HtmlCanvas html) throws IOException {
        html
            .h1(null).content("This is a heading... or?")
            .div(id(MY_CONTENT_ID))._div()
            .h3(null).content(":-)");
    }

    @Override
    public void initialize(Map<String, Object> model) {
        model.put(MY_MODEL_KEY, new DateTime());
    }
}
