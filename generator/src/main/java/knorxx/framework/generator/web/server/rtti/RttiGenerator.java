package knorxx.framework.generator.web.server.rtti;

import java.util.List;
import knorxx.framework.generator.single.JavaScriptResult;
import knorxx.framework.generator.web.client.webpage.annotation.WebPageUrl;
import knorxx.framework.generator.web.generator.JavaScriptBuilder;
import org.joda.time.DateTime;

/**
 *
 * @author sj
 */
public class RttiGenerator {
    
    public RttiGenerationResult getJavaScriptSource(String name, List<Class<?>> javaClasses, UrlResolver urlResolver, DateTime generationDateTime) {
        JavaScriptBuilder builder = new JavaScriptBuilder()
                .namespace(WebPageUrl.class)
                .constructor(WebPageUrl.class)
                ._constructor();
        
        for(Class<?> javaClass : javaClasses) {
            builder
                .namespace(javaClass)
                .code(javaClass.getName()).code(" = ").code(javaClass.getName()).code(" || {}").semicolon().newLine()
                .staticFunction(javaClass, "getName")
                    .code("return ").literal(javaClass.getName()).semicolon().newLine()
                ._function()
                .staticFunction(javaClass, "getSimpleName")
                    .code("return ").literal(javaClass.getSimpleName()).semicolon().newLine()
                ._function()
                .staticFunction(javaClass, "getAnnotation")
                    .code("return { ").newLine()
                        .property("value").anonymousFunction()
                            .code("return ").literal(urlResolver.resolveWebPage(javaClass.getName())).semicolon().newLine()
                        ._function()
                    .code("};").newLine()
                ._function();
        }
        
        return new RttiGenerationResult(name, generationDateTime, new JavaScriptResult(builder.create()));
    }
}
