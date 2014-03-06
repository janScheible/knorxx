package knorxx.framework.generator.order;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author sj
 */
/* package */ class OrderingResult {

    private final Set<OrderConstraint> constraints = new HashSet<>();
    private final Map<String, Class<?>> classLookup = new HashMap<>();

    /* package */ OrderingResult(List<Class<?>> classes) {
        for (Class<?> javaClass : classes) {
            classLookup.put(javaClass.getName(), javaClass);
        }
    }

    /* package */ void addConstraint(String first, String second) {
        if (classLookup.containsKey(first)
                && classLookup.containsKey(second)) {
            constraints.add(new OrderConstraint(first, second));
        }
    }

    /* package */ Set<OrderConstraint> getConstraints() {
        return constraints;
    }
}