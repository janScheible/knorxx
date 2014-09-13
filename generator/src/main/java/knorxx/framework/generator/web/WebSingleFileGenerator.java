package knorxx.framework.generator.web;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.io.CharStreams;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Set;
import knorxx.framework.generator.JavaFileWithSource;
import knorxx.framework.generator.single.JavaScriptResult;
import knorxx.framework.generator.single.SingleFileGenerator;
import knorxx.framework.generator.single.SingleFileGeneratorException;
import knorxx.framework.generator.single.SingleResult;

/**
 *
 * @author sj
 */
public class WebSingleFileGenerator extends SingleFileGenerator {
    
    private final SingleFileGenerator genericSingleFileGenerator;
    private final String allowedGenerationPackage;
    
    private final SingleFileGenerator[] specialFileGenerators;

    public WebSingleFileGenerator(SingleFileGenerator genericSingleFileGenerator, String allowedGenerationPackage, SingleFileGenerator...specialFileGenerators) {
        this.genericSingleFileGenerator = genericSingleFileGenerator;
        this.allowedGenerationPackage = allowedGenerationPackage;
        
        this.specialFileGenerators = specialFileGenerators;
    }

    @Override
    public SingleResult generate(JavaFileWithSource<?> javaFile, ClassLoader classLoader, Set<String> allowedPackages) throws SingleFileGeneratorException {
        Optional<String> preGeneratedSource = getPreGeneratedSource(javaFile.getJavaClass());
        
        if (preGeneratedSource.isPresent()) {
            return new JavaScriptResult(preGeneratedSource.get());
        } else {
            for(SingleFileGenerator specialFileGenerator : specialFileGenerators) {
                if(specialFileGenerator.isGeneratable(javaFile.getJavaClass())) {
                    return specialFileGenerator.generate(javaFile, classLoader, allowedPackages);
                }
            }
        }

        return genericSingleFileGenerator.generate(javaFile, classLoader, allowedPackages);
    }

    @Override
    public boolean isGeneratable(Class<?> javaClass) {        
        if(getPreGeneratedSource(javaClass).isPresent()) {
            return true;
        } 
        
        for (SingleFileGenerator specialFileGenerator : specialFileGenerators) {
            if (specialFileGenerator.isGeneratable(javaClass)) {
                return true;
            }
        }
        
        if(!javaClass.getPackage().getName().startsWith(allowedGenerationPackage)) {
            return false;
        }
        
        return genericSingleFileGenerator.isGeneratable(javaClass);
    }
    
    public static Optional<String> getPreGeneratedSource(Class<?> javaClass) {
        URL url = javaClass.getResource(javaClass.getSimpleName() + ".js");
        
        if (url != null) {
            try (InputStream stream = url.openStream()) {
                String source = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8));
                return Optional.of(source);
            } catch (Exception ex) {
                // do nothing... we're simple not able to find a pre generated source
            }
        }
        
        return Optional.absent();
    }    
}
