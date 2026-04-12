package com.nidhinsai.hooks;

import com.nidhinsai.framework.base.DriverFactory;
import com.nidhinsai.framework.utils.EvidenceCapture;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Hooks {

    private static final Logger LOG = LogManager.getLogger(Hooks.class);

    @Before
    public void beforeScenario(Scenario scenario) {
        LOG.info("========== START: [{}] ==========", scenario.getName());
        DriverFactory.initDriver();
        LOG.debug("WebDriver initialised for thread {}", Thread.currentThread().getName());
    }

    /**
     * Runs after every scenario.
     * On failure: captures a full-evidence bundle (screenshot + page source + URL)
     * and attaches the screenshot bytes inline into the Cucumber HTML report.
     */
    @After(order = 100)
    public void captureEvidenceOnFailure(Scenario scenario) {
        if (scenario.isFailed()) {
            LOG.error("Scenario FAILED: [{}] — capturing evidence...", scenario.getName());
            try {
                byte[] png = EvidenceCapture.captureAll(scenario.getName());
                if (png.length > 0) {
                    scenario.attach(png, "image/png", "Failure Screenshot");
                    LOG.info("Screenshot attached to Cucumber report for scenario: {}", scenario.getName());
                }
            } catch (Exception e) {
                LOG.error("Evidence capture threw an exception: {}", e.getMessage(), e);
            }
        }
    }

    @After(order = 0)
    public void tearDownDriver(Scenario scenario) {
        DriverFactory.quitDriver();
        String status = scenario.isFailed() ? "FAILED" : "PASSED";
        LOG.info("========== END  : [{}] — {} ==========", scenario.getName(), status);
    }
}
