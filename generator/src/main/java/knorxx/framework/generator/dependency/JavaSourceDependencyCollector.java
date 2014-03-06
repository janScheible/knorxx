package knorxx.framework.generator.dependency;

import com.google.common.base.Optional;
import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import knorxx.framework.generator.JavaFile;
import knorxx.framework.generator.JavaFileOnClasspath;
import static knorxx.framework.generator.util.JavaIdentifierUtils.getPackageName;
import static knorxx.framework.generator.util.JavaIdentifierUtils.isValidClassName;
import static knorxx.framework.generator.util.JavaIdentifierUtils.isValidConstantName;

/**
 *
 * @author sj
 */
public class JavaSourceDependencyCollector extends DependencyCollector {

    public JavaSourceDependencyCollector() {
    }

    public JavaSourceDependencyCollector(DependencyCollector nextCollector) {
        super(nextCollector);
    }

    @Override
    public Set<String> collect(JavaFileOnClasspath<?> javaFile, ClassLoader classLoader) {
        throw new UnsupportedOperationException("Can't work on Java files without source!");
    }

    @Override
    protected Set<String> collectInternal(JavaFile<?> javaFile, final ClassLoader classLoader) {
        final Set<String> result = new TreeSet<>();

        Optional<InputStream> sourceInputStream = javaFile.getSourceInputStream();
        if (sourceInputStream.isPresent()) {
            try (InputStream input = sourceInputStream.get()) {
                CompilationUnit compilationUnit = JavaParser.parse(input);

                new VoidVisitorAdapter() {
                    
                    private String packageName;
                    private Set<String> importedClassNames = new HashSet<>();
                    private List<String> asteriskPackages = new ArrayList<>();

                    @Override
                    public void visit(PackageDeclaration packageDeclaration, Object arg) {
                        packageName = packageDeclaration.getName().toString();
                        asteriskPackages.add(packageName);
                        super.visit(packageDeclaration, arg);
                    }

                    @Override
                    public void visit(ImportDeclaration importDeclaration, Object arg) {
                        String className = importDeclaration.getName().toString();
                       
                        if (importDeclaration.isStatic()) {
                            if (importDeclaration.isAsterisk()) {
                                importedClassNames.add(className);
                                result.add(className);
                            } else {
                                // actually only removing the static part
                                String classNameWithoutStaticPart = getPackageName(className);
                                importedClassNames.add(classNameWithoutStaticPart); 
                                result.add(classNameWithoutStaticPart);
                            }
                        } else if(importDeclaration.isAsterisk()) {
                            if (!asteriskPackages.contains(className)) {
                                asteriskPackages.add(className);
                            }
                        } else if (isValidClassName(className)) {
                            importedClassNames.add(className);
                        }

                        super.visit(importDeclaration, arg);
                    }

                    @Override
                    public void visit(FieldAccessExpr fieldAccessExpr, Object arg) {
                        if (isValidConstantName(fieldAccessExpr.getField())) {
                            String className = fieldAccessExpr.getScope().toString();

                            if (className.contains(".")) {
                                if (isValidClassName(className)) {
                                    result.add(className);
                                }
                            } else {
                                String simpleClassName = className;
                                boolean resolved = false;

                                if (isValidClassName(simpleClassName)) {
                                    // 1. try to find the class in the list of imports 
                                    for (String importedClassName : importedClassNames) {
                                        if (importedClassName.endsWith("." + simpleClassName)) {
                                            result.add(importedClassName);
                                            resolved = true;
                                            break;
                                        }
                                    }

                                    if (!resolved) {
                                        for (String asteriskPackage : asteriskPackages) {
                                            // 2. check if it is a class in the same package
                                            try {
                                                String classNameWithCurrentPackage = asteriskPackage + "." + simpleClassName;
                                                Class.forName(classNameWithCurrentPackage, false, classLoader);
                                                result.add(classNameWithCurrentPackage);
                                                break;
                                            } catch (ClassNotFoundException ex) {
                                                // just do nothing... we are simply not able to resolve the class name... :-(
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        super.visit(fieldAccessExpr, arg);
                    }
                }.visit(compilationUnit, null);
            } catch (Exception ex) {
                throw new IllegalStateException("Can't parse and analyze the Java source in '" + javaFile + "'!", ex);
            }
        }

        return result;
    }
}
