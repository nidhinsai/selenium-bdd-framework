package com.nidhinsai.framework.utils;

import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitUtil {

    private static final Logger LOG = LogManager.getLogger(WaitUtil.class);

    private final WebDriverWait wait;
    private final FluentWait<WebDriver> fluentWait;
    private final WebDriver driver;

    public WaitUtil(WebDriver driver) {
        this.driver = driver;
        int explicitTimeout = Integer.parseInt(ConfigReader.get("app.timeout.explicit", "20"));
        int pollingMs       = Integer.parseInt(ConfigReader.get("app.timeout.polling_ms", "500"));

        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitTimeout));
        this.fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(explicitTimeout))
                .pollingEvery(Duration.ofMillis(pollingMs))
                // Ignore both common transient exceptions in CI environments
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }

    public WebElement visible(By locator) {
        LOG.debug("Waiting for visible: {}", locator);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement clickable(By locator) {
        LOG.debug("Waiting for clickable: {}", locator);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public boolean invisible(By locator) {
        LOG.debug("Waiting for invisible: {}", locator);
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public WebElement present(By locator) {
        LOG.debug("Waiting for DOM presence: {}", locator);
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /** Fluent wait — polls frequently, handles stale refs and missing elements gracefully. */
    public WebElement fluentVisible(By locator) {
        LOG.debug("Fluent-wait for visible: {}", locator);
        return fluentWait.until(ExpectedConditions.visibilityOfElementLocated(locator)::apply);
    }

    public boolean urlContains(String fragment) {
        return wait.until(ExpectedConditions.urlContains(fragment));
    }

    public boolean titleContains(String fragment) {
        return wait.until(ExpectedConditions.titleContains(fragment));
    }
}

    public WebElement visible(By locator) {
        LOG.debug("Waiting for visible: {}", locator);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement clickable(By locator) {
        LOG.debug("Waiting for clickable: {}", locator);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public boolean invisible(By locator) {
        LOG.debug("Waiting for invisible: {}", locator);
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public WebElement present(By locator) {
        LOG.debug("Waiting for DOM presence: {}", locator);
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /** Fluent wait — polls more frequently, useful for animated/async elements. */
    public WebElement fluentVisible(By locator) {
        LOG.debug("Fluent-wait for visible: {}", locator);
        return fluentWait.until(ExpectedConditions.visibilityOfElementLocated(locator)::apply);
    }

    public boolean urlContains(String fragment) {
        return wait.until(ExpectedConditions.urlContains(fragment));
    }

    public boolean titleContains(String fragment) {
        return wait.until(ExpectedConditions.titleContains(fragment));
    }
}
