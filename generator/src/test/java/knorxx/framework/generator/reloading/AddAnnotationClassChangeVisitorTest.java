package knorxx.framework.generator.reloading;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import knorxx.framework.generator.BaseGeneratorTest;
import knorxx.framework.generator.JavaFileOnClasspath;
import knorxx.framework.generator.reloading.testclass.AnnotatedClass;
import knorxx.framework.generator.reloading.testclass.AnnotationTestClass;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class AddAnnotationClassChangeVisitorTest extends BaseGeneratorTest {
    
    @Test
    public void unchangedExistingAnnotation() throws IOException, ClassNotFoundException {
        Class classWithAnnotation = AnnotatedClass.class;
        Class unchangedClass = addAnnotations(classWithAnnotation, list());
        
        assertTrue(classWithAnnotation.getAnnotation(Deprecated.class) != null);
        assertTrue(unchangedClass.getAnnotation(Deprecated.class) != null);
    }
    
    @Test
    public void readdedExistingAnnotation() throws IOException, ClassNotFoundException {
        Class classWithAnnotation = AnnotatedClass.class;
        Class classWithSameAnnotation = addAnnotations(classWithAnnotation, 
                list(new ClassAnnotationDescription(Deprecated.class)));
        
        assertTrue(classWithAnnotation.getAnnotation(Deprecated.class) != null);
        assertTrue(classWithSameAnnotation.getAnnotation(Deprecated.class) != null);
    }    
    
    @Test
    public void addClassAnnotation() throws IOException, ClassNotFoundException {
        Class classWithoutAnnotation = AnnotationTestClass.class;
        Class classWithAnnotation = addAnnotations(classWithoutAnnotation, 
                list(new ClassAnnotationDescription(Deprecated.class)));
        
        assertTrue(classWithoutAnnotation.getAnnotation(Deprecated.class) == null);
        assertTrue(classWithAnnotation.getAnnotation(Deprecated.class) != null);
    }
    
    @Test
    public void addConditionalMethodAnnotation() throws ClassNotFoundException, IOException, NoSuchMethodException {
        MethodAnnotationDescription conditionalMethodAnnotationDescription = new MethodAnnotationDescription(Deprecated.class) {
            @Override
            public boolean isApplicable(Class<?> javaClass, String memberName) {
                return memberName.startsWith("get") || memberName.startsWith("set");
            }
        };
        
        Class classWithoutAnnotation = AnnotationTestClass.class;
        Class classWithMethodAnnotation = addAnnotations(classWithoutAnnotation, list(conditionalMethodAnnotationDescription));
        
        assertTrue(classWithMethodAnnotation.getAnnotation(Deprecated.class) == null);
        
        assertTrue(classWithMethodAnnotation.getDeclaredMethod("make").getAnnotation(Deprecated.class) == null);
        assertTrue(classWithMethodAnnotation.getDeclaredMethod("getBla").getAnnotation(Deprecated.class) != null);
    }
    
    private Class addAnnotations(Class javaClass, List<AnnotationDescription> annotationDescriptions) throws ClassNotFoundException, IOException {
        ClassChangeVisitor classChangeVisitor = new AddAnnotationClassChangeVisitor(javaClass, annotationDescriptions);
        Class classWithAnnotation = defineClass(javaClass.getName(), 
                classChangeVisitor.apply(new JavaFileOnClasspath<>(javaClass).readClass()));
        
        return classWithAnnotation;
    }
    
    private List<AnnotationDescription> list(AnnotationDescription... annotationDescriptions) {
        List<AnnotationDescription> result = new ArrayList<>();
        result.addAll(Arrays.asList(annotationDescriptions));        
        return result;
    }
}
