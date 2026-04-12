package com.nidhinsai.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page object for the post-login dashboard / home screen.
 */
public class DashboardPage extends BasePage {

    private final By greeting       = By.cssSelector(".ng-star-inserted strong");
    private final By logoutButton   = By.cssSelector("[data-testid='logout'], #logout");
    private final By profileMenu    = By.cssSelector("[data-testid='profile'], #profile-menu");
    private final By pageHeading    = By.cssSelector("h1, .page-title");

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    /** Returns the post-login greeting text, e.g. "Hello, John!" */
    public String greetingText() {
        String text = getText(greeting);
        log.info("Greeting on dashboard: '{}'", text);
        return text;
    }

    /** True if the dashboard is loaded (greeting element visible). */
    public boolean isLoaded() {
        return isDisplayed(greeting);
    }

    public String pageHeading() {
        return getText(pageHeading);
    }

    public LoginPage logout() {
        log.info("Logging out");
        click(logoutButton);
        return new LoginPage(driver);
    }
}
