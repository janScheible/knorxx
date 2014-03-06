package knorxx.framework.generator.order;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.TopologicalOrderIterator;

/**
 *
 * @author sj
 */
public abstract class OrderSorter {
    
    private Optional<OrderSorter> nextCollector = Optional.absent();

    public OrderSorter() {
    }

    public OrderSorter(OrderSorter nextCollector) {
        this.nextCollector = Optional.of(nextCollector);
    }
    
    public Set<OrderConstraint> getConstraints(List<Class<?>> classes) {
        Set<OrderConstraint> result = getConstraintsInternal(classes);

        if (nextCollector.isPresent()) {
            result.addAll(nextCollector.get().getConstraintsInternal(classes));
        }

        return result;
    }

    protected abstract Set<OrderConstraint> getConstraintsInternal(List<Class<?>> classes);
    
    public List<String> sort(List<Class<?>> classes) throws OrderSortCycleException {
        List<String> result = new ArrayList<>();
        SimpleDirectedGraph<String, DefaultEdge> graph = new SimpleDirectedGraph<>(DefaultEdge.class);
        
        for (OrderConstraint orderConstraint : getConstraints(classes)) {
            graph.addVertex(orderConstraint.getFirst());
            graph.addVertex(orderConstraint.getSecond());
            
            graph.addEdge(orderConstraint.getFirst(), orderConstraint.getSecond());
        }
        
        CycleDetector<String, DefaultEdge> cycleDetector = new CycleDetector<>(graph);
        if(!cycleDetector.detectCycles()) {
                        for (TopologicalOrderIterator<String, DefaultEdge> iterator = new TopologicalOrderIterator<>(graph); iterator.hasNext();) {
                result.add(iterator.next());
            }
        } else {
            String cycles = Joiner.on(", ").join(cycleDetector.findCycles());
            
            throw new OrderSortCycleException("The given order constraints contain (at least one) cycle. Cycles can only "
                    + "be caused by static references because we have single inherintance only in Java (involved classes: '" + cycles + "').");
        }

        return result;
    }    
}
