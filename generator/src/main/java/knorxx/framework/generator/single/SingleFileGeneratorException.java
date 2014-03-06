package knorxx.framework.generator.single;

import java.io.File;

/**
 *
 * @author sj
 */
public class SingleFileGeneratorException extends Exception {
    
    private final File inputFile;

    public SingleFileGeneratorException(Throwable cause, File inputFile) {
        super(cause);
        
        this.inputFile = inputFile;
    }

    public File getInputFile() {
        return inputFile;
    }
}
