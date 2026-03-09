package base;

import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeScenario;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class BaseTest {
    protected static WebDriver driver;
    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

    @BeforeScenario
    public void setUp() {
        logger.info("Test senaryosu basliyor. Tarayici ayarlari yapilandiriliyor.");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--lang=tr-TR");
        options.addArguments("--disable-blink-features=AutomationControlled");

        driver = new ChromeDriver(options);
      
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(120));
        driver.manage().window().maximize();
        logger.info("Tarayici basariyla acildi.");
    }

    @AfterScenario
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            logger.info("Tarayici kapatildi. Test senaryosu sonlandi.");
        }
    }

    public static WebDriver getDriver() {
        return driver;
    }
}