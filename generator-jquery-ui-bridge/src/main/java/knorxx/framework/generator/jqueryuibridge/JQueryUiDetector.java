package knorxx.framework.generator.jqueryuibridge;

import java.util.Set;
import knorxx.framework.generator.library.LibraryDetector;
import knorxx.framework.generator.library.LibraryUrls;
import org.stjs.javascript.jquery.GlobalJQueryUI;

/**
 *
 * @author sj
 */
public class JQueryUiDetector extends LibraryDetector {

    public JQueryUiDetector() {
    }

    public JQueryUiDetector(LibraryDetector nextDetector) {
        super(nextDetector);
    }

    @Override
    protected LibraryUrls detectInternal(Set<String> javaClassNames) {
        LibraryUrls result = new LibraryUrls();
        
        for(String javaClassName : javaClassNames) {
            if(javaClassName.contains(GlobalJQueryUI.class.getName())) {
                result.getJavaScriptUrls().add("webjars/jquery-ui/1.10.3/ui/jquery-ui.js");
                result.getCssUrls().add("webjars/jquery-ui-themes/1.10.3/ui-lightness/jquery-ui.css");
                
                break;
            }
        }
        
        return result;
    }
}
