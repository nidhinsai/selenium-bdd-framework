package com.nidhinsai.steps;

import com.nidhinsai.framework.base.DriverFactory;
import com.nidhinsai.framework.pages.DashboardPage;
import com.nidhinsai.framework.pages.LoginPage;
import com.nidhinsai.framework.utils.ConfigReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.api.Assertions;

public class LoginSteps {

    private static final Logger LOG = LogManager.getLogger(LoginSteps.class);

    private LoginPage     loginPage;
    private DashboardPage dashboardPage;

    @Step("Navigate to the login page")
    @Given("the user is on the login page")
    public void theUserIsOnTheLoginPage() {
        LOG.info("Step: navigating to login page");
        loginPage = new LoginPage(DriverFactory.getDriver());
        loginPage.open(ConfigReader.get("app.url", "https://example.testproject.io/web/"));
    }

    @Step("Login with username '{username}' and password '{password}'")
    @When("the user logs in with username {string} and password {string}")
    public void theUserLogsInWithUsernameAndPassword(String username, String password) {
        LOG.info("Step: login with username='{}'", username);
        dashboardPage = loginPage.loginAs(username, password);
    }

    @Step("Attempt login with username '{username}' and password '{password}' (negative path)")
    @When("the user attempts login with username {string} and password {string}")
    public void theUserAttemptsLoginWithInvalidCredentials(String username, String password) {
        LOG.info("Step: attempting login (negative path) with username='{}'", username);
        loginPage.loginExpectingFailure(username, password);
    }

    @Step("Assert dashboard greeting contains '{expected}'")
    @Then("the greeting should contain {string}")
    public void theGreetingShouldContain(String expected) {
        LOG.info("Step: asserting greeting contains '{}'", expected);
        String actual = dashboardPage.greetingText();
        LOG.debug("Actual greeting: '{}'", actual);
        Assertions.assertThat(actual)
                .as("Greeting on dashboard should contain '%s' but was '%s'", expected, actual)
                .contains(expected);
    }

    @Step("Assert login error message contains '{expected}'")
    @Then("the login error message should contain {string}")
    public void theLoginErrorShouldContain(String expected) {
        LOG.info("Step: asserting login error contains '{}'", expected);
        Assertions.assertThat(loginPage.hasErrorMessage())
                .as("Expected an error message to be displayed on failed login")
                .isTrue();
        Assertions.assertThat(loginPage.errorMessage())
                .as("Login error should contain '%s'", expected)
                .contains(expected);
    }

    @Step("Assert the dashboard is displayed")
    @Then("the dashboard should be displayed")
    public void theDashboardShouldBeDisplayed() {
        LOG.info("Step: asserting dashboard is loaded");
        Assertions.assertThat(dashboardPage.isLoaded())
                .as("Dashboard should be displayed after successful login")
                .isTrue();
    }
}
