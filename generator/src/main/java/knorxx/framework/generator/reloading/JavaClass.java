package knorxx.framework.generator.reloading;

/**
 *
 * @author sj
 */
/* package */ class JavaClass {
    
    private final Class javaClass;
    private final byte[] classData;

    public JavaClass(Class javaClass) {
        this(javaClass, null);
    }
    
    public JavaClass(Class javaClass, byte[] classData) {
        this.javaClass = javaClass;
        this.classData = classData;
    }

    public byte[] getClassData() {
        return classData;
    }
    
    public boolean hasClassData() {
        return classData != null;
    }

    public Class getJavaClass() {
        return javaClass;
    }
}
