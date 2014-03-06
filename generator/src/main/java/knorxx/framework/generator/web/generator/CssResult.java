package knorxx.framework.generator.web.generator;

import java.util.Map;
import knorxx.framework.generator.single.SingleResult;

/**
 *
 * @author sj
 */
public class CssResult extends SingleResult {
    
    private final Map<String, String> cssDefinitions;

    public CssResult(String source, Map<String, String> cssDefinitions) {
        super(source);
        
        this.cssDefinitions = cssDefinitions;
    }

    public Map<String, String> getCssDefinitions() {
        return cssDefinitions;
    }
    
    public String getCssSource() {
        StringBuilder cascadingStyleSheets = new StringBuilder();
        
        for(String cssClassName : cssDefinitions.keySet()) {
            cascadingStyleSheets.append(cssClassName);
            cascadingStyleSheets.append(" ");
            cascadingStyleSheets.append(cssDefinitions.get(cssClassName));
            cascadingStyleSheets.append("\n");
        }
        
        return cascadingStyleSheets.toString();
    }
}
