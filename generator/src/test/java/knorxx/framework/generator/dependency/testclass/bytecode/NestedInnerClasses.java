package knorxx.framework.generator.dependency.testclass.bytecode;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author sj
 */
public class NestedInnerClasses {
    
    public Color color;
    
    public class FirstInner {

        public Rectangle rectangle;
        
        public class SecondInner {
            
            public Point point;
        }        
    }
}
