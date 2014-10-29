package knorxx.framework.generator.single;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Set;
import knorxx.framework.generator.JavaFileWithSource;
import knorxx.framework.generator.single.annotation.NotJavaScriptGeneratable;

/**
 *
 * @author sj
 */
public abstract class SingleFileGenerator {

    public abstract SingleResult generate(JavaFileWithSource<?> javaFile, ClassLoader classLoader, 
            Set<String> allowedPackages) throws SingleFileGeneratorException;
    
    public boolean isGeneratable(Class<?> javaClass) {
        return javaClass.getAnnotation(NotJavaScriptGeneratable.class) == null;
    }
    
    public Set<String> removeNotGeneratableJavaClasses(Class javaClass, Set<String> javaClassNames, final ClassLoader classLoader) {
        return new HashSet<>(Sets.filter(javaClassNames, new Predicate<String>() {
            @Override
            public boolean apply(String javaClassName) {
                try {
                    return isGeneratable(classLoader.loadClass(javaClassName));
                } catch (ClassNotFoundException ex) {
                    throw new IllegalStateException("The Java class with the name '" + javaClassName + "' can't be found!", ex);
                }
            }
        }));
    } 
}
