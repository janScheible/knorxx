package knorxx.framework.generator.web.client.css.annotation;

import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 *
 * @author sj
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface StyleScopeClass {
	String value();
}
