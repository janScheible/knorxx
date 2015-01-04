package knorxx.framework.generator;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import static com.google.common.base.Preconditions.checkArgument;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import knorxx.framework.generator.dependency.ByteCodeDependencyCollector;
import knorxx.framework.generator.dependency.DependencyCollector;
import knorxx.framework.generator.dependency.JavaSourceDependencyCollector;
import knorxx.framework.generator.library.LibraryDetector;
import knorxx.framework.generator.order.InheritanceOrderSorter;
import knorxx.framework.generator.order.OrderSorter;
import knorxx.framework.generator.single.SingleFileGenerator;
import knorxx.framework.generator.single.SingleFileGeneratorException;
import knorxx.framework.generator.single.SingleResult;
import static knorxx.framework.generator.util.JavaIdentifierUtils.javaClassNamesToPackages;
import static knorxx.framework.generator.util.JavaIdentifierUtils.removeJavaCoreClassNames;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author sj
 */
public class JavaScriptGenerator {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * @param dependencyCollector If no dependency collector is supplied the ByteCodeDependencyCollector and the 
     *                            JavaSourceDependencyCollector are used.
     * @param classLoader If no class loader is supplied Thread.currentThread().getContextClassLoader() is used.
     * @param orderSorter  If no order sorter is supplied the InheritanceOrderSorter is used.
     */
    public GenerationUnit generate(Class<?> javaClass, SingleFileGenerator singleFileGenerator,
            Optional<DependencyCollector> dependencyCollector, Optional<? extends ClassLoader> classLoader, 
            Optional<OrderSorter> orderSorter, Optional<LibraryDetector> libraryDetector, 
            GenerationRoots generationRoots) {
        return generate(javaClass, singleFileGenerator, getDependencyCollector(dependencyCollector),
                getClassLoader(classLoader), getOrderSorter(orderSorter), getLibraryDetector(libraryDetector),
                generationRoots, new GenerationUnit());
    }

    /**
     * @param dependencyCollector If no dependency collector is supplied the ByteCodeDependencyCollector and the 
     *                            JavaSourceDependencyCollector are used.
     * @param classLoader If no class loader is supplied Thread.currentThread().getContextClassLoader() is used.
     * @param orderSorter  If no order sorter is supplied the InheritanceOrderSorter is used.
     */    
    public GenerationUnit generate(Class<?> javaClass, SingleFileGenerator singleFileGenerator,
            Optional<DependencyCollector> dependencyCollector, Optional<? extends ClassLoader> classLoader, 
            Optional<OrderSorter> orderSorter, Optional<LibraryDetector> libraryDetector, 
            GenerationRoots generationRoots, GenerationUnit unit) {
        return generate(javaClass, singleFileGenerator, getDependencyCollector(dependencyCollector).get(),
                getClassLoader(classLoader).get(), getOrderSorter(orderSorter).get(), 
                getLibraryDetector(libraryDetector).get(), generationRoots, unit);
    }

    /**
     * @param dependencyCollector If no dependency collector is supplied the ByteCodeDependencyCollector and the 
     *                            JavaSourceDependencyCollector are used.
     * @param classLoader If no class loader is supplied Thread.currentThread().getContextClassLoader() is used.
     * @param orderSorter  If no order sorter is supplied the InheritanceOrderSorter is used.
     */    
    public GenerationUnit generateAll(Class<?> javaClass, SingleFileGenerator singleFileGenerator,
            Optional<DependencyCollector> dependencyCollector, Optional<? extends ClassLoader> classLoader, 
            Optional<OrderSorter> orderSorter, Optional<LibraryDetector> libraryDetector, GenerationRoots generationRoots) {
        return generateAll(Lists.<Class<?>>newArrayList(javaClass), singleFileGenerator, dependencyCollector, 
                classLoader, orderSorter, libraryDetector, generationRoots);
    }    
    
    /**
     * @param dependencyCollector If no dependency collector is supplied the ByteCodeDependencyCollector and the 
     *                            JavaSourceDependencyCollector are used.
     * @param classLoader If no class loader is supplied Thread.currentThread().getContextClassLoader() is used.
     * @param orderSorter  If no order sorter is supplied the InheritanceOrderSorter is used.
     */    
    public GenerationUnit generateAll(List<Class<?>> javaClasses, SingleFileGenerator singleFileGenerator,
            Optional<DependencyCollector> dependencyCollector, Optional<? extends ClassLoader> classLoader, 
            Optional<OrderSorter> orderSorter, Optional<LibraryDetector> libraryDetector, GenerationRoots generationRoots) {
        GenerationUnit unit = new GenerationUnit();
        for(int i = 1; i < javaClasses.size(); i++) {
            unit.addMissingDependencies(Sets.newHashSet(javaClasses.get(i).getName()));
        }
        
        unit = generate(javaClasses.get(0), singleFileGenerator, getDependencyCollector(dependencyCollector),
                getClassLoader(classLoader), getOrderSorter(orderSorter), getLibraryDetector(libraryDetector), 
                generationRoots, unit);

        while (!unit.isMissingDependenciesEmpty()) {
            Class nextJavaClass;

            try {
                nextJavaClass = classLoader.get().loadClass(unit.getNextDependency());
            } catch (ClassNotFoundException ex) {
                throw new IllegalStateException("Can't resolve the Java class '" + unit.getNextDependency() + "'!", ex);
            }

            unit = generate(nextJavaClass, singleFileGenerator, getDependencyCollector(dependencyCollector),
                    getClassLoader(classLoader), getOrderSorter(orderSorter), 
                    getLibraryDetector(libraryDetector), generationRoots, unit);
        }

        return unit;
    }

