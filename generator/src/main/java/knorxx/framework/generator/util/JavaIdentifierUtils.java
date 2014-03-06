package knorxx.framework.generator.util;

import com.google.common.base.CharMatcher;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author sj
 */
public class JavaIdentifierUtils {

    public static String getJavaClassSimpleName(String javaClassName) {
        List<String> classNameParts = Splitter.on(".").splitToList(javaClassName);
        String simpleClassName = classNameParts.get(classNameParts.size() - 1);
        return simpleClassName;
    }

    public static boolean isInnerClass(String javaClassName) {
        return javaClassName.contains("$") && !javaClassName.endsWith("$");
    }

    public static boolean isValidClassName(String className) {
        if (!className.contains(".")) {
            return className.matches("[A-Z][A-Za-z\\$]+");
        } else {
            String packageName = getPackageName(className);
            return isValidClassName(className.substring(packageName.length() + 1, className.length()));
        }
    }

    public static int getJavaClassNestingLevel(String javaClassName) {
        return CharMatcher.anyOf("$").countIn(javaClassName);
    }

    public static boolean isValidConstantName(String fieldName) {
        return fieldName.matches("[A-Z][A-Z_]+");
    }

    public static String getPackageName(String javaClassName) {
        List<String> classNameParts = new ArrayList<>(Splitter.on(".").splitToList(javaClassName));
        classNameParts.remove(classNameParts.size() - 1);
        return Joiner.on(".").join(classNameParts);
    }

    public static boolean isJavaCoreClass(String javaClassName) {
        return javaClassName.startsWith("java.") || javaClassName.startsWith("javax.");
    }

    public static boolean hasSuperclassOrImplementsInterface(Class<?> javaClass, String superJavaClassOrInterfaceName) {
        while (javaClass != null) {
            if (javaClass.getName().equals(superJavaClassOrInterfaceName)) {
                return true;
            }

            for (Class javaInterface : javaClass.getInterfaces()) {
                if (javaInterface.getName().equals(superJavaClassOrInterfaceName)) {
                    return true;
                }
            }

            javaClass = javaClass.getSuperclass();
        }

        return false;
    }

    public static Set<String> removeInnerClassesAndInvalidClassNames(Set<String> javaClassNames) {
        return new HashSet<>(Sets.filter(javaClassNames, new Predicate<String>() {
            @Override
            public boolean apply(String javaClassName) {
                return !isInnerClass(javaClassName) && isValidClassName(javaClassName);
            }
        }));
    }

    public static Set<String> javaClassNamesToPackages(Set<String> javaClassNames) {
        return new HashSet<>(Lists.transform(new ArrayList<>(javaClassNames), new Function<String, String>() {
            @Override
            public String apply(String javaClassName) {
                return getPackageName(javaClassName);
            }
        }));
    }

    public static Set<String> removeJavaCoreClassNames(Set<String> javaClassNames) {
        return new HashSet<>(Sets.filter(javaClassNames, new Predicate<String>() {
            @Override
            public boolean apply(String javaClassName) {
                return !isJavaCoreClass(javaClassName);
            }
        }));
    }
}
