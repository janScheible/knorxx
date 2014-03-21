package knorxx.framework.generator.util;

import com.google.common.base.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author sj
 */
public class HttpCookieUtils {
    
    public static Optional<Cookie> getCookie(String name, HttpServletRequest request) {
        if(request.getCookies() != null) {
            for(Cookie cookie : request.getCookies()) {
                if(cookie.getName().equals(name)) {
                    return Optional.of(cookie);
                }
            }
        }
        
        return Optional.absent();
    }
}
