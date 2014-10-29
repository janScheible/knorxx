package knorxx.framework.generator.web.generator;

import java.util.HashSet;
import java.util.Set;
import knorxx.framework.generator.JavaFileWithSource;
import knorxx.framework.generator.single.SingleFileGenerator;
import knorxx.framework.generator.single.SingleFileGeneratorException;
import knorxx.framework.generator.single.SingleResult;

/**
 *
 * @author sj
 */
public abstract class SpecialFileGenerator extends SingleFileGenerator {
    
    @Override
    public SingleResult generate(JavaFileWithSource<?> javaFile, ClassLoader classLoader, Set<String> allowedPackages) throws SingleFileGeneratorException {
        return generate(javaFile.getJavaClass());
    }
    
    public abstract SingleResult generate(Class<?> javaClass) throws SingleFileGeneratorException;

	/**
	 * Special file generators do not have any dependent files by default.
	 */
	@Override
	public Set<String> removeNotGeneratableJavaClasses(Class javaClass, Set<String> javaClassNames, ClassLoader classLoader) {
		return new HashSet<>();
	}	
}
