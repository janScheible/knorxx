package knorxx.framework.generator.dependency;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import knorxx.framework.generator.JavaFile;
import knorxx.framework.generator.JavaFileOnClasspath;
import knorxx.framework.generator.util.JavaIdentifierUtils;
import static knorxx.framework.generator.util.JavaIdentifierUtils.getPackageName;
import static knorxx.framework.generator.util.JavaIdentifierUtils.isValidClassName;
import net.sourceforge.pmd.lang.ParserOptions;
import net.sourceforge.pmd.lang.java.Java18Parser;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTImportDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPackageDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryPrefix;
import net.sourceforge.pmd.lang.java.ast.JavaParserVisitorAdapter;

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
                ASTCompilationUnit compilationUnit = (ASTCompilationUnit) new Java18Parser(
                        new ParserOptions()).parse("", new InputStreamReader(input, Charsets.UTF_8));

                new JavaParserVisitorAdapter() {

                    private String packageName;
                    private Set<String> importedClassNames = new HashSet<>();
                    private List<String> asteriskPackages = new ArrayList<>();                

                    @Override
                    public Object visit(ASTPackageDeclaration node, Object data) {
                        packageName = node.getPackageNameImage();
                        asteriskPackages.add(packageName);
                        
                        return super.visit(node, data);
                    }

                    @Override
                    public Object visit(ASTImportDeclaration node, Object data) {
                        String className = node.getImportedName();
                        boolean isStatic = node.isStatic();
                        boolean isAsterisk = node.isImportOnDemand();
                        
                        if (isStatic) {
                            if (isAsterisk) {
                                importedClassNames.add(className);
                                result.add(className);
                            } else {
                                // actually only removing the static part
                                String classNameWithoutStaticPart = getPackageName(className);
                                importedClassNames.add(classNameWithoutStaticPart); 
                                result.add(classNameWithoutStaticPart);
                            }
                        } else if(isAsterisk) {
                            if (!asteriskPackages.contains(className)) {
                                asteriskPackages.add(className);
                            }
                        } else if (isValidClassName(className)) {
                            importedClassNames.add(className);
                        }
                        
                        return super.visit(node, data);
                    }                   
                    
                    @Override
                    public Object visit(ASTPrimaryPrefix node, Object data) {
                        ASTName name = node.getFirstChildOfType(ASTName.class);
                        
                        if(name != null) {
                            String fieldAccess = name.getImage();

                            if (fieldAccess.contains(".")) {
                                String fieldName = JavaIdentifierUtils.getJavaClassSimpleName(fieldAccess);

                                if (JavaIdentifierUtils.isValidConstantName(fieldName)) {
                                    String className = JavaIdentifierUtils.getPackageName(fieldAccess);

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
                            }
                        }
                        
                        return super.visit(node, data);
                    }
                }.visit(compilationUnit, null);
            } catch (Exception ex) {
                throw new IllegalStateException("Can't parse and analyze the Java source in '" + javaFile + "'!", ex);
            }
        }

        return result;
    }    
}
