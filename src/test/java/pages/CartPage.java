package pages;

import org.example.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.assertj.core.api.Assertions.assertThat;

public class CartPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(CartPage.class);

    
    private final By addToCartDirectButton = By.cssSelector("button[data-test-id='addToCart']");
    private final By cartIcon = By.id("shoppingCart");
    private final By productTitleH1 = By.cssSelector("h1");

    public void addProductToCart() {
        clickElement(addToCartDirectButton);
        logger.info("Urun sepete eklendi butonuna tiklandi.");
    }

    public String readProductTitleOnDetailPage() {
        try {
            WebElement h1 = findElement(productTitleH1);
            String txt = h1.getText() == null ? "" : h1.getText().trim();
            if (!txt.isBlank()) return txt;
        } catch (Exception ignored) {
        }
        return "";
    }

    public void goToCart() {
        clickElement(cartIcon);
        logger.info("Sepetim sayfasina gidildi.");
    }

    public void verifyProductInCart(String expectedTitle) {
        if (expectedTitle == null) expectedTitle = "";
        expectedTitle = expectedTitle.trim();

       
        var nameLinks = findElements(By.cssSelector("div.product_name_2Klj3 a[href]"));

        assertThat(nameLinks)
                .withFailMessage("Sepette hic urun bulunamadi.")
                .isNotEmpty();

        WebElement first = nameLinks.get(0);

        if (!expectedTitle.isBlank()) {
            String inCartTitle = first.getText() == null ? "" : first.getText().trim();
            String expectedSnippet = expectedTitle.length() > 40
                    ? expectedTitle.substring(0, 40)
                    : expectedTitle;

            assertThat(inCartTitle.toLowerCase())
                    .withFailMessage("Sepetteki urun basligi beklenenden farkli. Beklenen icersin: %s, Bulunan: %s",
                            expectedSnippet, inCartTitle)
                    .contains(expectedSnippet.toLowerCase());
        }

        logger.info("Urunun sepette oldugu basariyla dogrulandi.");
    }
}