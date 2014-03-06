package knorxx.framework.generator;

import com.google.common.base.Optional;
import java.io.InputStream;

/**
 * References a file on the classpath with no source available.
 * 
 * @author sj
 */
public class JavaFileOnClasspath<T> extends JavaFile<T> {

    public JavaFileOnClasspath(Class<T> javaClass) {
        super(javaClass);
    }

    @Override
    public Optional<InputStream> getSourceInputStream() {
        throw new UnsupportedOperationException();
    }

    @Override
    public InputStream getClassInputStream() {
        String classFileName = getJavaClassName().substring(getJavaClassName().lastIndexOf(".") + 1);
        return getJavaClass().getResourceAsStream(classFileName + ".class");
    }

    @Override
    public JavaFile getInnerClass(String innerClassSimpleName, ClassLoader classLoader) {
        return new JavaFileOnClasspath(loadInnerClass(innerClassSimpleName, classLoader));
    }

    @Override
    public String toString() {
        return getJavaClassName();
    }
}
