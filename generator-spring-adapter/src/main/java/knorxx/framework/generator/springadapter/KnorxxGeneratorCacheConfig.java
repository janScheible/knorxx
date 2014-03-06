package knorxx.framework.generator.springadapter;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.InputStreamResource;

/**
 *
 * @author sj
 */
@Configuration
@EnableCaching
public class KnorxxGeneratorCacheConfig {
    
    public final static String GENERATOR_CACHE_NAME = "generatorCache";

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();

        try {
            ehCacheManagerFactoryBean.setConfigLocation(new InputStreamResource(new ByteArrayInputStream((
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<ehcache>\n" +
                    "    <defaultCache eternal=\"true\" maxElementsInMemory=\"100\" overflowToDisk=\"false\" />\n" +
                    "    <cache name=\"" + GENERATOR_CACHE_NAME + "\" maxEntriesLocalHeap=\"1000\" />" +
                    "</ehcache>").getBytes("UTF-8"))));
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException(ex);
        }

        return ehCacheManagerFactoryBean;
    }

    @Bean
    public CacheManager cacheManager() {
        EhCacheCacheManager cacheManager = new EhCacheCacheManager();
        cacheManager.setCacheManager(ehCacheManagerFactoryBean().getObject());
        return cacheManager;
    }
}
