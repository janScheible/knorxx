package knorxx.framework.generator.application;

import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author sj
 */
public class ApplicationResult extends HashMap<String, Object> {

	public void setApplicationName(String name) {
		put("applicationName", name);
	}

	public void setMainClassName(String name) {
		put("mainClassName", name);
	}

	public void setErrorHandlerClassName(String name) {
		put("errorHandlerClassName", name);
	}

	public void setJsonHelperClassName(String name) {
		put("jsonHelperClassName", name);
	}

	public void setLibraryCssUrls(Set<String> cssUrls) {
		put("libraryCssUrls", cssUrls);
	}

	public void setLibraryJavaScriptUrls(Set<String> javaScriptUrls) {
		put("libraryJavaScriptUrls", javaScriptUrls);
	}

	public void setCssUrls(Set<String> cssUrls) {
		put("cssUrls", cssUrls);
	}

	public void setJavaScriptUrls(Set<String> javaScriptUrls) {
		put("javaScriptUrls", javaScriptUrls);
	}

	public void setWebPageModelJson(String json) {
		put("webPageModelJson", json);
	}

	public void setPreRenderedHtml(String html) {
		put("preRenderedHtml", html);
	}	
}
