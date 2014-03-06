package knorxx.framework.generator.order;

import knorxx.framework.generator.order.GenerationResultComparator;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import knorxx.framework.generator.NamedResult;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.number.OrderingComparison.lessThan;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class GenerationResultComparatorTest {

    @Test
    public void compare1() {
        List<String> result = test(Lists.newArrayList("aaa", "bbb", "ccc", "ddd"),
                Lists.newArrayList("aaa", "bbb", "ddd"));
        
        assertThat(result.indexOf("bbb"), is(lessThan(result.indexOf("ddd"))));        
        assertThat(result.indexOf("aaa"), is(lessThan(result.indexOf("bbb"))));        
        assertThat(result.indexOf("aaa"), is(lessThan(result.indexOf("ddd"))));        
    }
    
    private List<String> test(List<String> all, List<String> sorted) {
        List<NamedResult> nameables = new ArrayList<>();
        
        for (final String javaClassName : all) {
            nameables.add(new NamedResult() {
                @Override
                public String getName() {
                    return javaClassName;
                }
            });
        }
        
        Collections.sort(nameables, new GenerationResultComparator(sorted));
        
        List<String> resultNames = new ArrayList<>();
        for(NamedResult nameable : nameables) {
            resultNames.add(nameable.getName());
        }
        
        return resultNames;
    }
}