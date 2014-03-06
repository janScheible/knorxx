package knorxx.framework.generator;

import com.google.common.base.Optional;
import knorxx.framework.generator.dependency.DependencyCollector;
import knorxx.framework.generator.library.LibraryDetector;
import knorxx.framework.generator.order.OrderSorter;
import knorxx.framework.generator.single.SingleFileGenerator;
import knorxx.framework.generator.single.StjsSingleFileGenerator;
import knorxx.framework.generator.testclass.BaseWebPage;
import knorxx.framework.generator.testclass.TestWebPage;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.stjs.javascript.annotation.Namespace;

/**
 *
 * @author sj
 */
public class JavaScriptGeneratorTest extends BaseGeneratorTestWithMultipleClassLoader {

    private ClassLoader classLoader;
    private SingleFileGenerator singleFileGenerator;
    private GenerationRoots generationRoots;
    private JavaScriptGenerator generator;

    @Before
    public void setUp() {
        classLoader = getCurrentClassLoader();
        singleFileGenerator = new StjsSingleFileGenerator();
        generationRoots = getTestGenerationRoots();

        generator = new JavaScriptGenerator();
    }

    public JavaScriptGeneratorTest(ClassLoader currentClassLoader) {
        super(currentClassLoader);
    }

    @Test
    public void single() {
        GenerationUnit unit = generator.generate(TestWebPage.class,
                singleFileGenerator, Optional.<DependencyCollector>absent(), Optional.of(classLoader), 
                Optional.<OrderSorter>absent(), Optional.<LibraryDetector>absent(), generationRoots);

        assertTrue(unit.getMissingDependencies().contains(BaseWebPage.class.getName()));
        assertTrue(unit.visitedClassesContains(TestWebPage.class.getName()));

        String javaScript = unit.getGenerationResult(TestWebPage.class.getName()).get().getSingleResult().toString();
        assertThat(javaScript, containsString(
                TestWebPage.class.getAnnotation(Namespace.class).value() + "." + TestWebPage.class.getSimpleName())); 
        assertThat(javaScript, containsString("\"" + TestWebPage.MESSAGE + "\""));
    }

    @Test
    public void all() {
        GenerationUnit unit = generator.generateAll(TestWebPage.class,
                singleFileGenerator, Optional.<DependencyCollector>absent(), Optional.of(classLoader), 
                Optional.<OrderSorter>absent(), Optional.<LibraryDetector>absent(), generationRoots);

        assertTrue(unit.visitedClassesContains(TestWebPage.class.getName()));
        assertTrue(unit.visitedClassesContains(BaseWebPage.class.getName()));
        
        assertTrue(unit.isMissingDependenciesEmpty());

        assertEquals(unit.getGenerationResults().size(), 2);
    }
}