    private GenerationUnit generate(Class<?> javaClass, SingleFileGenerator singleFileGenerator,
            DependencyCollector dependencyCollector, ClassLoader classLoader, OrderSorter orderSorter,
            LibraryDetector libraryDetector, GenerationRoots generationRoots, GenerationUnit unit) {
		logger.info("Generating JavaScript for {}", javaClass.getName());
		
        JavaFileWithSource javaFile = new JavaFileWithSource<>(javaClass, generationRoots);
        checkArgument(!javaFile.isInnerClass(), "It's not possible to generate JavaScript for inner classes!");

        unit = new GenerationUnit(unit);

        unit.addVisitedClass(javaFile.getJavaClassName());
        unit.removeMissingDependencies(javaFile.getJavaClassName());
        Set<String> dependencies = getAllDependencies(javaFile, dependencyCollector, classLoader, singleFileGenerator, generationRoots);
		logger.debug("Dependencies: {}", Joiner.on(", ").join(dependencies));
        
        unit.getLibraryUrls().addAll(libraryDetector.detect(dependencies));

        SingleResult singleResult;
        try {
            singleResult = singleFileGenerator.generate(javaFile, classLoader, javaClassNamesToPackages(dependencies));
        } catch (SingleFileGeneratorException ex) {
            throw new IllegalStateException("An error occured while generating the file '" + javaFile + "'!", ex);
        }

        dependencies = removeJavaCoreClassNames(dependencies);
		logger.debug("Dependencies after removal of Java core classes: {}", Joiner.on(", ").join(dependencies));
        dependencies = singleFileGenerator.removeNotGeneratableJavaClasses(javaClass, dependencies, classLoader);
		logger.debug("Dependencies after removal of not generatable Java classes (by {}): {}", 
				singleFileGenerator.getClass().getSimpleName(), Joiner.on(", ").join(dependencies));
        
        unit.addGenerationResult(new GenerationResult(javaFile, new DateTime(), dependencies, singleResult), orderSorter);
        unit.addMissingDependencies(dependencies);

        return unit;
    }
	
	/**
	 * Collects the dependencies of the javaFile and all of its super classes (if generatable).
	 */
	private Set<String> getAllDependencies(JavaFileWithSource<?> javaFile, DependencyCollector dependencyCollector, ClassLoader classLoader, SingleFileGenerator singleFileGenerator, GenerationRoots generationRoots) {
		Set<String> dependencies = new HashSet<>();

		Class currentJavaClass = javaFile.getJavaClass();
		while (currentJavaClass != null) {
			dependencies.addAll(dependencyCollector.collect(javaFile, classLoader));

			currentJavaClass = currentJavaClass.getSuperclass();
			if (currentJavaClass != null) {
				// NOTE The check if the super class is generatable feels a bit hacky... but it works. ;-)
				Set<String> superClassGeneratableTest = Sets.newHashSet(currentJavaClass.getName());
				superClassGeneratableTest = removeJavaCoreClassNames(superClassGeneratableTest);
				superClassGeneratableTest = singleFileGenerator.removeNotGeneratableJavaClasses(currentJavaClass, superClassGeneratableTest, classLoader);

				if (superClassGeneratableTest.isEmpty()) {
					currentJavaClass = null;
				} else {
					javaFile = new JavaFileWithSource<>(currentJavaClass, generationRoots);
				}
			}
		}

		return dependencies;
	}

    private Optional<? extends ClassLoader> getClassLoader(Optional<? extends ClassLoader> classLoader) {
        if (classLoader.isPresent()) {
            return classLoader;
        } else {
            return Optional.of(Thread.currentThread().getContextClassLoader());
        }
    }

    private Optional<DependencyCollector> getDependencyCollector(Optional<DependencyCollector> dependencyCollector) {
        if (dependencyCollector.isPresent()) {
            return dependencyCollector;
        } else {
            return Optional.<DependencyCollector>of(new ByteCodeDependencyCollector(new JavaSourceDependencyCollector()));
        }
    }
    
    private Optional<OrderSorter> getOrderSorter(Optional<OrderSorter> orderSorter) {
        if(orderSorter.isPresent()) {
            return orderSorter;
        } else {
            return Optional.<OrderSorter>of(new InheritanceOrderSorter());
        }
    }
    
    private Optional<LibraryDetector> getLibraryDetector(Optional<LibraryDetector> libraryDetector) {
        if(libraryDetector.isPresent()) {
            return libraryDetector;
        } else {
            return Optional.<LibraryDetector>of(new LibraryDetector.None());
        }
    }    
}
