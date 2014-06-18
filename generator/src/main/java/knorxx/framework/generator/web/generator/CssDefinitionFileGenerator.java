package knorxx.framework.generator.web.generator;

import com.google.common.base.CaseFormat;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import knorxx.framework.generator.single.SingleFileGeneratorException;
import static knorxx.framework.generator.util.JavaIdentifierUtils.hasSuperclassOrImplementsInterface;
import knorxx.framework.generator.web.client.css.CssDefinition;
import knorxx.framework.generator.web.client.css.annotation.NoRule;
import knorxx.framework.generator.web.client.css.annotation.StyleScopeClass;
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
        
		String styleScope = javaClass.getAnnotation(StyleScopeClass.class) != null ? 
				"." + javaClass.getAnnotation(StyleScopeClass.class).value() + " " : "";
        for(Field field : javaClass.getDeclaredFields()) {
            if(field.getType().equals(String.class)) {
				String fieldValue;
				try {
					fieldValue = (String) field.get(null);
				} catch (IllegalArgumentException | IllegalAccessException ex) {
					throw new SingleFileGeneratorException(ex, null);
				}
				
				if(field.getAnnotation(NoRule.class) == null) {
					String styleName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, field.getName());
					cssDefinitions.put(styleScope + "." + styleName, "{" + fieldValue + "}");
			
					builder
						.staticFunction(javaClass, field.getName())
							.code("return ").literal(styleName).semicolon().newLine()
						._function();
				} else {					
					builder
						.staticFunction(javaClass, field.getName())
							.code("return ").literal(fieldValue).semicolon().newLine()
						._function();					
				}
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
