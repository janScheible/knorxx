package knorxx.framework.generator;

import com.google.common.base.Optional;
import java.util.ArrayList;
import knorxx.framework.generator.reloading.ReloadPredicate;
import knorxx.framework.generator.reloading.ReloadingClassLoader;

/**
 *
 * @author sj
 */

public abstract class BaseGeneratorTest {

    /* package */ ClassLoader getTestClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
    
    /* package */ ClassLoader getReloadingTestClassLoader() {
        return new ReloadingClassLoader(getTestGenerationRoots(), getTestClassLoader(), new ArrayList(),
                Optional.of(new ReloadPredicate.AllowedPackage(getClass().getPackage().getName())));
    }
    
    protected ClassLoader getCurrentClassLoader() {
        return getTestClassLoader();
    }
    
    protected GenerationRoots getTestGenerationRoots() {
        return GenerationRoots.Simple.createTestJava();
    }
    
    protected Class defineClass(final String name, final byte[] data) throws ClassNotFoundException {
        ClassLoader classLoader = new ClassLoader(this.getClass().getClassLoader()) {
            @Override
            public Class<?> loadClass(String otherName) throws ClassNotFoundException {
                if (otherName.equals(name)) {
                    return defineClass(name, data, 0, data.length);
                } else {
                    return super.loadClass(otherName);
                }
            }
        };
        
        return classLoader.loadClass(name);
    }     
}
