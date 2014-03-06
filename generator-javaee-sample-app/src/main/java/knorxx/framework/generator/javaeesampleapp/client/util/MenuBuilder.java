package knorxx.framework.generator.javaeesampleapp.client.util;

import knorxx.framework.generator.web.client.webpage.annotation.WebPageUrl;
import knorxx.framework.generator.javaeesampleapp.client.ChatWebPage;
import knorxx.framework.generator.javaeesampleapp.client.GuiWebPage;
import knorxx.framework.generator.javaeesampleapp.client.ServiceWebPage;
import knorxx.framework.generator.javaeesampleapp.client.SimpleWebPage;
import knorxx.framework.generator.javaeesampleapp.client.admin.AdminWebPage;
import static org.stjs.javascript.jquery.GlobalJQuery.$;
import org.stjs.javascript.jquery.JQueryCore;

/**
 *
 * @author sj
 */
public class MenuBuilder {

    public static void build(JQueryCore<?> element) {
        SimpleWebPage.class.getAnnotation(WebPageUrl.class);
        element.append($("<a></a>").text(SimpleWebPage.class.getSimpleName()).attr("href", 
                ((WebPageUrl)SimpleWebPage.class.getAnnotation(WebPageUrl.class)).value()));
        element.append($("<br></br>"));
        element.append($("<a></a>").text(ServiceWebPage.class.getSimpleName()).attr("href", 
                ((WebPageUrl)ServiceWebPage.class.getAnnotation(WebPageUrl.class)).value()));
        element.append($("<br></br>"));
        element.append($("<a></a>").text(ChatWebPage.class.getSimpleName()).attr("href", 
                ((WebPageUrl)ChatWebPage.class.getAnnotation(WebPageUrl.class)).value()));
        element.append($("<br></br>"));
        element.append($("<a></a>").text(GuiWebPage.class.getSimpleName()).attr("href", 
                ((WebPageUrl)GuiWebPage.class.getAnnotation(WebPageUrl.class)).value()));
        element.append($("<br></br>"));        
        element.append($("<a></a>").text(AdminWebPage.class.getSimpleName()).attr("href", 
                ((WebPageUrl)AdminWebPage.class.getAnnotation(WebPageUrl.class)).value()));        
    }
}
