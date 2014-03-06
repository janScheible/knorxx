package knorxx.framework.generator.springsampleapp;

import knorxx.framework.generator.web.KnorxxApplication;
import knorxx.framework.generator.springadapter.KnorxxGeneratorCacheConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *
 * @author sj
 */
@Configuration
@EnableWebMvc
@ComponentScan("knorxx.framework.generator.springsampleapp")
@Import({KnorxxGeneratorCacheConfig.class})
public class SpringSampleAppConfig extends WebMvcConfigurerAdapter implements KnorxxApplication {

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
    
    @Override
    public String getName() {
        return "Spring Sample App";
    }
}