package knorxx.framework.generator.web.generator;

import com.google.common.collect.Sets;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import knorxx.framework.generator.JavaFileWithSource;
import knorxx.framework.generator.single.JavaScriptResult;
import knorxx.framework.generator.single.SingleFileGeneratorException;
import knorxx.framework.generator.single.SingleResult;
import knorxx.framework.generator.util.JavaIdentifierUtils;
import knorxx.framework.generator.web.generator.stjs.StjsJavaScriptClassBuilder;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

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
				.enumeration(javaClass, constants.toArray(new String[]{})).newLine();
		
		Map<String, Object> enumConstValueGetters = getEnumConstValueGetters((Class<Enum<?>>) javaClass);
		for(String constantAndGetterName : enumConstValueGetters.keySet()) {
			builder.staticFunction(javaClass, constantAndGetterName)
				.code("return ").literal(enumConstValueGetters.get(constantAndGetterName)).semicolon()
			._function();
		}
		
		return new JavaScriptResult(builder.create());
	}

	@Override
	public boolean isGeneratable(Class<?> javaClass) {
		return JavaIdentifierUtils.hasSuperclassOrImplementsInterface(javaClass, Enum.class.getName());
	}
	
	private Map<String, Object> getEnumConstValueGetters(Class<? extends Enum> enumClass) {
		Map<String, Object> fieldGetterMap = new HashMap<>();

		Set<String> internalNames = Sets.newHashSet("$VALUES");
		for (Enum constant : enumClass.getEnumConstants()) {
			internalNames.add(constant.name());
		}

		List<Pair<Field, String>> fieldsAndGetters = new ArrayList<>();
		for (Field field : enumClass.getDeclaredFields()) {
			if (!internalNames.contains(field.getName())) {
				String getterName = "get" + WordUtils.capitalize(field.getName(), '\0');
				if (hasGetter(enumClass, getterName)) {
					fieldsAndGetters.add(new ImmutablePair<>(field, getterName));
				}
			}
		}
		
		for (Pair<Field, String> fieldAndGetter : fieldsAndGetters) {
			for (Enum constant : enumClass.getEnumConstants()) {
				fieldGetterMap.put(constant.name() + "." + fieldAndGetter.getRight(),
						getValue(fieldAndGetter.getLeft(), constant));
			}
		}

		return fieldGetterMap;
	}
	
	private Object getValue(Field field, Enum constant) {
		try {
			field.setAccessible(true);
			return field.get(constant);
		} catch (IllegalArgumentException | IllegalAccessException ex) {
			throw new IllegalStateException("An error occured while getting the value of the field '" + field.getName() +
					"' of the enum constant '" + constant.name() + "'!", ex);
		}
	}

	private boolean hasGetter(Class<? extends Enum> enumClass, String getterName) {
		try {
			enumClass.getMethod(getterName);
			return true;
		} catch (NoSuchMethodException ex) {
			return false;
		} catch (SecurityException ex) {
			throw new IllegalStateException("An error occured while searching for a getter named '" + getterName
					+ "' in the class '" + enumClass.getName() + "'!", ex);
		}
	}	
}
