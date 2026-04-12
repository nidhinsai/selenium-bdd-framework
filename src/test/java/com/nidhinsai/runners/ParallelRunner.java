package com.nidhinsai.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Parallel scenario runner.
 * Parallelism is controlled by the thread-count in testng-parallel.xml.
 * Run with: mvn test -DsuiteFile=testng-parallel.xml
 */
@CucumberOptions(
        features  = "src/test/resources/features",
        glue      = {"com.nidhinsai.steps", "com.nidhinsai.hooks"},
        plugin    = {
            "pretty",
            "json:target/cucumber-parallel.json",
            "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true,
        tags       = "not @skip"
)
public class ParallelRunner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}