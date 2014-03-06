package knorxx.framework.generator.dependency.testclass.bytecode;

import java.awt.Rectangle;

/**
 *
 * @author sj
 */
public class AnonymousInnerClass {

    public void test() {
        System.out.println(new Runnable() {
            
            public Rectangle rectangle;

            @Override
            public void run() {
            }
        });
    }
}
