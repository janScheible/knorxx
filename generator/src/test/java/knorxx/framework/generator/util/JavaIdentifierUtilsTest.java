package knorxx.framework.generator.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class JavaIdentifierUtilsTest {
    
    @Test
    public void getJavaClassSimpleName() {
        assertEquals(JavaIdentifierUtils.getJavaClassSimpleName("Test"), "Test");
        assertEquals(JavaIdentifierUtils.getJavaClassSimpleName("bla.Test"), "Test");
        assertEquals(JavaIdentifierUtils.getJavaClassSimpleName("bla.blub.Test"), "Test");
    }
    
    @Test
    public void isInnerClass() {
        assertTrue(JavaIdentifierUtils.isInnerClass("Bla$Test"));
        assertTrue(JavaIdentifierUtils.isInnerClass("dummy.Bla$Test$Blub"));
        assertFalse(JavaIdentifierUtils.isInnerClass("test.$"));
    }
    
    @Test
    public void isValidClassName() {
        assertTrue(JavaIdentifierUtils.isValidClassName("BlaCamelCase"));
        assertTrue(JavaIdentifierUtils.isValidClassName("haha.dummy.Bla$Test"));
        assertFalse(JavaIdentifierUtils.isValidClassName("test.$"));
        assertFalse(JavaIdentifierUtils.isValidClassName("Bla.$"));
        assertFalse(JavaIdentifierUtils.isValidClassName("test.Bla_haha"));
        assertFalse(JavaIdentifierUtils.isValidClassName("test.0Bla"));
    }
    
    @Test
    public void getJavaClassNestingLevel() {
        assertEquals(JavaIdentifierUtils.getJavaClassNestingLevel("Bla$Test"), 1);
        assertEquals(JavaIdentifierUtils.getJavaClassNestingLevel("Bla$Test$Inner"), 2);
        assertEquals(JavaIdentifierUtils.getJavaClassNestingLevel("Bla"), 0);
        assertEquals(JavaIdentifierUtils.getJavaClassNestingLevel("dummy.Bla"), 0);        
    }
    
    @Test
    public void isValidConstantName() {
        assertTrue(JavaIdentifierUtils.isValidConstantName("GO_GO_GO"));
        assertTrue(JavaIdentifierUtils.isValidConstantName("GO"));
        assertFalse(JavaIdentifierUtils.isValidConstantName("_GO"));
        assertFalse(JavaIdentifierUtils.isValidConstantName("GO-BLA"));
    }
    
    @Test
    public void getPackageName() {
        assertEquals(JavaIdentifierUtils.getPackageName("Bla"), "");
        assertEquals(JavaIdentifierUtils.getPackageName("dummy.Bla"), "dummy");
        assertEquals(JavaIdentifierUtils.getPackageName("dummy.bla.Bla"), "dummy.bla");
    }
    
    @Test
    public void isJavaCoreClass() {
        assertTrue(JavaIdentifierUtils.isJavaCoreClass("java.lang.Exception"));
        assertTrue(JavaIdentifierUtils.isJavaCoreClass("javax.lang.Exception"));
        assertFalse(JavaIdentifierUtils.isJavaCoreClass("org.dummy.Exception"));
    }
}
