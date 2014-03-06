package knorxx.framework.generator;

import com.google.common.base.Optional;
import com.google.common.io.Files;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import knorxx.framework.generator.util.JavaIdentifierUtils;
import org.joda.time.DateTime;

/**
 * References a file in the file system for which the class file as well as the source is available.
 * 
 * @author sj
 */
public class JavaFileWithSource<T> extends JavaFile<T> {

    protected final File sourceFile;
    protected final URL sourceUrl;
    private final DateTime sourceFileChangeDate;
    
    protected final File classFile;
    protected final URL classUrl;
    private final DateTime classFileChangeDate;

    public JavaFileWithSource(Class<T> javaClass, GenerationRoots generationRoots) {
        this(javaClass, JavaFileWithSource.getJavaSourceFile(javaClass.getName(), generationRoots), 
                JavaFileWithSource.getJavaClassFile(javaClass.getName(), generationRoots));
    }
    
    protected JavaFileWithSource(Class<T> javaClass, String javaClassName, GenerationRoots generationRoots) {
        this(javaClass, JavaFileWithSource.getJavaSourceFile(javaClassName, generationRoots), 
                JavaFileWithSource.getJavaClassFile(javaClassName, generationRoots));
    }

    protected JavaFileWithSource(Class<T> javaClass) {
        super(javaClass);
        
        sourceFile = null;
        sourceUrl = null;
        sourceFileChangeDate = null;
        
        classFile = null;
        classUrl = null;
        classFileChangeDate = null;
    }
    
    protected JavaFileWithSource(JavaFileWithSource<T> javaFileWithSource) {
        this(javaFileWithSource.getJavaClass(), javaFileWithSource.sourceFile, javaFileWithSource.classFile);
    }
    
    private JavaFileWithSource(Class<T> javaClass, File sourceFile, File classFile) {
        super(javaClass);

        exists(javaClass, sourceFile, "java", "source");
        this.sourceFile = sourceFile.exists() ? sourceFile : null;
        this.sourceUrl = this.sourceFile != null ? null : getUrl(javaClass, "java");
        this.sourceFileChangeDate = new DateTime(sourceFile.lastModified());

        exists(javaClass, classFile, "class", "class");
        this.classFile = classFile.exists() ? classFile : null;
        this.classUrl = this.classFile != null ? null : getUrl(javaClass, "class");
        this.classFileChangeDate = new DateTime(classFile.lastModified());
    }
    
    private boolean exists(Class<T> javaClass, File file, String extension, String type) {
        if(file == null || !file.exists()) {
            URL url = getUrl(javaClass, extension);

            if (url == null) {
                throw new IllegalStateException("Can't resolve the Java " + type + " file of '" + javaClass.getSimpleName()
                        + ((file != null) ? "' neither in the file '" + file.getAbsolutePath() + "' nor" : "")
                        + " on the classpath!");
            }
        }
        
        return true;
    }
    
    private URL getUrl(Class<T> javaClass, String extension) {
        return javaClass.getResource(javaClass.getSimpleName() + "." + extension);
    }
    
    private static File getJavaSourceFile(String javaClassName, GenerationRoots generationRoots) {
        if(JavaIdentifierUtils.isInnerClass(javaClassName)) {
            javaClassName = javaClassName.substring(0, javaClassName.indexOf("$"));
        }
        
        return new File(generationRoots.getSourceRoot() + File.separator + javaClassName.replace(".", File.separator) + ".java");
    }
   
    private static File getJavaClassFile(String javaClassName, GenerationRoots generationRoots) {
        return new File(generationRoots.getOutputRoot() + File.separator + javaClassName.replace(".", File.separator) + ".class");        
    }    

    @Override
    public Optional<InputStream> getSourceInputStream() {
        return sourceFile != null ? getInputStream(sourceFile) : getInputStream(sourceUrl);
    }

    public DateTime getSourceFileChangeDate() {
        return sourceFileChangeDate;
    }
    
    @Override
    public InputStream getClassInputStream() {
        return classFile != null ? getInputStream(classFile).get() :getInputStream(classUrl).get();
    }

    public DateTime getClassFileChangeDate() {
        return classFileChangeDate;
    }
    
    public DateTime getLastFileChangeDate() {
        return sourceFileChangeDate.isAfter(classFileChangeDate) ? sourceFileChangeDate : classFileChangeDate;
    }

    @Override
    public JavaFileWithSource getInnerClass(String innerClassSimpleName, ClassLoader classLoader) {
        File innerClassFile = new File(classFile.getParent() + File.separator + 
                Files.getNameWithoutExtension(classFile.getName()) + "$" + innerClassSimpleName + ".class");
        return new JavaFileWithSource(loadInnerClass(innerClassSimpleName, classLoader), sourceFile, innerClassFile);
    }
    
    @Override
    public String toString() {
        return getJavaClassName() + " (source: '" + sourceFile.getAbsolutePath() + "', class: '" + classFile.getAbsolutePath() + "')";
    }
}
