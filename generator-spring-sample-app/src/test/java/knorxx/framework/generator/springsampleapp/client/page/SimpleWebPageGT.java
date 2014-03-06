package knorxx.framework.generator.springsampleapp.client.page;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author sj
 */
public class SimpleWebPageGT {

    @Test
    public void heading() {
        ChromeOptions options = new ChromeOptions();
		options.addArguments("window-size=800,600");
 
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		WebDriver driver = new ChromeDriver(capabilities);
        
        driver.get("http://localhost:8080/generator-spring-sample-app/knorxx/page/SimpleWebPage.html");
        
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id(SimpleWebPage.TITLE_ID.substring(1)), SimpleWebPage.HEADING));        
        
        driver.quit();
    }
}
