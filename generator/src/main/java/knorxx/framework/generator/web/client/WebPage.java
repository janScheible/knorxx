package knorxx.framework.generator.web.client;

import knorxx.framework.generator.web.client.webpage.PageModel;
import org.stjs.javascript.Map;
import org.stjs.javascript.annotation.Namespace;

/**
 *
 * @author sj
 */
@Namespace("knorxx.framework.generator.web.client")
public abstract class WebPage {
    
	public static final String CONTAINER_ID = "#container";
	
    public static final String MENU_ID = "#menu";
    public static final String TITLE_ID = "#title";
    public static final String CONTENT_ID = "#content";
    
    public abstract void render();
    
    public Map<String, Object> getModel() {
        return ((PageModel)(Object)this).model;
    }
}
