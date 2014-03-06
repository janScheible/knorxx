package knorxx.framework.generator.springadapter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import static knorxx.framework.generator.springadapter.KnorxxController.FRAMEWORK_URL_PREFIX;
import org.atmosphere.cpr.AtmosphereServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 *
 * @author sj
 */
public abstract class KnorxxDispatcherServletInitializer 
        extends AbstractAnnotationConfigDispatcherServletInitializer {

    public final static String ATMOSPHERE_URL = FRAMEWORK_URL_PREFIX + "/atmosphere";

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);

        ServletRegistration.Dynamic atmosphereServletRegistration = servletContext.addServlet("atmosphereServlet", 
                AtmosphereServlet.class);
        atmosphereServletRegistration.setAsyncSupported(true);
        atmosphereServletRegistration.addMapping("/" + ATMOSPHERE_URL + "/*");
        atmosphereServletRegistration.setLoadOnStartup(1);
    }
}
    

