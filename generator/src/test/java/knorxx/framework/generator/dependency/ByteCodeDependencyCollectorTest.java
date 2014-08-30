package knorxx.framework.generator.dependency;

import com.google.common.collect.Sets;
import java.util.Set;
import knorxx.framework.generator.BaseGeneratorTestWithMultipleClassLoader;
import knorxx.framework.generator.JavaFileOnClasspath;
import knorxx.framework.generator.dependency.testclass.bytecode.AnonymousInnerClass;
import knorxx.framework.generator.dependency.testclass.bytecode.AnonymousInnerClassWithStaticHelperInSamePackage;
import knorxx.framework.generator.dependency.testclass.bytecode.DependencyWithInnerClass;
import knorxx.framework.generator.dependency.testclass.bytecode.Enumeration;
import knorxx.framework.generator.dependency.testclass.bytecode.InnerClass;
import knorxx.framework.generator.dependency.testclass.bytecode.NestedInnerClasses;
import knorxx.framework.generator.dependency.testclass.bytecode.NestedStaticInnerClasses;
import knorxx.framework.generator.dependency.testclass.bytecode.Simple;
import knorxx.framework.generator.dependency.testclass.bytecode.StaticHelperClass;
import knorxx.framework.generator.dependency.testclass.bytecode.StaticInnerClass;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class ByteCodeDependencyCollectorTest extends BaseGeneratorTestWithMultipleClassLoader {

    public ByteCodeDependencyCollectorTest(ClassLoader currentClassLoader) {
        super(currentClassLoader);
    }

    @Test
    public void simple() {
        test(Simple.class, Sets.newHashSet("java.awt.Color"));
    }

    @Test
    public void innerClass() {
        test(InnerClass.class, Sets.newHashSet("java.awt.Color", "java.awt.Rectangle"));
    }

    @Test
    public void staticInnerClass() {
        test(StaticInnerClass.class, Sets.newHashSet("java.awt.Color", "java.awt.Rectangle"));
    }

    @Test
    public void nestedStaticInnerClasses() {
        test(NestedStaticInnerClasses.class, Sets.newHashSet("java.awt.Color", "java.awt.Rectangle",
                "java.awt.Point"));
    }

    @Test
    public void nestedInnerClasses() {
        test(NestedInnerClasses.class, Sets.newHashSet("java.awt.Color", "java.awt.Rectangle",
                "java.awt.Point"));
    }

    @Test
    public void anonymousInnerClass() {
        test(AnonymousInnerClass.class, Sets.newHashSet("java.awt.Rectangle"));
    }

    @Test
    public void anonymousInnerClassWithStaticHelperInSamePackage() {
        test(AnonymousInnerClassWithStaticHelperInSamePackage.class, Sets.newHashSet(StaticHelperClass.class.getName()));
    }

    @Test
    public void dependencyWithInnerClass() {
        test(DependencyWithInnerClass.class, Sets.newHashSet("com.projetloki.genesis.Properties"));
    }
	
	@Test
    public void enumeration() {
		test(Enumeration.class, Sets.newHashSet("java.net.StandardProtocolFamily"));
	}

    private <T> void test(Class<T> javaClass, Set<String> expectedDependencies) {
        ByteCodeDependencyCollector dependencyCollector = new ByteCodeDependencyCollector();
        Set<String> dependencies = dependencyCollector.collect(new JavaFileOnClasspath<>(javaClass), getCurrentClassLoader());

        assertThat(dependencies, hasItems(expectedDependencies.toArray(new String[]{})));
    }
}
