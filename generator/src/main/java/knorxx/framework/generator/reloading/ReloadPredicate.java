package knorxx.framework.generator.reloading;

/**
 *
 * @author sj
 */
public abstract class ReloadPredicate {

    public abstract boolean apply(Class<?> javaClass, boolean hasSource);

    public static class AllowedPackage extends ReloadPredicate {

        private final String packageName;

        public AllowedPackage(String packageName) {
            this.packageName = packageName + ".";
        }

        @Override
        public boolean apply(Class<?> javaClass, boolean hasSource) {
            return javaClass.getName().startsWith(packageName);
        }
    }
    
    public static class HasSource extends ReloadPredicate {

        @Override
        public boolean apply(Class<?> javaClass, boolean hasSource) {
            return hasSource;
        }
    }
}
