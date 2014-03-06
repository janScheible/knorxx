package knorxx.framework.generator.order;

import java.util.List;
import java.util.Set;

/**
 *
 * @author sj
 */
public class InheritanceOrderSorter extends OrderSorter {

    public InheritanceOrderSorter() {
    }

    public InheritanceOrderSorter(OrderSorter nextCollector) {
        super(nextCollector);
    }

    @Override
    protected Set<OrderConstraint> getConstraintsInternal(List<Class<?>> classes) {
        OrderingResult result = new OrderingResult(classes);

        for (Class<?> javaClass : classes) {
            Class<?> currentJavaClass = javaClass;

            while (currentJavaClass != null) {
                String currentClassName = currentJavaClass.getName();

                for (Class<?> currentClassInterface : currentJavaClass.getInterfaces()) {
                    result.addConstraint(currentClassInterface.getName(), currentClassName);
                }

                currentJavaClass = currentJavaClass.getSuperclass();

                if (currentJavaClass != null) {
                    String superClassName = currentJavaClass.getName();
                    result.addConstraint(superClassName, currentClassName);
                }
            }
        }

        return result.getConstraints();

    }
}
