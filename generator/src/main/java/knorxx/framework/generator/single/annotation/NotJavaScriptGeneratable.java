package knorxx.framework.generator.single.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author sj
 */
@Target({ ElementType.TYPE , ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotJavaScriptGeneratable {    
}
