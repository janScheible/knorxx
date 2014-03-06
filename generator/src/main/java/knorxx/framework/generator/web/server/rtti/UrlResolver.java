package knorxx.framework.generator.web.server.rtti;

/**
 *
 * @author sj
 */
public class UrlResolver {

    private final String prefix;
    private final String rootPackage;

    public UrlResolver(String prefix, String rootPackage) {
        this.prefix = prefix;
        this.rootPackage = rootPackage;
    }
    
    public String resolveWebPage(String javaClassName) {
        return ensurePrefix(javaClassName.substring(rootPackage.length() + 1)).replace(".", "/") + ".html";
    }
    
    public String resolveJavaScriptFile(String javaClassName) {
        return ensurePrefix(javaClassName).replace(".", "/") + ".js";
    }

    public String resolveCssFile(String javaClassName) {
        return ensurePrefix(javaClassName).replace(".", "/") + ".css";
    }
    
    private String ensurePrefix(String javaClassName) {
        return (javaClassName.startsWith(prefix + ".") ? "" : prefix + ".") + javaClassName;
    }
}
