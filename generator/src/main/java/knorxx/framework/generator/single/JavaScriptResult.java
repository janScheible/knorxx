package knorxx.framework.generator.single;

import com.google.common.base.Optional;

/**
 *
 * @author sj
 */
public class JavaScriptResult extends SingleResult {
    
    private final Optional<String> sourceMap;

    public JavaScriptResult(String source) {
        super(source);
        sourceMap = Optional.absent();
    }
    
    public JavaScriptResult(String source, String sourceMap) {
        super(source);
        this.sourceMap = Optional.of(sourceMap);
    }    

    public Optional<String> getSourceMap() {
        return sourceMap;
    }
}
