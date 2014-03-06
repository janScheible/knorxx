package knorxx.framework.generator.order;

import knorxx.framework.generator.order.OrderSortCycleException;
import knorxx.framework.generator.order.InheritanceOrderSorter;
import knorxx.framework.generator.order.OrderSorter;
import knorxx.framework.generator.order.OrderConstraint;
import knorxx.framework.generator.order.StaticUsageOrderSorter;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import knorxx.framework.generator.order.testclass.inheritance.SubClass;
import knorxx.framework.generator.order.testclass.inheritance.SuperClass;
import knorxx.framework.generator.order.testclass.inheritance.SuperInterface;
import knorxx.framework.generator.order.testclass.staticusage.Client;
import knorxx.framework.generator.order.testclass.staticusage.ClientCycle;
import knorxx.framework.generator.order.testclass.staticusage.ClientSubClass;
import knorxx.framework.generator.order.testclass.staticusage.Parent;
import knorxx.framework.generator.order.testclass.staticusage.ParentCycle;
import knorxx.framework.generator.order.testclass.staticusage.ParentNull;
import knorxx.framework.generator.order.testclass.staticusage.ParentSubClass;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.number.OrderingComparison.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class OrderSorterTest {

    @Test
    public void sortInheritanceConstraints() throws OrderSortCycleException {
        ArrayList<Class<?>> classes = Lists.newArrayList(new Class<?>[]{SubClass.class, SuperClass.class, SuperInterface.class});
        OrderSorter orderSorter = new InheritanceOrderSorter();
        List<String> result = orderSorter.sort(classes);
        
        assertThat(result.indexOf(SuperClass.class.getName()), 
                is(lessThan(result.indexOf(SubClass.class.getName()))));
        assertThat(result.indexOf(SuperInterface.class.getName()), 
                is(lessThan(result.indexOf(SuperClass.class.getName()))));        
        assertThat(result.indexOf(SuperInterface.class.getName()), 
                is(lessThan(result.indexOf(SubClass.class.getName()))));
    }
    
    @Test(expected = OrderSortCycleException.class)
    public void sortStaticReferenceCycleError() throws OrderSortCycleException {
        ArrayList<Class<?>> classes = Lists.newArrayList(new Class<?>[]{ParentCycle.class, ClientCycle.class});
        OrderSorter orderSorter = new StaticUsageOrderSorter();
        orderSorter.sort(classes);
    }

    @Test
    public void staticUsage() {
        OrderSorter orderSorter = new StaticUsageOrderSorter();
        Set<OrderConstraint> constraints = orderSorter.getConstraints(Lists.newArrayList(new Class<?>[]{
            Parent.class, Client.class }));
        
        assertEquals(constraints.size(), 1);
        assertThat(constraints, containsConstraint(Client.class, Parent.class));
    }

    @Test
    public void staticUsageNull() {
        OrderSorter orderSorter = new StaticUsageOrderSorter();
        Set<OrderConstraint> constraints = orderSorter.getConstraints(Lists.newArrayList(new Class<?>[]{
            ParentNull.class, Client.class }));
        
        assertEquals(0, constraints.size());
    }    
    
    @Test
    public void staticUsageSubClass() {
        OrderSorter orderSorter = new StaticUsageOrderSorter();
        Set<OrderConstraint> constraints = orderSorter.getConstraints(Lists.newArrayList(new Class<?>[]{
            ParentSubClass.class, Client.class, ClientSubClass.class }));
        
        assertEquals(constraints.size(), 1);
        assertThat(constraints, containsConstraint(ClientSubClass.class, ParentSubClass.class));
    }    

    @Test
    public void getInheritanceConstraints() {
        OrderSorter orderSorter = new InheritanceOrderSorter();
        Set<OrderConstraint> constraints = orderSorter.getConstraints(Lists.newArrayList(new Class<?>[]{
            SubClass.class, SuperClass.class, SuperInterface.class }));
        
        assertEquals(2, constraints.size());
        assertThat(constraints, containsConstraint(SuperClass.class, SubClass.class));
        assertThat(constraints, containsConstraint(SuperInterface.class, SuperClass.class));
    }
    
    private BaseMatcher containsConstraint(final Class<?> first, final Class<?> second) {
        return new BaseMatcher() {

            @Override
            public boolean matches(Object item) {
                Set<OrderConstraint> constraints = (Set<OrderConstraint>) item;
                for(OrderConstraint constraint : constraints) {
                    if(constraint.getFirst().equals(first.getName()) && constraint.getSecond().equals(second.getName())) {
                        return true;
                    }
                }
                
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("'" + first.getName() + "' before '" + second.getName() + "'");
            }
        };
    }
}