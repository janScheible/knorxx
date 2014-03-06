package knorxx.framework.generator.single;

/**
 *
 * @author sj
 */
public abstract class SingleResult {

    private final String source;

    public SingleResult(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    @Override
    public String toString() {
        return source;
    }
}
