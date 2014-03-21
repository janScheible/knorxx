package knorxx.framework.generator.web.server.rpc;

/**
 *
 * @author sj
 */
public class MissingCsrfProtectionCookieException extends SecurityException {

    public MissingCsrfProtectionCookieException(String s) {
        super(s);
    }
}
