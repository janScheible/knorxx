    package knorxx.framework.generator.springsampleapp;

import knorxx.framework.generator.springadapter.KnorxxDispatcherServletInitializer;

public class SpringSampleAppDispatchServletInitializer extends KnorxxDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[0];
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{SpringSampleAppConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}