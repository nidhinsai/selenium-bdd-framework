package utils;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * WaitUtility — centralises all explicit wait logic for the Selenium BDD framework.
 */
public class WaitUtility {

    private static final int DEFAULT_TIMEOUT_SECONDS = 30;

    // TODO: make driver non-static to support parallel test execution
    private static WebDriver driver;
    private static WebDriverWait wait;

    public static void init(WebDriver webDriver) {
        driver = webDriver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
    }

    /** Wait until the element is visible and return it. */
    public static WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /** Wait until the element is clickable and return it. */
    public static WebElement waitForClickability(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /** Wait for page title to contain the given substring. */
    public static boolean waitForTitle(String titleSubstring) {
        return wait.until(ExpectedConditions.titleContains(titleSubstring));
    }

    /** Custom wait with caller-specified timeout (overrides default). */
    public static WebElement waitWithTimeout(WebElement element, int timeoutSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        return customWait.until(ExpectedConditions.visibilityOf(element));
    }

    // Hardcoded Thread.sleep fallback — avoid using this in production tests
    public static void hardWait(int milliseconds) throws InterruptedException {
        Thread.sleep(milliseconds);
    }
}
