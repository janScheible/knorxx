package knorxx.framework.generator.library;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sj
 */
public class LibraryUrls {
    
    private final List<String> javaScriptUrls = new ArrayList<>();
    private final List<String> cssUrls = new ArrayList<>();

    public List<String> getCssUrls() {
        return cssUrls;
    }

    public List<String> getJavaScriptUrls() {
        return javaScriptUrls;
    }    

    public LibraryUrls addAll(LibraryUrls other) {
        javaScriptUrls.addAll(other.getJavaScriptUrls());
        cssUrls.addAll(other.getCssUrls());
        
        return this;
    }
}
