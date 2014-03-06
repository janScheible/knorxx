package knorxx.framework.generator;

import com.google.common.base.Optional;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Sub class for testing purposes only. This Java file is not backed by a real class file but by a String instead.
 * 
 * @author sj
 */
public class MemorySourceJavaFile extends JavaFileWithSource<Void> {

    private final String javaSource;

    public MemorySourceJavaFile(String javaSource) {
        super(Void.class);
        this.javaSource = javaSource;
    }

    @Override
    public Optional<InputStream> getSourceInputStream() {
        try {
            return Optional.<InputStream>of(new ByteArrayInputStream(javaSource.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public InputStream getClassInputStream() {
        throw new UnsupportedOperationException();
    }

    @Override
    public JavaFileWithSource getInnerClass(String innerClassSimpleName, ClassLoader classLoader) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return javaSource;
    }
}
