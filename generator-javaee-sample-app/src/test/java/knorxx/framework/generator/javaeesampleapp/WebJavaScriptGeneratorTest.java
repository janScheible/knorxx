/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package knorxx.framework.generator.javaeesampleapp;

import knorxx.framework.generator.GenerationRoots;
import knorxx.framework.generator.GenerationUnit;
import knorxx.framework.generator.javaeesampleapp.client.ChatWebPage;
import knorxx.framework.generator.javaeesampleapp.client.GuiWebPage;
import knorxx.framework.generator.javaeesampleapp.client.ServiceWebPage;
import knorxx.framework.generator.javaeesampleapp.client.SimpleWebPage;
import knorxx.framework.generator.library.LibraryDetector;
import knorxx.framework.generator.web.WebJavaScriptGenerator;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author sj
 */
public class WebJavaScriptGeneratorTest {
    
    private WebJavaScriptGenerator generator;
    
    @Before
    public void setUp() {
        GenerationRoots generationRoots = GenerationRoots.Simple.createMainJava();
        
        String allowedReloadPackage = JavaEeSampleApp.class.getPackage().getName();
        String allowedGenerationPackage = SimpleWebPage.class.getPackage().getName();
        
        generator  = new WebJavaScriptGenerator(generationRoots, allowedGenerationPackage, allowedReloadPackage,
                "/csrfProtectionCookiePath", "/rpcUrl", new LibraryDetector.None());
    }
    
    @Test
    public void simpleWebPage() {
        GenerationUnit result = generator.generateAll(SimpleWebPage.class);
        "".trim();
    }

    @Test
    public void serviceWebPage() {
        GenerationUnit result = generator.generateAll(ServiceWebPage.class);
        "".trim();
    }

    @Test
    public void chatWebPage() {
        GenerationUnit result = generator.generateAll(ChatWebPage.class);
        "".trim();
    }

    @Test
    public void guiWebPage() {
        GenerationUnit result = generator.generateAll(GuiWebPage.class);
        "".trim();
    }
}
