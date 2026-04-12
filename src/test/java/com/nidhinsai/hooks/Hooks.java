package com.nidhinsai.hooks;

import com.nidhinsai.framework.base.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {
    @Before
    public void beforeScenario() {
        DriverFactory.initDriver();
    }

    @After
    public void afterScenario() {
        DriverFactory.quitDriver();
    }
}