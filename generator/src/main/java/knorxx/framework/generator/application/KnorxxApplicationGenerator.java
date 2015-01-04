package knorxx.framework.generator.application;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import knorxx.framework.generator.GenerationRoots;
import knorxx.framework.generator.GenerationUnit;
import knorxx.framework.generator.library.LibraryDetector;
import knorxx.framework.generator.library.LibraryUrls;
import knorxx.framework.generator.reloading.annotation.Reloadable;
import knorxx.framework.generator.web.KnorxxApplication;
import knorxx.framework.generator.web.WebJavaScriptGenerator;
import knorxx.framework.generator.web.client.ErrorHandler;
import knorxx.framework.generator.web.client.WebPage;
import knorxx.framework.generator.web.client.webpage.PageArranger;
import knorxx.framework.generator.web.client.webpage.annotation.WebPageArranger;
import knorxx.framework.generator.web.server.json.JsonHelper;
import knorxx.framework.generator.web.server.rtti.RttiGenerationResult;
import knorxx.framework.generator.web.server.rtti.RttiGenerator;
import knorxx.framework.generator.web.server.rtti.UrlResolver;
import org.joda.time.DateTime;
import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

/**
 *
 * @author sj
 */
public class KnorxxApplicationGenerator {

	private final KnorxxApplication knorxxApplication;
	private final String allowedReloadPackage;
	private final String allowedGenerationPackage;
	private final List<Class<?>> webPageClasses;
	private final LibraryDetector libraryDetector;
	private final UrlResolver urlResolver;
	private final JsonHelper jsonHelper;
	private final knorxx.framework.generator.web.client.JsonHelper javaScriptJsonHelper;
	private final ErrorHandler javaScriptErrorHandler;
	private final String jsonRpcUrl;

	public KnorxxApplicationGenerator(KnorxxApplication knorxxApplication, String allowedReloadPackage, 
			String allowedGenerationPackage, List<Class<?>> webPageClasses, 
			LibraryDetector libraryDetector, UrlResolver urlResolver, JsonHelper jsonHelper, 
			knorxx.framework.generator.web.client.JsonHelper javaScriptJsonHelper, ErrorHandler javaScriptErrorHandler, 
			String jsonRpcUrl) {
		this.knorxxApplication = knorxxApplication;
		this.allowedReloadPackage = allowedReloadPackage;		
		this.allowedGenerationPackage = allowedGenerationPackage;
		this.webPageClasses = webPageClasses;
		this.libraryDetector = libraryDetector;	
		this.urlResolver = urlResolver;	
		this.jsonHelper = jsonHelper;
		this.javaScriptJsonHelper = javaScriptJsonHelper;
		this.javaScriptErrorHandler = javaScriptErrorHandler;
		this.jsonRpcUrl = jsonRpcUrl;
	}

	public ApplicationResult generateWebPage(GenerationRoots generationRoots, Class<?> webPageClass, 
			Optional<Collection<PageArranger>> pageArrangers, String contextPath, PopulatableCache populatableCache) throws IOException {
		ApplicationResult result = new ApplicationResult();
		result.setApplicationName(knorxxApplication.getName());

		result.setMainClassName(webPageClass.getName());

		result.setErrorHandlerClassName(javaScriptErrorHandler.getClass().getName());
		result.setJsonHelperClassName(javaScriptJsonHelper.getClass().getName());

		WebJavaScriptGenerator generator = new WebJavaScriptGenerator(generationRoots,
				allowedGenerationPackage, allowedReloadPackage, contextPath + "/",
				jsonRpcUrl.substring(1), libraryDetector);
		GenerationUnit unit = generator.generateAll(Lists.<Class<?>>newArrayList(webPageClass,
				javaScriptJsonHelper.getClass(), javaScriptErrorHandler.getClass()));

		result.setLibraryCssUrls(unit.getLibraryUrls().getCssUrls());
		result.setLibraryJavaScriptUrls(unit.getLibraryUrls().getJavaScriptUrls());

		RttiGenerator rttiGenerator = new RttiGenerator();
		RttiGenerationResult rttiGenerationResult = rttiGenerator.getJavaScriptSource(
				WebPage.class.getPackage().getName() + ".RunTimeTypeInformation",
				webPageClasses, urlResolver, new DateTime());

		LibraryUrls cacheUrls = populatableCache.populate(unit, rttiGenerationResult, urlResolver);
		result.setCssUrls(cacheUrls.getCssUrls());
		result.setJavaScriptUrls(cacheUrls.getJavaScriptUrls());

		result.setWebPageModelJson("{}");
		if(pageArrangers.isPresent()) {
			for (PageArranger pageArranger : pageArrangers.get()) {
				if (webPageClass.getAnnotation(WebPageArranger.class) != null
						&& webPageClass.getAnnotation(WebPageArranger.class).value().getName().equals(pageArranger.getClass().getName())) {
					Map<String, Object> webPageModel = new HashMap<>();
					pageArranger.initialize(webPageModel);
					result.setWebPageModelJson(jsonHelper.toJson(webPageModel));

					HtmlCanvas htmlCanvas = new HtmlCanvas();
					renderPageArranger(pageArranger, htmlCanvas, generator.getClassLoader());
					result.setPreRenderedHtml(htmlCanvas.toHtml());

					break;
				}
			}
		}

		return result;
	}

	private void renderPageArranger(Renderable pageArranger, HtmlCanvas htmlCanvas, ClassLoader classLoader) throws IOException {
		Method renderOnMethod = null;

		try {
			renderOnMethod = pageArranger.getClass().getMethod("renderOn", HtmlCanvas.class);
		} catch (NoSuchMethodException | SecurityException ex) {
			throw new IllegalStateException("Can't find the method renderOn(HtmlCanvas) of a PageArranger. "
					+ "This might be cause by an API change!");
		}

		boolean reloadableRenderOn = renderOnMethod.getAnnotation(Reloadable.class) != null;
		if (reloadableRenderOn) {
			try {
				final Object pageArrangerInstance = classLoader.loadClass(pageArranger.getClass().getName()).newInstance();
				pageArranger = new Renderable() {
					@Override
					public void renderOn(HtmlCanvas html) throws IOException {
						try {
							Method renderOnMethod = pageArrangerInstance.getClass().getMethod("renderOn", HtmlCanvas.class);
							renderOnMethod.invoke(pageArrangerInstance, html);
						} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
							throw new IllegalStateException("Can't call renderOn(HtmlCanvas) on the reloaded PageArranger instance!", ex);
						}
					}
				};
			} catch (SecurityException | InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
				throw new IllegalStateException("Can't instantiate a PageArranger with a renderOn(HtmlCanvas) "
						+ "method marked with @Reloadable! Does it have a default constructor?");
			}
		}

		pageArranger.renderOn(htmlCanvas);
	}
}
