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
	
	public enum Theme { 
		UI_LIGHTNESS("ui-lightness"),
		REDMOND("redmond");
		
		private final String folder;

		private Theme(String folder) {
			this.folder = folder;
		}

		public String getFolder() {
			return folder;
		}
	}
	
	private final String themeFolder;

    public JQueryUiDetector() {
		this.themeFolder = Theme.UI_LIGHTNESS.getFolder();
    }

    public JQueryUiDetector(LibraryDetector nextDetector) {
        super(nextDetector);
		this.themeFolder = Theme.UI_LIGHTNESS.getFolder();
    }
	
    public JQueryUiDetector(Theme theme, LibraryDetector nextDetector) {
        super(nextDetector);
		this.themeFolder = theme.getFolder();
    }
	
    public JQueryUiDetector(String themeFolder, LibraryDetector nextDetector) {
        super(nextDetector);
		this.themeFolder = themeFolder;
    }		

    @Override
    protected LibraryUrls detectInternal(Set<String> javaClassNames) {
        LibraryUrls result = new LibraryUrls();
        
        for(String javaClassName : javaClassNames) {
            if(javaClassName.contains(GlobalJQueryUI.class.getName())) {
                result.getJavaScriptUrls().add("webjars/jquery-ui/1.10.3/ui/jquery-ui.js");
                result.getCssUrls().add("webjars/jquery-ui-themes/1.10.3/" + themeFolder + "/jquery-ui.css");
                
                break;
            }
        }
        
        return result;
    }
}
