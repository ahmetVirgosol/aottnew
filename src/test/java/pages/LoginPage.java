package pages;

import org.example.BasePage;
import org.example.Locator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class LoginPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);

    private final By emailInput    = Locator.get("emailInput");
    private final By passwordInput = Locator.get("passwordInput");
    private final By loginButton   = Locator.get("btn_GirisYap");

    public void typeEmail(String email) {
        WebElement input = findFirstVisible(Duration.ofSeconds(5), emailInput);
        input.clear();
        input.sendKeys(email);
        logger.info("Email/telefon alani dolduruldu.");
    }

    public void continueAfterEmail() {
        logger.info("continueAfterEmail cagrildi ancak tek sayfali login ekraninda ek islem yapilmadi.");
    }

    public void typePassword(String password) {
        WebElement input = findFirstVisible(Duration.ofSeconds(12), passwordInput);
        input.clear();
        input.sendKeys(password);
        logger.info("Sifre alani dolduruldu.");
    }

    public void submitLogin() {
        WebElement btn = findFirstClickable(Duration.ofSeconds(12), loginButton);
        btn.click();
        logger.info("Giris yap butonuna tiklandi.");
    }

    public void login(String email, String password) {
        typeEmail(email);
        continueAfterEmail();
        typePassword(password);
        submitLogin();
    }
}