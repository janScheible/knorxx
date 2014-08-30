package knorxx.framework.generator.web;

import com.google.common.base.Optional;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Entity;
import knorxx.framework.generator.GenerationRoots;
import knorxx.framework.generator.GenerationUnit;
import knorxx.framework.generator.JavaScriptGenerator;
import knorxx.framework.generator.dependency.DependencyCollector;
import knorxx.framework.generator.library.LibraryDetector;
import knorxx.framework.generator.order.InheritanceOrderSorter;
import knorxx.framework.generator.order.OrderSorter;
import knorxx.framework.generator.order.StaticUsageOrderSorter;
import knorxx.framework.generator.reloading.AnnotationDescription;
import knorxx.framework.generator.reloading.ClassAnnotationDescription;
import knorxx.framework.generator.reloading.MethodAnnotationDescription;
import knorxx.framework.generator.reloading.ReloadPredicate;
import knorxx.framework.generator.reloading.ReloadingClassLoader;
import knorxx.framework.generator.single.StjsSingleFileGenerator;
import static knorxx.framework.generator.util.JavaIdentifierUtils.hasSuperclassOrImplementsInterface;
import knorxx.framework.generator.web.client.WebPage;
import knorxx.framework.generator.web.client.messagequeue.annotation.QueueMessage;
import knorxx.framework.generator.web.generator.CssDefinitionFileGenerator;
import knorxx.framework.generator.web.generator.MessageQueueFileGenerator;
import knorxx.framework.generator.web.generator.RpcServiceFileGenerator;
import knorxx.framework.generator.web.generator.annotation.OmitNamespace;
import org.stjs.javascript.annotation.Namespace;
import org.stjs.javascript.annotation.STJSBridge;
import org.stjs.javascript.annotation.SyntheticType;
import org.stjs.javascript.annotation.Template;

/**
 *
 * @author sj
 */
public class WebJavaScriptGenerator {

    private final GenerationRoots generationRoots;
    private final String allowedGenerationPackage;
    private final String allowedReloadPackage;
    private final WebSingleFileGenerator webSingleFileGenerator;
    private final ClassLoader classLoader;
    private final LibraryDetector libraryDetector;
    
    private final JavaScriptGenerator javaScriptGenerator = new JavaScriptGenerator();
    private final OrderSorter orderSorter = new InheritanceOrderSorter(new StaticUsageOrderSorter());

    public WebJavaScriptGenerator(GenerationRoots generationRoots, final String allowedGenerationPackage, 
            final String allowedReloadPackage, String csrfProtectionCookiePath, String rpcUrl, 
            LibraryDetector libraryDetector) {
        this.generationRoots = generationRoots;
        this.allowedGenerationPackage = allowedGenerationPackage;
        this.allowedReloadPackage = allowedReloadPackage;
        this.libraryDetector = libraryDetector;

        this.classLoader = createClassLoader();
        this.webSingleFileGenerator = new WebSingleFileGenerator(new StjsSingleFileGenerator(), allowedGenerationPackage,
                new CssDefinitionFileGenerator(), new MessageQueueFileGenerator(), 
                new RpcServiceFileGenerator(csrfProtectionCookiePath, rpcUrl));
    }

    private boolean isDataTransferObject(Class<?> javaClass) {
        return (javaClass.getAnnotation(QueueMessage.class) != null
                || javaClass.getAnnotation(Entity.class) != null);
    }

    private boolean isAllowedClass(Class<?> javaClass) {
        return javaClass.getPackage().getName().startsWith(allowedReloadPackage);
    }

    private boolean isServerSideClass(Class<?> javaClass) {
        return javaClass.getPackage().getName().startsWith(allowedReloadPackage)
                && !javaClass.getPackage().getName().startsWith(allowedGenerationPackage);
    }

    private boolean isClientSideClass(Class<?> javaClass) {
        return javaClass.getPackage().getName().startsWith(allowedReloadPackage)
                && javaClass.getPackage().getName().startsWith(allowedGenerationPackage);
    }

    private ClassLoader createClassLoader() {
        List<AnnotationDescription> annotationDescriptions = new ArrayList<>();

        annotationDescriptions.add(new ClassAnnotationDescription(Namespace.class) {
            @Override
            public boolean isApplicable(Class<?> javaClass, String memberName) {
				Annotation omitNamespace = javaClass.getAnnotation(OmitNamespace.class);
                return isAllowedClass(javaClass) && omitNamespace == null;
            }

            @Override
            public Map<String, Object> getValues(Class<?> javaClass, String memberName) {
                Map<String, Object> result = new HashMap<>();
                result.put("value", javaClass.getPackage().getName());
                return result;
            }
        });

        annotationDescriptions.add(new ClassAnnotationDescription(SyntheticType.class) {
            @Override
            public boolean isApplicable(Class<?> javaClass, String memberName) {
                if (isServerSideClass(javaClass) && isDataTransferObject(javaClass)) {
                    return true;
                } else {
                    return false;
                }
            }
        });

        annotationDescriptions.add(new ClassAnnotationDescription(STJSBridge.class) {
            @Override
            public boolean isApplicable(Class<?> javaClass, String memberName) {
                if(isServerSideClass(javaClass) && !isDataTransferObject(javaClass)) {
                    return true;
                } else if (isClientSideClass(javaClass)) {
                    if (hasSuperclassOrImplementsInterface(javaClass, WebPage.class.getName())
                            && !Modifier.isAbstract(javaClass.getModifiers())) {
                        return true;
                    }
                }

                return false;
            }
        });

        Map<String, Object> propertyAccessTemplateValues = new HashMap<>();
        propertyAccessTemplateValues.put("value", "propertyAccess");
        annotationDescriptions.add(new MethodAnnotationDescription(Template.class, propertyAccessTemplateValues) {
            @Override
            public boolean isApplicable(Class<?> javaClass, String memberName) {
                if (isServerSideClass(javaClass) && isDataTransferObject(javaClass)) {
                    return memberName.startsWith("get") || memberName.startsWith("set");
                } else {
                    return false;
                }
            }
        });

        return new ReloadingClassLoader(generationRoots, Thread.currentThread().getContextClassLoader(),
                annotationDescriptions, Optional.of(new ReloadPredicate.AllowedPackage(allowedReloadPackage)));
    }

    public GenerationUnit generateAll(Class<?> javaClass) {
        return javaScriptGenerator.generateAll(javaClass, webSingleFileGenerator, Optional.<DependencyCollector>absent(),
                Optional.of(classLoader), Optional.of(orderSorter), Optional.of(libraryDetector), generationRoots);
    }
    
    public GenerationUnit generateAll(List<Class<?>> javaClasses) {
        return javaScriptGenerator.generateAll(javaClasses, webSingleFileGenerator, Optional.<DependencyCollector>absent(),
                Optional.of(classLoader), Optional.of(orderSorter), Optional.of(libraryDetector), generationRoots);
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }
}
