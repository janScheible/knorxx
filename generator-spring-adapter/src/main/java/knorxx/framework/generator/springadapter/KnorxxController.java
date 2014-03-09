package knorxx.framework.generator.springadapter;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import knorxx.framework.generator.GenerationResult;
import knorxx.framework.generator.GenerationRoots;
import knorxx.framework.generator.GenerationUnit;
import knorxx.framework.generator.library.LibraryDetector;
import knorxx.framework.generator.library.LibraryUrls;
import knorxx.framework.generator.single.JavaScriptResult;
import static knorxx.framework.generator.springadapter.CacheRequestType.*;
import static knorxx.framework.generator.springadapter.KnorxxGeneratorCacheConfig.GENERATOR_CACHE_NAME;
import knorxx.framework.generator.web.KnorxxApplication;
import knorxx.framework.generator.web.WebJavaScriptGenerator;
import knorxx.framework.generator.web.client.ErrorHandler;
import knorxx.framework.generator.web.client.RpcService;
import knorxx.framework.generator.web.client.WebPage;
import knorxx.framework.generator.web.client.webpage.PageArranger;
import knorxx.framework.generator.web.client.webpage.annotation.WebPageArranger;
import knorxx.framework.generator.web.generator.CssResult;
import knorxx.framework.generator.web.server.json.JsonHelper;
import knorxx.framework.generator.web.server.rpc.ExceptionMarshaller;
import knorxx.framework.generator.web.server.rpc.RpcCall;
import knorxx.framework.generator.web.server.rpc.RpcCaller;
import knorxx.framework.generator.web.server.rpc.RpcResult;
import knorxx.framework.generator.web.server.rtti.RttiGenerationResult;
import knorxx.framework.generator.web.server.rtti.RttiGenerator;
import knorxx.framework.generator.web.server.rtti.UrlResolver;
import org.joda.time.DateTime;
import org.rendersnake.HtmlCanvas;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author sj
 */
public abstract class KnorxxController implements ApplicationContextAware {
    
    public final static String FRAMEWORK_URL_PREFIX = "knorxx";
    private final static String JSON_RPC_URL = "/" + FRAMEWORK_URL_PREFIX + "/jsonRpc";
    
    private final Class<?> applicationRootClass;
    private final Class<?> javaScriptGenerationRoot;    
    private final Class<?> indexWebPageClass;
    private final Function<HttpServletRequest, GenerationRoots> generationRootsFunction;
    private GenerationRoots generationRoots;
    private final LibraryDetector libraryDetector;
    private final ExceptionMarshaller exceptionMarshaller;
    
    private final RpcCaller rpcCaller = new RpcCaller();
    private final UrlResolver urlResolver;
    
    @Autowired
    KnorxxApplication knorxxApplication;
    
    @Autowired
    Collection<RpcService> rpcServices;    
    
    ApplicationContext applicationContext;
    
    @Autowired
    Collection<WebPage> webPages;
    
    @Autowired
    CacheManager cacheManager;
    
    @Autowired
    JsonHelper jsonHelper;
    
    @Autowired
    knorxx.framework.generator.web.client.JsonHelper javaScriptJsonHelper;
    
    @Autowired
    ErrorHandler javaScriptErrorHandler;

    public KnorxxController(Class<?> applicationRootClass, Class<?> javaScriptGenerationRoot, Class<?> indexWebPageClass, 
            Function<HttpServletRequest, GenerationRoots> generationRootsFunction,
            LibraryDetector libraryDetector, ExceptionMarshaller exceptionMarshaller) {
        this.applicationRootClass = applicationRootClass;
        this.indexWebPageClass = indexWebPageClass;
        this.javaScriptGenerationRoot = javaScriptGenerationRoot;
        this.generationRootsFunction = generationRootsFunction;
        this.libraryDetector = libraryDetector;
        this.exceptionMarshaller = exceptionMarshaller;
        
        this.urlResolver = new UrlResolver(FRAMEWORK_URL_PREFIX, javaScriptGenerationRoot.getPackage().getName());
    }
    
    @RequestMapping(value = "/")
    public String index() {
        return "redirect:/" + urlResolver.resolveWebPage(indexWebPageClass.getName());
    }
    
    @RequestMapping(value = "/" + FRAMEWORK_URL_PREFIX + "/**/*.html")
    public ModelAndView webPage(HttpServletRequest request) throws IOException {
        Class<?> webPageClass = null;
        List<Class<?>> webPageClasses = new ArrayList<>();
        for(WebPage webPage : webPages) {
            webPageClasses.add(webPage.getClass());
            
            if(urlResolver.resolveWebPage(webPage.getClass().getName()).equals(getUrl(request))) {
                webPageClass = webPage.getClass();
            }            
        }
        
        if(webPageClass != null) {
            return generateWebPage(request, webPageClass, javaScriptGenerationRoot.getPackage().getName(), 
                    urlResolver, webPageClasses);
        } else {
            return null;        
        }
    }
    
    @RequestMapping(value = {"/" + FRAMEWORK_URL_PREFIX + "/**/*.js", "/" + FRAMEWORK_URL_PREFIX + "/**/*.js.map",
        "/" + FRAMEWORK_URL_PREFIX + "/**/*.css", "/" + FRAMEWORK_URL_PREFIX + "/**/*.java"})
    @ResponseBody
    public String responseFromCache(HttpServletRequest request) {
        return getResponseFromCache(request);
    }
    
    @RequestMapping(value = JSON_RPC_URL, method = RequestMethod.POST)
    @ResponseBody
    public String rpcCall(HttpServletRequest request) throws IOException {
        RpcCall rpcCall = new RpcCall(CharStreams.toString(new InputStreamReader(request.getInputStream(), "UTF-8")));
        RpcResult rpcResult = rpcCaller.call(rpcCall, exceptionMarshaller, jsonHelper, new ArrayList<>(rpcServices), request);
        return jsonHelper.toJson(rpcResult);
    }
    
