package knorxx.framework.generator;

import com.google.common.base.Optional;
import com.google.common.io.ByteStreams;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import knorxx.framework.generator.util.JavaIdentifierUtils;

/**
 *
 * @author sj
 */
public abstract class JavaFile<T> {

    private final Class<T> javaClass;

    protected JavaFile(Class<T> javaClass) {
        this.javaClass = javaClass;
    }

    protected JavaFile(Class<T> javaClass, GenerationRoots generationRoots) {
        this.javaClass = javaClass;
    }

    public Class<T> getJavaClass() {
        return javaClass;
    }
    
    public String getJavaClassName() {
        return javaClass.getName();
    }
    
    public String getJavaClassSimpleName() {
        return javaClass.getSimpleName();
    }
    
    public boolean isInnerClass() {
        return JavaIdentifierUtils.isInnerClass(getJavaClassName());
    }
    
    public abstract Optional<InputStream> getSourceInputStream();
    
    public abstract InputStream getClassInputStream();
    
    public byte[] readClass() throws IOException {
        return ByteStreams.toByteArray(getClassInputStream());
    }    
    
    public abstract JavaFile getInnerClass(String innerClassSimpleName, ClassLoader classLoader);    
    
    protected Optional<InputStream> getInputStream(File file) {
        try {
            return Optional.<InputStream>of(new FileInputStream(file));
        } catch (FileNotFoundException ex) {
            // should not happen, we checked the existence in the constructor
        }

        return Optional.<InputStream>absent();
    }
    
    protected Optional<InputStream> getInputStream(URL url) {
        try {
            return Optional.<InputStream>of(url.openStream());
        } catch (IOException ex) {
            // should not happen, we checked the existence in the constructor
        }

        return Optional.<InputStream>absent();
    }    
    
    protected Class loadInnerClass(String innerClassSimpleName, ClassLoader classLoader) {
        String innerClassName = getJavaClassName() + "$" + innerClassSimpleName;
        
        try {
            Class innerClass = classLoader.loadClass(innerClassName);
            return innerClass;
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Can't load the class '" + innerClassName + "' via the given ClassLoader!", ex);
        }
    }
}
