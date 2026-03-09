package org.example;

import base.BaseTest;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage() {
        //db lazyloading alakası yok  gecikme suresıne fokus kotu durumdan korun
    }

    protected void ensureDriver() {
        if (driver == null) {
            driver = BaseTest.getDriver();
        }
        if (driver == null) {
            throw new IllegalStateException("WebDriver hazir degil. @BeforeScenario hook'u calismamis olabilir.");
        }
        if (wait == null) {
            wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        }
    }

    protected WebElement findElement(By locator) {
        ensureDriver();
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void clickElement(By locator) {
        ensureDriver();
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        try {
            el.click();
        } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
            tryDismissObstructions();
            try {
                wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
            } catch (Exception ignored) {
                jsClick(locator);
            }
        }
    }

    protected void sendKeysToElement(By locator, String text) {
        ensureDriver();
        WebElement element = findElement(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected List<WebElement> findElements(By locator) {
        ensureDriver();
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    protected void waitUntilVisible(By locator) {
        ensureDriver();
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void waitUntilUrlContains(String fragment) {
        ensureDriver();
        wait.until(ExpectedConditions.urlContains(fragment));
    }

    protected Optional<WebElement> findOptional(By locator, Duration timeout) {
        ensureDriver();
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, timeout);
            return Optional.of(shortWait.until(ExpectedConditions.visibilityOfElementLocated(locator)));
        } catch (TimeoutException e) {
            return Optional.empty();
        }
    }

    protected Optional<WebElement> findClickableOptional(By locator, Duration timeout) {
        ensureDriver();
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, timeout);
            return Optional.of(shortWait.until(ExpectedConditions.elementToBeClickable(locator)));
        } catch (TimeoutException e) {
            return Optional.empty();
        }
    }

    protected WebElement findFirstVisible(Duration timeout, By... locators) {
        ensureDriver();
        for (By locator : locators) {
            Optional<WebElement> found = findOptional(locator, timeout);
            if (found.isPresent()) return found.get();
        }
        throw new NoSuchElementException("Gorunur element bulunamadi. Locator sayisi=" + locators.length);
    }

    protected WebElement findFirstClickable(Duration timeout, By... locators) {
        ensureDriver();
        for (By locator : locators) {
            Optional<WebElement> found = findClickableOptional(locator, timeout);
            if (found.isPresent()) return found.get();
        }
        throw new NoSuchElementException("Click edilebilir element bulunamadi. Locator sayisi=" + locators.length);
    }

    protected void scrollIntoView(WebElement element) {
        ensureDriver();
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'center'});", element);
    }

    protected void jsClick(By locator) {
        ensureDriver();
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    protected void tryDismissObstructions() {
        ensureDriver();
        try {
            driver.switchTo().activeElement().sendKeys(Keys.ESCAPE);
        } catch (Exception ignored) {
        }

        By[] closeCandidates = new By[]{
                By.id("onetrust-accept-btn-handler"),
                By.cssSelector("button[aria-label='Kapat']"),
                By.cssSelector("button[title='Kapat']"),
                By.cssSelector("button[aria-label='Close']"),
                By.cssSelector("button[title='Close']"),
                By.cssSelector("[class*='close' i]"),
                By.cssSelector("[data-test-id*='close' i]"),
                By.xpath("//button[contains(.,'Kabul') or contains(.,'kabul') or contains(.,'Accept') or contains(.,'accept')]"),
                By.xpath("//button[contains(.,'Kapat') or contains(.,'kapat') or contains(.,'X')]")
        };

        for (By by : closeCandidates) {
            try {
                List<WebElement> els = driver.findElements(by);
                for (WebElement el : els) {
                    try {
                        if (el.isDisplayed() && el.isEnabled()) {
                            el.click();
                            // tek popup kapat kalanlarına dokunma durumu .!!
                            return;
                        }
                    } catch (Exception ignored) {
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }

    protected void failIfBlockedN1E2() {
        ensureDriver();
        By n1e2 = By.xpath("//*[contains(.,'Hata Kodu') and contains(.,'(N1E2)')]");
        if (findOptional(n1e2, Duration.ofSeconds(1)).isPresent()) {
            throw new IllegalStateException("Sayfa 'Hata Kodu: (N1E2)' ile engellendi (bot/süpheli trafik). Bu durumda otomasyon akışı devam edemez.");
        }
    }

    protected void switchToNewestWindow() {
        ensureDriver();
        String current = driver.getWindowHandle();
        Set<String> handles = driver.getWindowHandles();
        if (handles.size() <= 1) return;

        List<String> ordered = new ArrayList<>(handles);
        
        for (int i = ordered.size() - 1; i >= 0; i--) {
            String h = ordered.get(i);
            if (!h.equals(current)) {
                driver.switchTo().window(h);
                return;
            }
        }
    }
}