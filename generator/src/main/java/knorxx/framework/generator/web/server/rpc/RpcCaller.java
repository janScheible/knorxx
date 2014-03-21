package knorxx.framework.generator.web.server.rpc;

import com.google.common.base.Optional;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import knorxx.framework.generator.util.HttpCookieUtils;
import knorxx.framework.generator.web.client.RpcService;
import knorxx.framework.generator.web.server.json.JsonHelper;
import knorxx.framework.generator.web.server.rpc.RpcResult.Status;

/**
 *
 * @author sj
 */
public class RpcCaller {
    
    public final static String CSRF_PROTECTION_COOKIE_NAME = "RPCSESSIONID";
    
    public RpcResult call(RpcCall rpcCall, ExceptionMarshaller exceptionSerializer, JsonHelper jsonHelper, 
            List<RpcService> rpcServices, HttpServletRequest request) {
        Optional<Cookie> csrfProtectionCookie = HttpCookieUtils.getCookie(CSRF_PROTECTION_COOKIE_NAME, request);
        if(!csrfProtectionCookie.isPresent()) {
            throw new MissingCsrfProtectionCookieException(String.format("Can't find a CSRF protection cookie "
                    + "named '%s'!", CSRF_PROTECTION_COOKIE_NAME));
        } else if(!csrfProtectionCookie.get().getValue().equals(rpcCall.getCsrfProtectionToken())) {
            throw new CsrfAttackException(String.format("A CSRF attack was detected while calling the method "
                    + "'%s' of the service '%s'.", rpcCall.getMethodName(), rpcCall.getServiceName()));
        }
        
        for(RpcService rpcService : rpcServices) {
            Class serviceClass = rpcService.getClass();
            if(serviceClass.getName().equals(rpcCall.getServiceName())) {
                for(Method method : serviceClass.getDeclaredMethods()) {
                    if(method.getName().equals(rpcCall.getMethodName())) {
                        List<Object> arguments = new ArrayList<>();
                        int argIndex = 0;
                        
                        for(Class<?> parameterType : method.getParameterTypes()) {
                            if(parameterType.equals(HttpServletRequest.class)) {
                                arguments.add(request);
                                argIndex++;
                            } else if(parameterType.equals(HttpSession.class)) {
                                arguments.add(request.getSession());
                                argIndex++;
                            } else if(argIndex < rpcCall.getArgumentsJsons().size()) {
                                arguments.add(jsonHelper.fromJson(rpcCall.getArgumentsJsons().get(argIndex), parameterType));
                                argIndex++;
                            } else {
                                arguments.add(null);
                            }
                        }
                        
                        Object rpcResult;
                        try {
                            rpcResult = method.invoke(rpcService, arguments.toArray());
                        } catch (IllegalAccessException | IllegalArgumentException ex) {
                            throw new IllegalStateException(String.format("An error occured while invoking the metohd "
                                    + "'%s' of the service '%s' via refelction.", rpcCall.getMethodName(),
                                    rpcCall.getServiceName()), ex);
                        } catch(InvocationTargetException ex) {
                            return new RpcResult(Status.EXCEPTION, exceptionSerializer.marshall(ex.getCause(), jsonHelper));
                        }
                        
                        return new RpcResult(Status.SUCCESS, jsonHelper.toJson(rpcResult, method.getReturnType()));
                    }
                }
                
                throw new IllegalStateException("The service '" + rpcCall.getServiceName() + "' has no public method "
                        + "named '" + rpcCall.getMethodName() + "'!");
            }            
        }
        
        throw new IllegalStateException("There's no registered service with the name '" + rpcCall.getServiceName() + "'!");
    }
}
