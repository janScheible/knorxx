package knorxx.framework.generator.dependency.testclass.sourceandbytecode;

import java.awt.Color;

/**
 *
 * @author sj
 */
public class All {
    
    public class Inner {
        
        public Color doIt() {
            System.out.println(ConstantHolder.TEST_STRING);
            return Color.RED;
        }        
    }
}
