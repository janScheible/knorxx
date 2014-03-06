package knorxx.framework.generator.springsampleapp;

import knorxx.framework.generator.extjsbridge.ExtJsDetector;
import knorxx.framework.generator.jqueryuibridge.JQueryUiDetector;
import knorxx.framework.generator.springadapter.GenerationRootsRequestFunction;
import knorxx.framework.generator.springadapter.KnorxxController;
import knorxx.framework.generator.springsampleapp.client.JavaScriptGenerationRoot;
import knorxx.framework.generator.springsampleapp.client.page.SimpleWebPage;
import knorxx.framework.generator.web.server.rpc.VerboseExceptionMarshaller;
import org.springframework.stereotype.Controller;

/**
 *
 * @author sj
 */
@Controller
public class SpringSampleAppController extends KnorxxController {
    
    public SpringSampleAppController() {
        super(SpringSampleAppConfig.class, JavaScriptGenerationRoot.class, SimpleWebPage.class, 
                new GenerationRootsRequestFunction("/WEB-INF/javaScriptGeneration.properties"),
                new JQueryUiDetector(new ExtJsDetector()), new VerboseExceptionMarshaller());
    }
}
