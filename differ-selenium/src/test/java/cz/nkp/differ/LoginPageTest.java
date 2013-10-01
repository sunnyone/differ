package cz.nkp.differ;
import com.google.common.base.Function;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Stavel <stavel.jan@gmail.com>
 * Date: 17.9.13
 * Time: 13:21
 */

public class LoginPageTest {
    WebDriver driver = new FirefoxDriver();

    @Before
    public void setUp() throws Exception {
        //System.setProperty("webdriver.chrome.driver", "/usr/bin/google-chrome");
        System.out.println("vystup");
        driver.get("http://differ.nkp.cz");
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }
    @Test
    public void loginPage() throws Exception {
        System.out.println("loginPage");
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(30, TimeUnit.SECONDS)
                .pollingEvery(5, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);

        WebElement loginButton = wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return driver.findElement(By.xpath("//span[text()=='Login']"));
            }
        });
        System.out.println("login button click");
        loginButton.click();
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }
 }
