package knorxx.framework.generator.springsampleapp.client.util;

import knorxx.framework.generator.springsampleapp.client.page.ChatWebPage;
import knorxx.framework.generator.springsampleapp.client.page.ExtJsWebPage;
import knorxx.framework.generator.springsampleapp.client.page.JQueryUiWebPage;
import knorxx.framework.generator.springsampleapp.client.page.ServiceWebPage;
import knorxx.framework.generator.springsampleapp.client.page.SimpleWebPage;
import knorxx.framework.generator.springsampleapp.client.page.admin.AdminWebPage;
import knorxx.framework.generator.web.client.webpage.annotation.WebPageUrl;
import static org.stjs.javascript.jquery.GlobalJQuery.$;
import org.stjs.javascript.jquery.JQueryCore;

/**
 *
 * @author sj
 */
public class MenuBuilder {

    public static void build(JQueryCore<?> element) {
        element.append($("<a></a>").text(SimpleWebPage.class.getSimpleName()).attr("href", 
                ((WebPageUrl)SimpleWebPage.class.getAnnotation(WebPageUrl.class)).value()));
        element.append($("<br></br>"));
        element.append($("<a></a>").text(ServiceWebPage.class.getSimpleName()).attr("href", 
                ((WebPageUrl)ServiceWebPage.class.getAnnotation(WebPageUrl.class)).value()));
        element.append($("<br></br>"));
        element.append($("<a></a>").text(ChatWebPage.class.getSimpleName()).attr("href", 
                ((WebPageUrl)ChatWebPage.class.getAnnotation(WebPageUrl.class)).value()));
        element.append($("<br></br>"));
        element.append($("<a></a>").text(JQueryUiWebPage.class.getSimpleName()).attr("href", 
                ((WebPageUrl)JQueryUiWebPage.class.getAnnotation(WebPageUrl.class)).value()));
        element.append($("<br></br>"));
        element.append($("<a></a>").text(ExtJsWebPage.class.getSimpleName()).attr("href", 
                ((WebPageUrl)ExtJsWebPage.class.getAnnotation(WebPageUrl.class)).value()));        
        element.append($("<br></br>"));        
        element.append($("<a></a>").text(AdminWebPage.class.getSimpleName()).attr("href", 
                ((WebPageUrl)AdminWebPage.class.getAnnotation(WebPageUrl.class)).value()));        
    }
}
