package com.nidhinsai.framework.pages;

import com.nidhinsai.framework.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private final WebDriver driver;
    private final WaitUtil wait;

    private final By username = By.id("name");
    private final By password = By.id("password");
    private final By loginButton = By.id("login");
    private final By greeting = By.cssSelector(".ng-star-inserted strong");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WaitUtil(driver);
    }

    public void open(String url) {
        driver.get(url);
    }

    public void login(String user, String pass) {
        wait.visible(username).clear();
        wait.visible(username).sendKeys(user);
        wait.visible(password).clear();
        wait.visible(password).sendKeys(pass);
        wait.clickable(loginButton).click();
    }

    public String greetingText() {
        return wait.visible(greeting).getText();
    }
}