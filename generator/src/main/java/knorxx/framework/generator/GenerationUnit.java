package knorxx.framework.generator;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import knorxx.framework.generator.library.LibraryUrls;
import knorxx.framework.generator.order.GenerationResultComparator;
import knorxx.framework.generator.order.OrderSortCycleException;
import knorxx.framework.generator.order.OrderSorter;

/**
 *
 * @author sj
 */
public class GenerationUnit {
    
    private final List<GenerationResult> generationResults = new ArrayList<>();
    private final Set<String> visitedClasses = new HashSet<>();
    private final Set<String> missingDependencies = new HashSet<>();
    private final LibraryUrls libraryUrls = new LibraryUrls();
    
    /* package */ GenerationUnit(GenerationUnit other) {
        this.generationResults.addAll(other.generationResults);
        this.visitedClasses.addAll(other.visitedClasses);
        this.missingDependencies.addAll(other.missingDependencies);
        this.libraryUrls.addAll(other.libraryUrls);
    }

    /* package */ GenerationUnit() {
    }
    
    /* package */ void addGenerationResult(GenerationResult generationResult, OrderSorter orderSorter) {
        generationResults.add(generationResult);
        
        List<Class<?>> javaClasses = new ArrayList<>();
        for(GenerationResult currentGenerationResult : generationResults) {
            javaClasses.add(currentGenerationResult.getJavaFile().getJavaClass());
        }
        
        List<String> sortedJavaClassNames;
        try {
            sortedJavaClassNames = orderSorter.sort(javaClasses);
        } catch (OrderSortCycleException ex) {
            throw new IllegalStateException(ex);
        }
        
        Collections.sort(generationResults, new GenerationResultComparator(sortedJavaClassNames));
    }

    public List<GenerationResult> getGenerationResults() {
        return ImmutableList.copyOf(generationResults);
    }
    
    public Optional<GenerationResult> getGenerationResult(String javaClassName) {
        for (GenerationResult generationResult : generationResults) {
            if (generationResult.getJavaFile().getJavaClassName().equals(javaClassName)) {
                return Optional.of(generationResult);
            }
        }
        
        return Optional.absent();
    }
    
    public void addVisitedClass(String javaClassName) {
        visitedClasses.add(javaClassName);
    }
    
    public boolean visitedClassesContains(String javaClassName) {
        return visitedClasses.contains(javaClassName);
    }
    
    public Set<String> getMissingDependencies() {
        return ImmutableSet.copyOf(missingDependencies);
    }
    
    public void addMissingDependencies(Set<String> missingDependencies) {
        missingDependencies = new HashSet<>(missingDependencies);
        missingDependencies.removeAll(visitedClasses);
        this.missingDependencies.addAll(missingDependencies);
    }
    
    public void removeMissingDependencies(String javaClassName) {
        missingDependencies.remove(javaClassName);
    }
    
    public boolean isMissingDependenciesEmpty() {
        return missingDependencies.isEmpty();
    }

    public String getNextDependency() {
        return missingDependencies.iterator().next();
    }

    public LibraryUrls getLibraryUrls() {
        return libraryUrls;
    }

    @Override
    public String toString() {
        return "[" + Joiner.on(", ").join(generationResults) + "]";
    }
}
