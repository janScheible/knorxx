package knorxx.framework.generator.reloading;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

/**
 *
 * @author sj
 */
public abstract class ClassChangeVisitor extends ClassAdapter {

    private final ClassWriter classWriter;

    public ClassChangeVisitor(ClassWriter classWriter) {
        super(classWriter);
        this.classWriter = classWriter;
    }

    public byte[] toByteArray() {
        return classWriter.toByteArray();
    }
    
    public byte[] apply(byte[] classData) {
        ClassReader classReader = new ClassReader(classData);
        classReader.accept(this, 0);  
        return toByteArray();
    }
}
