package com.nidhinsai.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private final By username    = By.id("name");
    private final By password    = By.id("password");
    private final By loginButton = By.id("login");
    private final By errorMsg    = By.cssSelector(".error-msg, [data-testid='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open(String url) {
        navigateTo(url);
        return this;
    }

    public DashboardPage loginAs(String user, String pass) {
        log.info("Logging in as: {}", user);
        type(username, user);
        type(password, pass);
        click(loginButton);
        return new DashboardPage(driver);
    }

    /** Attempts login and stays on the login page (for negative-path tests). */
    public LoginPage loginExpectingFailure(String user, String pass) {
        log.info("Attempting login (expecting failure) as: {}", user);
        type(username, user);
        type(password, pass);
        click(loginButton);
        return this;
    }

    public boolean hasErrorMessage() {
        return isDisplayed(errorMsg);
    }

    public String errorMessage() {
        return getText(errorMsg);
    }
}

