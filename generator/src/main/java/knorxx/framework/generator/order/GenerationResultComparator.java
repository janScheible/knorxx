package knorxx.framework.generator.order;

import java.util.Comparator;
import java.util.List;
import knorxx.framework.generator.NamedResult;

/**
 *
 * @author sj
 */
public class GenerationResultComparator implements Comparator<NamedResult> {
    
    private final List<String> sortedJavaClassNames;

    public GenerationResultComparator(List<String> sortedJavaClassNames) {
        this.sortedJavaClassNames = sortedJavaClassNames;
    }

    @Override
    public int compare(NamedResult first, NamedResult second) {
        boolean containsFirst = sortedJavaClassNames.contains(first.getName());
        boolean containsSecond = sortedJavaClassNames.contains(second.getName());
        
        if (!containsFirst && !containsSecond) {
            return 0;
        } else if (containsFirst && !containsSecond) {
            return 1;
        } else if (!containsFirst && containsSecond) {
            return -1;
        } else {
            int firstIndex = sortedJavaClassNames.indexOf(first.getName());
            int secondIndex = sortedJavaClassNames.indexOf(second.getName());
            
            return firstIndex < secondIndex ? -1 : 1;
        }
    }    
}
