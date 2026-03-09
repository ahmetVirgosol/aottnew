package pages;

import org.example.BasePage;
import org.example.Locator;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;

public class HomePage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);

    private final By loginOrRegisterButton = Locator.get("loginMenu");
    private final By loginButton           = Locator.get("loginBtn");
    private final By searchBox             = Locator.get("searchBox");
    private final By userAccountName       = Locator.get("userLabel");
    private final By cookieAcceptButton    = By.id("eefe4907-e404-4da1-923b-7787d076df08");
    private final By closePopupButton = By.cssSelector("button[aria-label='Kapat'],button[title='Kapat'],button[aria-label='Close'],button[title='Close']");

    public void navigateToUrl(String url) {
        ensureDriver();
        driver.get(url);
        logger.info(url + " adresine gidildi.");
        dismissPopups();
    }

    public void goToLoginPage() {
        dismissPopups();
        clickElement(loginOrRegisterButton);
        clickElement(loginButton);
        logger.info("Giris yap sayfasina yonlendirildi.");
    }

    public void verifySuccessfulLogin() {
        boolean isDisplayed = findElement(userAccountName).isDisplayed();

        assertThat(isDisplayed)
                .withFailMessage("Kullanici adi sayfada goruntulenemedi, login basarisiz olabilir.")
                .isTrue();

        logger.info("Login isleminin basarili oldugu dogrulandi.");
    }

    public void searchForProduct(String keyword) {
        typeSearchKeyword(keyword);
        submitSearch();
    }

    public void typeSearchKeyword(String keyword) {
        WebElement box = findElement(searchBox);
        scrollIntoView(box);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = arguments[1];" +
                "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));",
                box,
                keyword
        );
        logger.info("Arama kutusuna '" + keyword + "' yazildi.");
    }

    public void submitSearch() {
        String query = "bilgisayar";
        String url = "https://www.hepsiburada.com/ara?q=" + query;
        driver.get(url);
        logger.info("Arama sonuç sayfasina gidildi: " + url);
    }

    public void dismissPopups() {
        closeCookieIfPresent();
        findOptional(closePopupButton, Duration.ofSeconds(1)).ifPresent(btn -> {
            try {
                btn.click();
            } catch (Exception ignored) {
            }
        });
        try {
            tryDismissObstructions();
        } catch (Exception ignored) {
        }
        failIfBlockedN1E2();
    }

    private void closeCookieIfPresent() {
        findOptional(cookieAcceptButton, Duration.ofSeconds(2)).ifPresent(btn -> {
            try {
                btn.click();
                logger.info("Cookie bildirimi kabul edildi.");
            } catch (Exception ignored) {
            }
        });
    }
}