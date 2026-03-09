package pages;

import org.example.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class LoginPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);

    // Şu anki DOM'a göre tek sayfalı login ekranı:
    // email: id=txtUserName, şifre: id=txtPassword, giriş: id=btnLogin
    // Eski fallback locator'ları ileride ihtiyaç olursa geri alabilmek için git revizyonunda duruyor.
    private final By emailInput = By.id("txtUserName");
    private final By passwordInput = By.id("txtPassword");
    private final By loginButton = By.id("btnLogin");

    public void typeEmail(String email) {
        WebElement input = findFirstVisible(Duration.ofSeconds(5), emailInput);
        input.clear();
        input.sendKeys(email);
        logger.info("Email/telefon alani dolduruldu.");
    }

    public void continueAfterEmail() {
        // Tek sayfalı login ekranında ekstra bir "Devam" adımı yok; şimdilik no-op.
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