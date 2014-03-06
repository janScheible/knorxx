package knorxx.framework.generator.web.generator.stjs;

/**
 *
 * @author sj
 */
public class StjsJavaScriptClassBuilder extends AbstractStjsJavaScriptClassBuilder<StjsJavaScriptClassBuilder> {

    public StjsJavaScriptClassBuilder(Class namespaceJavaClass) {
        super(namespaceJavaClass);
    }
    
    @Override
    protected StjsJavaScriptClassBuilder self() {
        return this;
    }
}
