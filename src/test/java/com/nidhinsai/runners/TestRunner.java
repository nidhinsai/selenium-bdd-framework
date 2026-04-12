package com.nidhinsai.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * Default (sequential) test runner.
 * Tags can be overridden at runtime: -Dcucumber.filter.tags="@smoke"
 */
@CucumberOptions(
        features  = "src/test/resources/features",
        glue      = {"com.nidhinsai.steps", "com.nidhinsai.hooks"},
        plugin    = {
            "pretty",
            "html:target/cucumber-report.html",
            "json:target/cucumber.json",
            "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true,
        tags       = "not @skip"
)
public class TestRunner extends AbstractTestNGCucumberTests {
}