package pages;

import org.example.BasePage;
import org.example.Locator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.assertj.core.api.Assertions.assertThat;

public class CartPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(CartPage.class);

    private final By addToCartDirectButton = Locator.get("addToCart");
    private final By cartIcon              = Locator.get("cartIcon");
    private final By productTitleH1        = By.cssSelector("h1");

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

        if (!expectedTitle.isBlank()) {
            String core = extractCoreTitle(expectedTitle);
            String expectedSnippet = core.length() > 40 ? core.substring(0, 40) : core;
            String expectedLower = expectedSnippet.toLowerCase();

            boolean anyMatch = false;
            String lastSeen = "";

            for (WebElement link : nameLinks) {
                String inCartTitle = link.getText() == null ? "" : link.getText().trim();
                lastSeen = inCartTitle;
                if (!inCartTitle.isEmpty() && inCartTitle.toLowerCase().contains(expectedLower)) {
                    anyMatch = true;
                    break;
                }
            }

            assertThat(anyMatch)
                    .withFailMessage("Sepetteki urun basligi beklenenden farkli. Beklenen icersin: %s, Son gorulen: %s",
                            expectedSnippet, lastSeen)
                    .isTrue();
        }

        logger.info("Urunun sepette oldugu basariyla dogrulandi.");
    }

    private String extractCoreTitle(String raw) {
        String cleaned = raw.replace("\r", "");
        String[] lines = cleaned.split("\n");
        String best = cleaned.trim();
        int bestLen = 0;
        for (String line : lines) {
            String t = line.trim();
            if (t.length() > bestLen) {
                bestLen = t.length();
                best = t;
            }
        }
        return best;
    }
}