package knorxx.framework.generator.order;

import com.google.common.base.Objects;
import java.util.List;

/**
 *
 * @author sj
 */
public class OrderConstraint {
    
    private final String first;
    private final String second;

    public OrderConstraint(String first, String second) {
        this.first = first;
        this.second = second;
    }
    
    public boolean evaluate(List<String> list) {
        return list.indexOf(first) < list.indexOf(second);
    }

    public String getFirst() {
        return first;
    }

    public String getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "'" + first + "' before '" + second + "'";
    }    

    @Override
    public int hashCode() {
        return Objects.hashCode(first, second);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof OrderConstraint) {
            OrderConstraint other = (OrderConstraint) obj;
            return Objects.equal(first, other.first) && Objects.equal(second, other.second);
        } else {
            return false;
        }
    }
}
