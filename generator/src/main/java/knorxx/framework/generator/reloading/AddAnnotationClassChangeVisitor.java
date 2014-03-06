package knorxx.framework.generator.reloading;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

/**
 *
 * @author sj
 */
public class AddAnnotationClassChangeVisitor extends ClassChangeVisitor {

    final List<AnnotationDescription> annotationDescriptions;
    private Set<AnnotationDescription> presentClassAnnotations = new HashSet<>();
    private final Class<?> javaClass;

    public AddAnnotationClassChangeVisitor(Class<?> javaClass, List<AnnotationDescription> annotationDescriptions) {
        super(new ClassWriter(0));
        
        this.javaClass = javaClass;
        this.annotationDescriptions = annotationDescriptions;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
        
        for (AnnotationDescription description : annotationDescriptions) {
            if (description instanceof MethodAnnotationDescription && description.isApplicable(javaClass, name)) {
                addAnnotation(methodVisitor, (MethodAnnotationDescription) description, description.getValues(javaClass, name));
            }
        }

        return methodVisitor;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, final boolean visible) {
        for (AnnotationDescription description : annotationDescriptions) {
            if (description.getAnnotationClassDesc().equals(desc)) {
                presentClassAnnotations.add(description);
                break;
            }
        }
        
        return super.visitAnnotation(desc, visible);
    }

    @Override
    public void visitEnd() {
        for (AnnotationDescription description : annotationDescriptions) {
            if (description instanceof ClassAnnotationDescription && description.isApplicable(javaClass, javaClass.getName()) &&
                    !presentClassAnnotations.contains(description)) {
                addAnnotation(cv, (ClassAnnotationDescription) description, description.getValues(javaClass, javaClass.getName()));
            }
        }
        
        super.visitEnd();
    }

    private void addAnnotation(ClassVisitor visitor, ClassAnnotationDescription description, Map<String, Object> values) {
        addAnnotation(visitor.visitAnnotation(description.getAnnotationClassDesc(), true), values);
    }

    private void addAnnotation(MethodVisitor visitor, MethodAnnotationDescription description, Map<String, Object> values) {
        addAnnotation(visitor.visitAnnotation(description.getAnnotationClassDesc(), true), values);
    }

    private void addAnnotation(AnnotationVisitor annotationVisitor, Map<String, Object> values) {
        for (String key : values.keySet()) {
            annotationVisitor.visit(key, values.get(key));
        }
        
        annotationVisitor.visitEnd();
    }
}
