package knorxx.framework.generator.web.server.rpc;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import knorxx.framework.generator.web.client.RpcService;
import knorxx.framework.generator.web.server.json.GsonHelper;
import knorxx.framework.generator.web.server.json.JsonHelper;
import knorxx.framework.generator.web.server.rpc.testclass.TestService;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author sj
 */
public class RpcCallerTest {
    
    private final static String CSRF_PROTECTION_TOKEN = "CSRF_PROTECTION_TOKEN";
    
    private JsonHelper jsonHelper  = new GsonHelper();
    private RpcCaller rpcCaller = new RpcCaller();

    private HttpServletRequest request;
    private HttpSession session;
    
    @Before
    public void setUp() {
        request = Mockito.mock(HttpServletRequest.class);
        session = Mockito.mock(HttpSession.class);
        Mockito.when(request.getSession()).thenReturn(session);
        Cookie csrfProtectionCookie = new Cookie(RpcCaller.CSRF_PROTECTION_COOKIE_NAME, CSRF_PROTECTION_TOKEN);
        Mockito.when(request.getCookies()).thenReturn(new Cookie[]{csrfProtectionCookie});
    }

    @Test
    public void regularCall() {
        RpcResult result = call(new RpcCall(TestService.class.getName(), TestService.GET_BY_ID_METHOD_NAME, 
                CSRF_PROTECTION_TOKEN, Lists.newArrayList("null", "12")), new VerboseExceptionMarshaller());
        assertEquals(RpcResult.Status.SUCCESS, result.getStatus());
        assertThat(result.getJsonResult(), containsString(TestService.GET_BY_ID_RESULT));
    }
    
    @Test
    public void causeException() {
        RpcResult result = call(new RpcCall(TestService.class.getName(), TestService.THROW_EXCEPTION_METHOD_NAME, 
                CSRF_PROTECTION_TOKEN, new ArrayList<String>()), new VerboseExceptionMarshaller());
        assertEquals(RpcResult.Status.EXCEPTION, result.getStatus());
        assertThat(result.getJsonResult(), containsString(TestService.THROW_EXCEPTION_NAME));
    }    
    
    public RpcResult call(RpcCall rpcCall, ExceptionMarshaller exceptionMarshaller) {
        RpcResult result = rpcCaller.call(rpcCall, exceptionMarshaller, jsonHelper, 
                Lists.<RpcService>newArrayList(new TestService()), request);
        return result;
    }
}