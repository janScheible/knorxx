package knorxx.framework.generator.web.generator;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.io.CharStreams;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import knorxx.framework.generator.single.JavaScriptResult;
import knorxx.framework.generator.single.SingleFileGeneratorException;
import knorxx.framework.generator.single.SingleResult;

/**
 *
 * @author sj
 */
public class PreGeneratedSourceGenerator extends SpecialFileGenerator {

	@Override
	public SingleResult generate(Class<?> javaClass) throws SingleFileGeneratorException {
		return new JavaScriptResult(getPreGeneratedSource(javaClass).get());
	}

	@Override
	public boolean isGeneratable(Class<?> javaClass) {
		return getPreGeneratedSource(javaClass).isPresent();
	}
	
    private Optional<String> getPreGeneratedSource(Class<?> javaClass) {
        URL url = javaClass.getResource(javaClass.getSimpleName() + ".js");
        
        if (url != null) {
            try (InputStream stream = url.openStream()) {
                String source = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8));
                return Optional.of(source);
            } catch (Exception ex) {
                // do nothing... we're simply not able to find a pre generated source
            }
        }
        
        return Optional.absent();
    }	
}
