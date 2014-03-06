package knorxx.framework.generator.reloading;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author sj
 */
public abstract class AnnotationDescription {
    
    private final String annotationClassName;
    private final String annotationClassDesc;

    private final Class<? extends Annotation> annotationClass;
    private final Map<String, Object> values;
    
    public AnnotationDescription(Class<? extends Annotation> annotationClass) {
        this(annotationClass, new HashMap<String, Object>());
    }

    public AnnotationDescription(Class<? extends Annotation> annotationClass, Map<String, Object> values) {
        this.annotationClassName = annotationClass.getName();
        this.annotationClassDesc = "L" + annotationClassName.replace(".", "/") + ";";

        this.annotationClass = annotationClass;
        this.values = values;
    }
    
    public boolean isApplicable(Class<?> javaClass, String memberName) {
        return true;
    }
    
    public Map<String, Object> getValues(Class<?> javaClass, String memberName) {
        return values;
    }

    public Class<? extends Annotation> getAnnotationClass() {
        return annotationClass;
    }

    public String getAnnotationClassDesc() {
        return annotationClassDesc;
    }

    @Override
    public String toString() {
        return annotationClassName;
    }

    @Override
    public int hashCode() {
        return annotationClassName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof AnnotationDescription) {
            return this.annotationClassName.equals(((AnnotationDescription) obj).annotationClassName);
        } else {
            return false;
        }
    }
}

