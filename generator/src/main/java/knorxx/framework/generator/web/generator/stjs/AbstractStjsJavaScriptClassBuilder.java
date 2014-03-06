package knorxx.framework.generator.web.generator.stjs;

import knorxx.framework.generator.web.generator.AbstractJavaScriptBuilder;

/**
 *
 * @author sj
 */
public abstract class AbstractStjsJavaScriptClassBuilder<T extends AbstractStjsJavaScriptClassBuilder<T>> extends AbstractJavaScriptBuilder<T>{

    public AbstractStjsJavaScriptClassBuilder(Class namespaceJavaClass) {
        this.namespace(namespaceJavaClass);
    }

    @Override
    public final T namespace(Class javaClass) {
        return super.namespace(javaClass);
    }
}
