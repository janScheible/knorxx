package knorxx.framework.generator.web.server.rpc;

/**
 *
 * @author sj
 */
public class CsrfAttackException extends SecurityException {

    public CsrfAttackException(String s) {
        super(s);
    }
}
