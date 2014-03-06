/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package knorxx.framework.generator.springadapter;

import com.google.common.base.Function;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import knorxx.framework.generator.GenerationRoots;

/**
 *
 * @author sj
 */
public class GenerationRootsRequestFunction implements Function<HttpServletRequest, GenerationRoots> {
    
    private final String classPathName;

    public GenerationRootsRequestFunction(String classPathName) {
        this.classPathName = classPathName;
    }
    
    @Override
    public GenerationRoots apply(HttpServletRequest request) {
        try {
            return new GenerationRoots.PropertyFile(request.getServletContext().getResourceAsStream(classPathName));
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to load the file '" + classPathName + "'!");
        }
    }
}
