package knorxx.framework.generator.reloading;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 *
 * @author sj
 */
public class ClassAnnotationDescription extends AnnotationDescription {

    public ClassAnnotationDescription(Class<? extends Annotation> annotationClass, Map<String, Object> values) {
        super(annotationClass, values);
    }

    public ClassAnnotationDescription(Class<? extends Annotation> annotationClass) {
        super(annotationClass);
    }
}
