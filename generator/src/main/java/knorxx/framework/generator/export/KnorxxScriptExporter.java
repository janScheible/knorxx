package knorxx.framework.generator.export;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import knorxx.framework.generator.GenerationRoots;
import knorxx.framework.generator.GenerationUnit;
import knorxx.framework.generator.application.KnorxxApplicationGenerator;
import knorxx.framework.generator.application.PopulatableCache;
import knorxx.framework.generator.library.LibraryDetector;
import knorxx.framework.generator.library.LibraryUrls;
import knorxx.framework.generator.web.KnorxxApplication;
import knorxx.framework.generator.web.client.ErrorHandler;
import knorxx.framework.generator.web.client.WebPage;
import knorxx.framework.generator.web.client.webpage.PageArranger;
import knorxx.framework.generator.web.server.json.JsonHelper;
import knorxx.framework.generator.web.server.rpc.ExceptionMarshaller;
import knorxx.framework.generator.web.server.rtti.RttiGenerationResult;
import knorxx.framework.generator.web.server.rtti.UrlResolver;
import org.reflections.Reflections;

/**
 *
 * @author sj
 */
public abstract class KnorxxScriptExporter {
	
	KnorxxApplicationGenerator applicationGenerator;
	
	public KnorxxScriptExporter(Class<?> applicationRootClass, Class<?> javaScriptGenerationRoot, Class<?> indexWebPageClass, 
            LibraryDetector libraryDetector, ExceptionMarshaller exceptionMarshaller) {
		Reflections reflections = new Reflections();
		
		applicationGenerator = new KnorxxApplicationGenerator(
				getSingleton(KnorxxApplication.class, reflections),
				applicationRootClass.getPackage().getName(),
				javaScriptGenerationRoot.getPackage().getName(),
				getWebPageClasses(reflections), 
				libraryDetector, 
				new UrlResolver("prefix", javaScriptGenerationRoot.getPackage().getName()),
				getSingleton(JsonHelper.class, reflections),
				getSingleton(knorxx.framework.generator.web.client.JsonHelper.class, reflections), 
				getSingleton(ErrorHandler.class, reflections),
				"/jsonRpcUrl");
	}
	
	private <T> T getSingleton(Class<T> javaClass, Reflections reflections) {
		Set<Class<? extends T>> subClasses = reflections.getSubTypesOf(javaClass);
		
		int allowedResultCount = 1;
		
		T instance = null;
		
		for (Class<? extends T> subClass : subClasses) {
			if (!(subClass.equals(javaClass) || subClass.equals(knorxx.framework.generator.web.server.json.GsonHelper.class))) {
				try {
					instance = subClass.getConstructor().newInstance();
				} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException |
						IllegalArgumentException | InvocationTargetException ex) {
					throw new IllegalStateException(ex);
				}
			} else {
				allowedResultCount++;
			}
		}

		if(subClasses.size() != allowedResultCount) {
			throw new IllegalStateException("Can't instantiate a singleton of '" + javaClass.getName() + 
					"' because the following subclasses were found on the classpath: " + Joiner.on(", ").join(subClasses));
		}
		
		return instance;
	}
	
	private List<Class<?>> getWebPageClasses(Reflections reflections) {
		List<Class<?>> result = new ArrayList<>();
		
		for(Class webPageClass : reflections.getSubTypesOf(WebPage.class)) {
			result.add(webPageClass);
		}
		
		return result;
	}
	
	protected ExportResult export(GenerationRoots generationRoots, Class<?> webPageClass) {
		final AtomicReference<GenerationUnit> generationUnitRef = new AtomicReference<>();
		final AtomicReference<RttiGenerationResult> rttiGenerationResultRef = new AtomicReference<>();
		final AtomicReference<UrlResolver> urlResolverRef = new AtomicReference<>();
		
		PopulatableCache populatableCache = new PopulatableCache() {
			@Override
			public LibraryUrls populate(GenerationUnit unit, RttiGenerationResult rttiGenerationResult, UrlResolver urlResolver) {
				generationUnitRef.set(unit);
				rttiGenerationResultRef.set(rttiGenerationResult);
				urlResolverRef.set(urlResolver);
				
				return new LibraryUrls();
			}
		};
		
		try {
			return new ExportResult(applicationGenerator.generateWebPage(generationRoots, webPageClass, 
					Optional.<Collection<PageArranger>>absent(), "/context", populatableCache), generationUnitRef.get(),
					rttiGenerationResultRef.get(), urlResolverRef.get());
		} catch (IOException ex) {
			throw new IllegalStateException(ex);
		}
	}	
}
