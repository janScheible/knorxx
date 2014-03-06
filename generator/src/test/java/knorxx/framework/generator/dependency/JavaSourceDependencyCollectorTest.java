package knorxx.framework.generator.dependency;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import java.util.Set;
import knorxx.framework.generator.BaseGeneratorTestWithMultipleClassLoader;
import knorxx.framework.generator.MemorySourceJavaFile;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class JavaSourceDependencyCollectorTest extends BaseGeneratorTestWithMultipleClassLoader {

    public JavaSourceDependencyCollectorTest(ClassLoader currentClassLoader) {
        super(currentClassLoader);
    }

    @Test
    public void otherPackageExplicitImport() {
        test("dummy", Optional.of("java.awt.BorderLayout"), "BorderLayout.EAST", false,
                Sets.newHashSet("java.awt.BorderLayout"));
    }

    @Test
    public void samePackageImplicitImport() {
        test("java.awt", Optional.<String>absent(), "CardLayout.EAST", false,
                Sets.newHashSet("java.awt.CardLayout"));
    }
    
    @Test
    public void samePackageImplicitImportWithStaticInnerClass() {
        test("java.awt", Optional.<String>absent(), "CardLayout.EAST", true,
                Sets.newHashSet("java.awt.CardLayout"));
    }    

    @Test
    public void fullNameReference() {
        test("dummy", Optional.<String>absent(), "java.awt.CardLayout.EAST", false,
                Sets.newHashSet("java.awt.CardLayout"));
    }
    
    @Test
    public void staticImport() {
        test("dummy", Optional.of("static java.awt.BorderLayout.*"), "EAST", false,
                Sets.newHashSet("java.awt.BorderLayout"));
    }
    
    @Test
    public void staticImportWithStaticInnerClass() {
        test("dummy", Optional.of("static java.awt.BorderLayout.*"), "EAST", true,
                Sets.newHashSet("java.awt.BorderLayout"));
    }    
    
    @Test
    public void asteriskImport() {
        test("dummy", Optional.of("java.awt.*"), "BorderLayout.EAST", false,
                Sets.newHashSet("java.awt.BorderLayout"));
    }
    
    private void test(final String packageName, final Optional<String> importedClass,
            final String variableReference, boolean variableReferenceInStaticInnerClass, Set<String> expectedDependencies) {
        JavaSourceDependencyCollector dependencyCollector = new JavaSourceDependencyCollector();
        
        String javaClassStart = 
                "package " + packageName + ";\n" +
                (importedClass.isPresent() ? "import " + importedClass.get() + ";\n" : "") +
                "public class Dummy {\n";
        
        String javaMethod = 
                "    public void test() {\n" +
                "        System.out.println(" + variableReference + ");\n" + 
                "    }\n";
        
        String javaClassEnd = 
                "}";
        
        String javaSource = javaClassStart + 
                (variableReferenceInStaticInnerClass ? "public static class InnerDummy {\n" : "") +
                javaMethod + 
                (variableReferenceInStaticInnerClass ? "}\n" : "") +
                javaClassEnd;
        
        Set<String> dependencies = dependencyCollector.collect(new MemorySourceJavaFile(javaSource), getCurrentClassLoader());

        assertEquals(expectedDependencies, dependencies);
    }
}
