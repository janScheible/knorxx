package knorxx.framework.generator.extjsbridge;

import java.util.Set;
import knorxx.framework.generator.library.LibraryDetector;
import knorxx.framework.generator.library.LibraryUrls;

/**
 *
 * @author sj
 */
public class ExtJsDetector extends LibraryDetector {

    public ExtJsDetector() {
    }

    public ExtJsDetector(LibraryDetector nextDetector) {
        super(nextDetector);
    }

    @Override
    protected LibraryUrls detectInternal(Set<String> javaClassNames) {
        LibraryUrls result = new LibraryUrls();

        for(String javaClassName : javaClassNames) {
            if(javaClassName.contains(Ext.class.getPackage().getName())) {
                result.getJavaScriptUrls().add("webjars/extjs/4.2.1.883/ext-debug.js");
                result.getCssUrls().add("webjars/extjs/4.2.1.883/resources/css/ext-all.css");
                
                break;
            }
        }
        
        return result;
    }
}
