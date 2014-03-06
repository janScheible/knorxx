package knorxx.framework.generator.springadapter.atmosphere;

import java.util.Enumeration;
import javax.servlet.ServletContext;
import org.atmosphere.di.ServletContextHolder;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author sj
 */
public class WebApplicationContextRetriever {
    
    public WebApplicationContext getWebApplicationContext() {
        ServletContext servletContext = ServletContextHolder.getServletContext();
        WebApplicationContext webApplicationContext = null;
        
        for(Enumeration<String> enumeration = servletContext.getAttributeNames(); enumeration.hasMoreElements(); ) {
            String attributeName = enumeration.nextElement();
            Object attributeValue = servletContext.getAttribute(attributeName);
            if(attributeValue instanceof WebApplicationContext) {
                if(webApplicationContext == null) {
                    webApplicationContext = (WebApplicationContext) attributeValue;
                } else {
                    throw new IllegalStateException("Found multiple instances of WebApplicationContext and was not able to choose one!");
                }
            }
        }
        
        if(webApplicationContext != null) {
            return webApplicationContext;
        } else {
            throw new IllegalStateException("No instance of WebApplicationContext was found in the servlet context!");
        }
    }
}
