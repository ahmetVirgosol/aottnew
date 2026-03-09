package org.example;

import com.thoughtworks.gauge.Step;
import pages.CartPage;
import pages.HomePage;
import pages.LoginPage;
import pages.SearchPage;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import static org.assertj.core.api.Assertions.assertThat;

public class StepImplementation extends BasePage {
    private final HomePage homePage = new HomePage();
    private final LoginPage loginPage = new LoginPage();
    private final SearchPage searchPage = new SearchPage();
    private final CartPage cartPage = new CartPage();

    private String selectedProductTitle = "";

    @Step("<key> adresine gidilir")
    public void navigate(String key) {
        ensureDriver();
        driver.get(JsonUtils.getValue(key));
    }

    @Step("<key> üzerine hover yapılır")
    public void hoverStep(String key) {
        ensureDriver();
        new Actions(driver).moveToElement(driver.findElement(Locator.get(key))).perform();
    }

    @Step("loginMenu üzerine hover yapılır")
    public void hoverLoginMenu() {
        hoverStep("loginMenu");
    }

    @Step("<key> tıklanır")
    public void clickStep(String key) {
        clickElement(Locator.get(key));
    }

    @Step("loginBtn tıklanır")
    public void clickLoginButtonFromMenu() {
        clickStep("loginBtn");
    }

    @Step("<key> alanına <dataKey> yazılır")
    public void typeStep(String key, String dataKey) {
        sendKeysToElement(Locator.get(key), JsonUtils.getValue(dataKey));
    }

    @Step("<key> görünür olmalı")
    public void checkVisible(String key) {
        assertThat(findElement(Locator.get(key)).isDisplayed()).isTrue();
    }

    @Step("<key> alanında ENTER basılır")
    public void pressEnter(String key) {
        findElement(Locator.get(key)).sendKeys(Keys.ENTER);
    }

    @Step("<key> listesinin dolu olduğu doğrulanır")
    public void checkList(String key) {
        assertThat(findElements(Locator.get(key))).isNotEmpty();
    }

    @Step("Gridde 2. satır 1. ürün seçilir")
    public void selectProduct() {
        selectedProductTitle = searchPage.selectFirstProductOnSecondRowAndOpen();
        // Ürün detay sayfasından daha net başlık okunabiliyorsa onu tercih et.
        String detailTitle = cartPage.readProductTitleOnDetailPage();
        if (detailTitle != null && !detailTitle.isBlank()) {
            selectedProductTitle = detailTitle;
        }
    }

    // --- HB-TC01 senaryosu için daha okunaklı adımlar ---

    @Step("Tarayıcıyı aç")
    public void openBrowser() {
        // Tarayıcı BaseTest @BeforeScenario ile açılıyor; burada sadece hazır olduğunu doğruluyoruz.
        ensureDriver();
        assertThat(driver).isNotNull();
    }

    @Step("URL'ye git")
    public void goToHomePage() {
        homePage.navigateToUrl(JsonUtils.getValue("url"));
    }

    @Step("Giriş yap sayfasına git")
    public void goToLoginPage() {
        homePage.goToLoginPage();
    }

    @Step("Email-telefon alanına geçerli kullanıcı adı gir")
    public void typeValidEmailAndContinue() {
        String email = envOrJson("HB_EMAIL", "email");
        loginPage.typeEmail(email);
    }

    @Step("Şifre alanına şifreyi gir")
    public void typePassword() {
        String password = envOrJson("HB_PASSWORD", "password");
        loginPage.typePassword(password);
    }

    @Step("Giriş yap butonuna tıkla")
    public void submitLogin() {
        loginPage.submitLogin();
    }

    @Step("Loginin başarılı olduğunu doğrula")
    public void verifyLoginSuccess() {
        homePage.verifySuccessfulLogin();
    }

    @Step("Arama kutusuna bilgisayar yaz")
    public void typeComputerInSearch() {
        homePage.typeSearchKeyword(JsonUtils.getValue("arananKelime"));
    }

    @Step("Aramayı başlat")
    public void submitSearch() {
        homePage.submitSearch();
    }

    @Step("Arama sonuçlarının gelmesini bekle")
    public void waitResults() {
        searchPage.waitForSearchResults();
    }

    @Step("Ürün listesi görünür olmalı")
    public void verifyProductListVisible() {
        checkList("productItems");
    }

    @Step("İkinci satırdaki ilk ürünü sepete ekle")
    public void addSelectedProductToCart() {
        cartPage.addProductToCart();
    }

    @Step("Sepete git")
    public void goToCart() {
        cartPage.goToCart();
    }

    @Step("Sepette olduğunu doğrula")
    public void verifyInCart() {
        cartPage.verifyProductInCart(selectedProductTitle);
    }

    private String envOrJson(String envKey, String jsonKey) {
        String env = System.getenv(envKey);
        if (env != null && !env.isBlank()) return env.trim();
        return JsonUtils.getValue(jsonKey);
    }
}