package knorxx.framework.generator.web.server.rtti;

import java.util.HashSet;
import knorxx.framework.generator.GenerationResult;
import knorxx.framework.generator.single.SingleResult;
import org.joda.time.DateTime;

/**
 *
 * @author sj
 */
public class RttiGenerationResult extends GenerationResult {
    
    private final String name;
    
    /* package */ RttiGenerationResult(String name, DateTime generationDateTime, SingleResult singleResult) {
        super(null, generationDateTime, new HashSet<String>(), singleResult);        
        
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
