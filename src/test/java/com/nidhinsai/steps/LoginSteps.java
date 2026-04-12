package com.nidhinsai.steps;

import com.nidhinsai.framework.base.DriverFactory;
import com.nidhinsai.framework.pages.LoginPage;
import com.nidhinsai.framework.utils.ConfigReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;

public class LoginSteps {
    private LoginPage loginPage;

    @Given("the user is on the login page")
    public void theUserIsOnTheLoginPage() {
        loginPage = new LoginPage(DriverFactory.getDriver());
        loginPage.open(ConfigReader.get("app.url", "https://example.testproject.io/web/"));
    }

    @When("the user logs in with username {string} and password {string}")
    public void theUserLogsInWithUsernameAndPassword(String username, String password) {
        loginPage.login(username, password);
    }

    @Then("the greeting should contain {string}")
    public void theGreetingShouldContain(String expected) {
        Assertions.assertThat(loginPage.greetingText()).contains(expected);
    }
}