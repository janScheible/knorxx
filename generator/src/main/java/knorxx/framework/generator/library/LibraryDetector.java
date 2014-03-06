package knorxx.framework.generator.library;

import com.google.common.base.Optional;
import java.util.Set;

/**
 *
 * @author sj
 */
public abstract class LibraryDetector {
    
    private Optional<LibraryDetector> nextDetector = Optional.absent();
    

    public LibraryDetector() {
    }

    public LibraryDetector(LibraryDetector nextDetector) {
        this.nextDetector = Optional.of(nextDetector);
    }
        
    
    public final LibraryUrls detect(Set<String> javaClassNames) {
        LibraryUrls libraryUrls = detectInternal(javaClassNames);

        if (nextDetector.isPresent()) {
            libraryUrls.addAll(nextDetector.get().detect(javaClassNames));
        }
        
        return libraryUrls;
    }
    
    protected abstract LibraryUrls detectInternal(Set<String> javaClassNames);
    
    public static class None extends LibraryDetector {

        @Override
        protected LibraryUrls detectInternal(Set<String> javaClassNames) {
            return new LibraryUrls();
        }
    }
}
