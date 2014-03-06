package knorxx.framework.generator.order;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;

/**
 *
 * @author sj
 */
public class StaticUsageOrderSorter extends OrderSorter {

    public StaticUsageOrderSorter() {
    }

    public StaticUsageOrderSorter(OrderSorter nextCollector) {
        super(nextCollector);
    }

    @Override
    protected Set<OrderConstraint> getConstraintsInternal(List<Class<?>> classes) {
        OrderingResult result = new OrderingResult(classes);

        for (Class<?> javaClass : classes) {
            String javaClassName = javaClass.getName();

            for (Field field : javaClass.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    try {
                        field.setAccessible(true);
                        Object value = field.get(null);

                        if (value != null) {
                            String referencedValueClassName = value.getClass().getName();
                            result.addConstraint(referencedValueClassName, javaClassName);
                        }
                    } catch (IllegalArgumentException | IllegalAccessException ex) {
                        throw new IllegalStateException("Error while analyzing the static fields of '" + javaClass.getName() + "'!", ex);
                    }
                }
            }
        }

        return result.getConstraints();
    }
}
