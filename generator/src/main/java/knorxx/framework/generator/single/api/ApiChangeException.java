package knorxx.framework.generator.single.api;

/**
 *
 * @author sj
 */
public class ApiChangeException extends RuntimeException {

    public ApiChangeException(String message) {
        super(message);
    }

    public ApiChangeException(String message, Throwable cause) {
        super(message, cause);
    }
}
