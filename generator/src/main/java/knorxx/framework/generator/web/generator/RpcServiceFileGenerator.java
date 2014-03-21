package knorxx.framework.generator.web.generator;

import java.lang.reflect.Method;
import java.util.UUID;
import knorxx.framework.generator.single.JavaScriptResult;
import knorxx.framework.generator.single.SingleFileGeneratorException;
import static knorxx.framework.generator.util.JavaIdentifierUtils.hasSuperclassOrImplementsInterface;
import knorxx.framework.generator.web.client.ErrorHandler;
import knorxx.framework.generator.web.client.JsonHelper;
import knorxx.framework.generator.web.client.RpcService;
import knorxx.framework.generator.web.generator.stjs.StjsJavaScriptClassBuilder;
import static knorxx.framework.generator.web.server.rpc.RpcCall.*;
import knorxx.framework.generator.web.server.rpc.RpcCaller;
import knorxx.framework.generator.web.server.rpc.RpcResult;
import knorxx.framework.generator.web.server.rpc.VerboseExceptionMarshaller;

/**
 *
 * @author sj
 */
public class RpcServiceFileGenerator extends SpecialFileGenerator {
    
    private final String csrfProtectionCookiePath;
    private final String rpcUrl;
    
    private final String csrfProtectionToken;    

    public RpcServiceFileGenerator(String csrfProtectionCookiePath, String rpcUrl) {
        this.csrfProtectionCookiePath = csrfProtectionCookiePath;
        this.rpcUrl = rpcUrl;
         // TODO Replace that with a cryptographically more secure session id generation method.
        this.csrfProtectionToken = UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    @Override
    public JavaScriptResult generate(Class<?> javaClass) throws SingleFileGeneratorException {
        StjsJavaScriptClassBuilder builder = new StjsJavaScriptClassBuilder(javaClass)
            .constructor(javaClass)
            ._constructor();
        
        for(Method method : javaClass.getDeclaredMethods()) {
            builder
                .function(javaClass, method)
                    .code("document.cookie = '%s=%s; path=%s'", RpcCaller.CSRF_PROTECTION_COOKIE_NAME, 
                            csrfProtectionToken, csrfProtectionCookiePath).newLine()
                    .code("var args = Array.prototype.slice.call(arguments);").newLine()
                    .code("var scope = args.pop();").newLine()
                    .code("var callback = args.pop();").newLine()
                    .newLine()
                    .jQueryPost()
                        .property("url").literal(rpcUrl).comma().newLine()
                        .property("data")
                            .noIndent().jsonStringifyObjectLiteral()._noIndent()
                            .newLine()
                                .property(SERVICE_NAME_PROPERTY).literal(javaClass.getName()).comma().newLine()
                                .property(METHOD_NAME_PROPERTY).literal(method.getName()).comma().newLine()
                                .property(CSRF_PROTECTION_TOKEN_PROPERTY).literal(csrfProtectionToken).comma().newLine()
                                .property(ARGUMENTS_PROPERTY).noIndent().code("args")._noIndent().newLine()
                            ._jsonStringifyObjectLiteral().comma().newLine()
                        .property("success")
                            .anonymousFunction("jsonData")
                                .code("var data = JSON.parse(jsonData, %s);", JsonHelper.PARSE_REVIVER_FUNCTION).newLine()
                                .if$("data.status === '" + RpcResult.Status.EXCEPTION.name() + "'")
                                    .code("%s.displayRuntimeError(data.%s.%s, data.%s.%s, data.%s.%s, data.%s.%s);", 
                                            ErrorHandler.INSTANCE_NAME, 
                                            RpcResult.JSON_RESULT_PROPERTY, VerboseExceptionMarshaller.MESSAGE_PROPERTY,
                                            RpcResult.JSON_RESULT_PROPERTY, VerboseExceptionMarshaller.NAME_PROPERTY,
                                            RpcResult.JSON_RESULT_PROPERTY, VerboseExceptionMarshaller.LINE_NUMER_PROPERTY,
                                            RpcResult.JSON_RESULT_PROPERTY, VerboseExceptionMarshaller.STACK_TRACE_PROPERTY).newLine()
                                .else$()
                                    .code("callback.call(scope, data.%s);", RpcResult.JSON_RESULT_PROPERTY).newLine()
                                ._if()
                            ._function()
                    ._jQueryPost()
                ._function();
        }
        
        return new JavaScriptResult(builder.create());
    }

    @Override
    public boolean isGeneratable(Class<?> javaClass) {
        return !javaClass.getName().equals(RpcService.class.getName()) && 
                hasSuperclassOrImplementsInterface(javaClass, RpcService.class.getName());
    }    
}
