package knorxx.framework.generator.dependency.testclass.bytecode;

/**
 *
 * @author sj
 */
public class AnonymousInnerClassWithStaticHelperInSamePackage {
 
    public void test() {
        System.out.println(new Runnable() {
            
            @Override
            public void run() {
                StaticHelperClass.doIt();
            }
        });
    }
}
