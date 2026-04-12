package com.nidhinsai.framework.pages;

import com.nidhinsai.framework.utils.ConfigReader;
import com.nidhinsai.framework.utils.ScreenshotUtil;
import com.nidhinsai.framework.utils.WaitUtil;
import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Base page object providing common driver interactions, waits, and evidence capture.
 * All page objects must extend this class.
 */
public abstract class BasePage {

    protected final Logger log = LogManager.getLogger(getClass());
    protected final WebDriver driver;
    protected final WaitUtil wait;
    protected final JavascriptExecutor js;
    protected final Actions actions;

    protected BasePage(WebDriver driver) {
        this.driver  = driver;
        this.wait    = new WaitUtil(driver);
        this.js      = (JavascriptExecutor) driver;
        this.actions = new Actions(driver);
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    protected void navigateTo(String url) {
        log.info("Navigating to: {}", url);
        driver.get(url);
    }

    protected String currentUrl() {
        return driver.getCurrentUrl();
    }

    protected String pageTitle() {
        return driver.getTitle();
    }

    // ── Element interactions ──────────────────────────────────────────────────

    protected void click(By locator) {
        log.debug("Clicking: {}", locator);
        wait.clickable(locator).click();
    }

    protected void clickJs(By locator) {
        log.debug("JS click: {}", locator);
        WebElement el = wait.visible(locator);
        js.executeScript("arguments[0].click();", el);
    }

    protected void type(By locator, String text) {
        log.debug("Typing '{}' into: {}", text, locator);
        WebElement el = wait.visible(locator);
        el.clear();
        el.sendKeys(text);
    }

    protected String getText(By locator) {
        String text = wait.visible(locator).getText();
        log.debug("getText({}) = '{}'", locator, text);
        return text;
    }

    protected String getAttribute(By locator, String attribute) {
        return wait.visible(locator).getAttribute(attribute);
    }

    protected boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected void selectByVisibleText(By locator, String text) {
        log.debug("Selecting '{}' in: {}", text, locator);
        new Select(wait.visible(locator)).selectByVisibleText(text);
    }

    protected void hover(By locator) {
        log.debug("Hovering over: {}", locator);
        actions.moveToElement(wait.visible(locator)).perform();
    }

    // ── Waits ────────────────────────────────────────────────────────────────

    protected void waitForUrl(String partialUrl) {
        int timeout = Integer.parseInt(ConfigReader.get("app.timeout.explicit", "20"));
        new WebDriverWait(driver, Duration.ofSeconds(timeout))
                .until(ExpectedConditions.urlContains(partialUrl));
    }

    protected void waitForTitle(String partialTitle) {
        int timeout = Integer.parseInt(ConfigReader.get("app.timeout.explicit", "20"));
        new WebDriverWait(driver, Duration.ofSeconds(timeout))
                .until(ExpectedConditions.titleContains(partialTitle));
    }

    // ── Evidence ─────────────────────────────────────────────────────────────

    protected byte[] takeScreenshot(String label) {
        return ScreenshotUtil.capture(label);
    }

    protected void scrollIntoView(By locator) {
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", wait.visible(locator));
    }
}
