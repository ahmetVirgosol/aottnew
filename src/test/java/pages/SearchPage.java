package pages;

import org.example.BasePage;
import org.example.Locator;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(SearchPage.class);

    private final By productListItems = Locator.get("productItems");

    public void waitForSearchResults() {
        List<WebElement> products = findElements(productListItems);

        assertThat(products)
                .withFailMessage("Arama sonucunda urun listesi bos geldi.")
                .isNotEmpty();

        logger.info("Arama sonuclari sayfasinin yuklendigi ve urunlerin listelendigi dogrulandi.");
    }

    public String selectFirstProductOnSecondRowAndOpen() {
        waitForSearchResults();
        List<WebElement> products = findElements(productListItems);

        int targetIndex = resolveSecondRowFirstItemIndex(products);
        if (targetIndex < 0 || targetIndex >= products.size()) {
            fail("Ikinci satirin ilk urunu bulunamadi. Uygun grid/satir yapisi tespit edilemedi.");
        }

        WebElement target = products.get(targetIndex);
        scrollIntoView(target);

        String title = safeText(target);
        if (title.isBlank()) {
            title = "(urun basligi okunamadi)";
        }

        boolean clicked = false;
        try {
            WebElement link = target.findElement(By.cssSelector("a"));
            link.click();
            clicked = true;
        } catch (Exception ignored) {
        }
        if (!clicked) {
            try {
                target.click();
            } catch (Exception e) {
                fail("Urun kartina tiklanamadi: " + e.getMessage());
            }
        }

        switchToNewestWindow();

        logger.info("Ikinci satirdaki ilk urun acildi. Index=" + targetIndex + ", title=" + title);
        return title;
    }

    private int resolveSecondRowFirstItemIndex(List<WebElement> products) {
        ensureDriver();
        JavascriptExecutor js = (JavascriptExecutor) driver;

        @SuppressWarnings("unchecked")
        List<Map<String, Number>> rects = (List<Map<String, Number>>) js.executeScript(
                """
                const els = arguments[0];
                return Array.from(els).map(e => {
                  const r = e.getBoundingClientRect();
                  return { top: Math.round(r.top), left: Math.round(r.left), width: Math.round(r.width), height: Math.round(r.height) };
                });
                """,
                products
        );

        if (rects == null || rects.isEmpty()) return -1;

        int tolerance = 6;
        List<Integer> rowTops = new ArrayList<>();
        for (Map<String, Number> r : rects) {
            int top = r.get("top").intValue();
            boolean matched = false;
            for (int i = 0; i < rowTops.size(); i++) {
                if (Math.abs(rowTops.get(i) - top) <= tolerance) {
                    matched = true;
                    break;
                }
            }
            if (!matched) rowTops.add(top);
        }
        rowTops.sort(Integer::compareTo);
        if (rowTops.size() < 2) return -1;
        int secondRowTop = rowTops.get(1);

        int bestIdx = -1;
        int bestLeft = Integer.MAX_VALUE;
        for (int i = 0; i < rects.size(); i++) {
            int top = rects.get(i).get("top").intValue();
            if (Math.abs(top - secondRowTop) <= tolerance) {
                int left = rects.get(i).get("left").intValue();
                if (left < bestLeft) {
                    bestLeft = left;
                    bestIdx = i;
                }
            }
        }
        return bestIdx;
    }

    private String safeText(WebElement el) {
        try {
            String t = el.getText();
            return t == null ? "" : t.trim();
        } catch (Exception e) {
            return "";
        }
    }
}