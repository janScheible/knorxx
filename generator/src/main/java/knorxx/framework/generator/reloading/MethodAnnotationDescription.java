package knorxx.framework.generator.reloading;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 *
 * @author sj
 */
public class MethodAnnotationDescription extends AnnotationDescription {

    public MethodAnnotationDescription(Class<? extends Annotation> annotationClass, Map<String, Object> values) {
        super(annotationClass, values);
    }

    public MethodAnnotationDescription(Class<? extends Annotation> annotationClass) {
        super(annotationClass);
    }
}
