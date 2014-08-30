package knorxx.framework.generator.library;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author sj
 */
public class LibraryUrls {
    
    private final Set<String> javaScriptUrls = new HashSet<>();
    private final Set<String> cssUrls = new HashSet<>();

    public Set<String> getCssUrls() {
        return cssUrls;
    }

    public Set<String> getJavaScriptUrls() {
        return javaScriptUrls;
    }    

    public LibraryUrls addAll(LibraryUrls other) {
        javaScriptUrls.addAll(other.getJavaScriptUrls());
        cssUrls.addAll(other.getCssUrls());
        
        return this;
    }
}
