package knorxx.framework.generator.dependency.testclass.bytecode;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

/**
 *
 * @author sj
 */
public class NestedStaticInnerClasses {
    
    public Color color;
    
    public static class FirstStaticInner {

        public Rectangle rectangle;
        
        public static class SecondStaticInner {
            
            public Point point;
        }        
    }
}