    private ModelAndView generateWebPage(HttpServletRequest request, Class<?> webPageClass, 
            String allowedGenerationPackage, UrlResolver urlResolver, List<Class<?>> webPageClasses) throws IOException {
        Map<String, Object> model = new HashMap<>();
        model.put("applicationName", knorxxApplication.getName());
        
        model.put("mainClassName", webPageClass.getName());
        
        model.put("errorHandlerClassName", javaScriptErrorHandler.getClass().getName());        
        model.put("jsonHelperClassName", javaScriptJsonHelper.getClass().getName());
        
        String allowedReloadPackage = applicationRootClass.getPackage().getName();
        
        WebJavaScriptGenerator generator = new WebJavaScriptGenerator(getGenerationRoots(request),
                allowedGenerationPackage, allowedReloadPackage, JSON_RPC_URL.substring(1), libraryDetector);
        GenerationUnit unit = generator.generateAll(Lists.<Class<?>>newArrayList(webPageClass,
                javaScriptJsonHelper.getClass(), javaScriptErrorHandler.getClass()));
        
        model.put("libraryCssUrls", unit.getLibraryUrls().getCssUrls());
        model.put("libraryJavaScriptUrls", unit.getLibraryUrls().getJavaScriptUrls());
        
        RttiGenerator rttiGenerator = new RttiGenerator();
        RttiGenerationResult rttiGenerationResult = rttiGenerator.getJavaScriptSource(
                WebPage.class.getPackage().getName() + ".RunTimeTypeInformation",
                webPageClasses, urlResolver, new DateTime());
        
        LibraryUrls cacheUrls = populateCache(unit, rttiGenerationResult, urlResolver);
        model.put("cssUrls", cacheUrls.getCssUrls());
        model.put("javaScriptUrls", cacheUrls.getJavaScriptUrls());
        
        model.put("webPageModelJson", "{}");
        for(PageArranger pageArranger : applicationContext.getBeansOfType(PageArranger.class).values()) {
            if(webPageClass.getAnnotation(WebPageArranger.class) != null &&
                    webPageClass.getAnnotation(WebPageArranger.class).value().getName().equals(pageArranger.getClass().getName())) {
                Map<String, Object> webPageModel = new HashMap<>();
                pageArranger.initialize(webPageModel);
                model.put("webPageModelJson", jsonHelper.toJson(webPageModel));
                
                HtmlCanvas htmlCanvas = new HtmlCanvas();
                pageArranger.renderOn(htmlCanvas);
                model.put("preRenderedHtml", htmlCanvas.toHtml());

                break;
            }
        }
        
        return new ModelAndView("/presentation.jsp", model);
    }    
    
    private GenerationRoots getGenerationRoots(HttpServletRequest request) {
        generationRoots = generationRoots != null ? generationRoots : generationRootsFunction.apply(request);
        return generationRoots;
    }

    private LibraryUrls populateCache(GenerationUnit unit, RttiGenerationResult rttiGenerationResult, UrlResolver urlResolver) {
        LibraryUrls cacheUrls = new LibraryUrls();
        Cache generatorCache = cacheManager.getCache(GENERATOR_CACHE_NAME);
        
        for(GenerationResult result : Iterables.concat(unit.getGenerationResults(), Lists.newArrayList(rttiGenerationResult))) {
            if(result.getSingleResult() instanceof CssResult) {
                String cssUrl = urlResolver.resolveCssFile(result.getName());
                cacheUrls.getCssUrls().add(cssUrl);
                
                generatorCache.put(cssUrl, result);
            }
            
            String javaScriptUrl = urlResolver.resolveJavaScriptFile(result.getName());
            cacheUrls.getJavaScriptUrls().add(javaScriptUrl);
            generatorCache.put(javaScriptUrl, result);
        }
        
        return cacheUrls;
    }
    
    private String getUrl(HttpServletRequest request) {
        return request.getRequestURI().substring(request.getContextPath().length() + 1);
    }
    
    private String getResponseFromCache(HttpServletRequest request) {
        String url = getUrl(request);
        CacheRequestType requestType = JAVA_SCRIPT_OR_CSS;

        if (url.endsWith(".java")) {
            url = url.substring(0, url.length() - 4) + "js";
            requestType = JAVA;
        } else if (url.endsWith(".js.map")) {
            url = url.substring(0, url.length() - 4);
            requestType = SOURCE_MAP;
        }

        GenerationResult result = (GenerationResult) cacheManager.getCache(GENERATOR_CACHE_NAME).get(url).get();

        Optional<String> sourceMap = Optional.absent();
        if (result.getSingleResult() instanceof JavaScriptResult) {
            sourceMap = ((JavaScriptResult) result.getSingleResult()).getSourceMap();
        }
        
        if (requestType == SOURCE_MAP) {
            return sourceMap.get();
        } else if (requestType == JAVA) {
            try {
                InputStream javaSourceInputStream = result.getJavaFile().getSourceInputStream().get();
                return CharStreams.toString(new InputStreamReader(javaSourceInputStream, Charsets.UTF_8));
            } catch(IOException ex) {
                throw new IllegalStateException("Can't resolve the Java source file referenced by the source mapping URL!", ex);
            }            
        } else if (url.endsWith(".css")) {
            return ((CssResult) result.getSingleResult()).getCssSource();
        } else {
            String response = result.getSingleResult().getSource();

            if (sourceMap.isPresent()) {
                response += "\n//# sourceMappingURL=" + url.substring(url.lastIndexOf("/") + 1) + ".map";
            }

            return response;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}