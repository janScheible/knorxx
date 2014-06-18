package knorxx.framework.generator.web.client.css.annotation;

import java.lang.annotation.ElementType;
import static java.lang.annotation.ElementType.FIELD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 *
 * @author sj
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface NoRule {
	
}
