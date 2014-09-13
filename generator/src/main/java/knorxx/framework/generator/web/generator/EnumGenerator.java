package knorxx.framework.generator.web.generator;

import com.google.common.base.Optional;
import java.util.ArrayList;
import java.util.List;
import knorxx.framework.generator.reloading.NotYetLoadedJavaFile;
import knorxx.framework.generator.single.JavaScriptResult;
import knorxx.framework.generator.single.SingleFileGeneratorException;
import knorxx.framework.generator.single.SingleResult;
import knorxx.framework.generator.web.WebSingleFileGenerator;
import knorxx.framework.generator.web.generator.stjs.StjsJavaScriptClassBuilder;

/**
 * This special enum generator emulates the st-js enum generation with one important exception: It does not need the
 * source of the enum class to be available. This allows easy usage of enums from other JARs.
 * 
 * @author sj
 */
public class EnumGenerator extends SpecialFileGenerator {

	@Override
	public SingleResult generate(Class<?> javaClass) throws SingleFileGeneratorException {
		List<String> constants = new ArrayList<>();
		for (Enum constant : ((Class<Enum<?>>) javaClass).getEnumConstants()) {
			constants.add(constant.name());
		}
		
		StjsJavaScriptClassBuilder builder = new StjsJavaScriptClassBuilder(javaClass)
				.enumeration(javaClass, constants.toArray(new String[]{}));
		
		return new JavaScriptResult(builder.create());
	}

	@Override
	public boolean isGeneratable(Class<?> javaClass) {
		return javaClass.isEnum() && WebSingleFileGenerator.getPreGeneratedSource(javaClass).isPresent();
	}
}
