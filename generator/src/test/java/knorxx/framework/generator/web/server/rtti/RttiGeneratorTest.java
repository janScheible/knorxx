package knorxx.framework.generator.web.server.rtti;

import com.google.common.collect.Lists;
import knorxx.framework.generator.testclass.TestWebPage;
import static org.hamcrest.core.StringContains.containsString;
import org.joda.time.DateTime;
import static org.junit.Assert.assertThat;
import org.junit.Test;


/**
 *
 * @author sj
 */
public class RttiGeneratorTest {
    
    @Test
    public void testGetJavaScriptSource() {
        RttiGenerationResult result = new RttiGenerator().getJavaScriptSource("", Lists.<Class<?>>newArrayList(
                                     TestWebPage.class), new UrlResolver("", ""), new DateTime());
        
        assertThat(result.getSingleResult().getSource(), containsString(TestWebPage.class.getName()));
        assertThat(result.getSingleResult().getSource(), containsString(TestWebPage.class.getSimpleName()));
    }
}