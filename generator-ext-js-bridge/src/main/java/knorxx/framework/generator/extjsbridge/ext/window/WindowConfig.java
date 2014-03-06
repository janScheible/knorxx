package knorxx.framework.generator.extjsbridge.ext.window;

import knorxx.framework.generator.extjsbridge.Config;
import org.stjs.javascript.annotation.SyntheticType;
import org.stjs.javascript.annotation.Template;

/**
 *
 * @author sj
 */
@SyntheticType
public class WindowConfig extends Config {
    
    private String title;
    private int height;
    private int width;
    private String layout;
    private String xclass;

    public WindowConfig() {
    }

    @Template("propertyAccess")
    public void setHeight(int height) {
        this.height = height;
    }

    @Template("propertyAccess")
    public void setLayout(String layout) {
        this.layout = layout;
    }

    @Template("propertyAccess")
    public void setTitle(String title) {
        this.title = title;
    }

    @Template("propertyAccess")
    public void setWidth(int width) {
        this.width = width;
    }

    @Template("propertyAccess")
    public int getHeight() {
        return height;
    }

    @Template("propertyAccess")
    public String getLayout() {
        return layout;
    }

    @Template("propertyAccess")
    public String getTitle() {
        return title;
    }

    @Template("propertyAccess")
    public int getWidth() {
        return width;
    }

    @Template("propertyAccess")
    public void setXclass(String xclass) {
        this.xclass = xclass;
    }

    @Template("propertyAccess")
    public String getXclass() {
        return xclass;
    }    
}
