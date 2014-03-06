package knorxx.framework.generator.web.client.webpage.annotation;

import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import org.stjs.javascript.annotation.Namespace;

/**
 * This is a special annotation which can be used to look up the URL of a WebPage sub class on the JavaScript side.
 * 
 * @author sj
 */
@Retention(RUNTIME)
@Target(TYPE)
@Namespace("knorxx.framework.generator")
public @interface WebPageUrl {
    String value();
}