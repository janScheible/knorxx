package knorxx.framework.generator.springsampleapp.server.arranger;

import java.io.IOException;
import java.util.Map;
import static knorxx.framework.generator.springsampleapp.client.page.JQueryUiWebPage.MY_CONTENT_ID;
import static knorxx.framework.generator.springsampleapp.client.page.JQueryUiWebPage.MY_MODEL_KEY;
import knorxx.framework.generator.web.client.webpage.PageArranger;
import org.joda.time.DateTime;
import static org.rendersnake.HtmlAttributesFactory.id;
import org.rendersnake.HtmlCanvas;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author sj
 */
@Component
@Scope("request") 
public class JQueryUiPageArranger extends PageArranger {
    
    @Override
    public void initialize(Map<String, Object> model) {
        model.put(MY_MODEL_KEY, new DateTime());
    }
    
    @Override
    public void renderOn(HtmlCanvas html) throws IOException {
        html
            .h1(null).content("This is a heading... or?")
            .div(id(MY_CONTENT_ID))._div()
            .h3(null).content(":-)");
    }
}
