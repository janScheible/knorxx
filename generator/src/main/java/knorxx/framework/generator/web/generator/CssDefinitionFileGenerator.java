package knorxx.framework.generator.web.generator;

import com.google.common.base.CaseFormat;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import knorxx.framework.generator.single.SingleFileGeneratorException;
import static knorxx.framework.generator.util.JavaIdentifierUtils.hasSuperclassOrImplementsInterface;
import knorxx.framework.generator.web.client.CssDefinition;
import knorxx.framework.generator.web.generator.stjs.StjsJavaScriptClassBuilder;

/**
 *
 * @author sj
 */
public class CssDefinitionFileGenerator extends SpecialFileGenerator {

    @Override
    public CssResult generate(Class<?> javaClass) throws SingleFileGeneratorException {
        Map<String, String> cssDefinitions = new HashMap<>();
        
        StjsJavaScriptClassBuilder builder = new StjsJavaScriptClassBuilder(javaClass)
            .constructor(javaClass)
            ._constructor();
        
        for(Field field : javaClass.getDeclaredFields()) {
            if(field.getType().equals(String.class)) {
                String styleName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, field.getName());
                
                try {
                    cssDefinitions.put("." + styleName, "{" + (String)field.get(null) + "}");
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    throw new SingleFileGeneratorException(ex, null);
                }
                        
                builder
                    .staticFunction(javaClass, field.getName())
                        .code("return ").literal(styleName).semicolon().newLine()
                    ._function();
            }
        }
                
        return new CssResult(builder.create(), cssDefinitions);
    }

    @Override
    public boolean isGeneratable(Class<?> javaClass) {
        return !javaClass.getName().equals(CssDefinition.class.getName()) &&
                hasSuperclassOrImplementsInterface(javaClass, CssDefinition.class.getName());
    }
}
