package knorxx.framework.generator.web.client;

import static org.stjs.javascript.Global.alert;
import org.stjs.javascript.annotation.Namespace;

/**
 * This file contains error handler functions which can be customized. The default are simple alert boxes.
 * 
 * @author sj
 */
@Namespace("knorxx.framework.generator.web.client")
public class ErrorHandler {

    public static final String INSTANCE_NAME = "knorxxErrorHandler";
    
    public void displayRuntimeError(String message, String name, int lineNumber, String stackTrace) {
        alert(message + "\nname: " + name + "\nlineNumber: " + lineNumber + "\nstackTrace:" + stackTrace);
    }

    public void displayCommunicationError() {
        alert("Server communication error!");
    }
}
