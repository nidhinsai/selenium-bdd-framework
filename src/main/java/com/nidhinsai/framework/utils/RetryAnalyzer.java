package com.nidhinsai.framework.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retries flaky tests up to MAX_RETRY times before marking them as failed.
 * Wire into any test method: @Test(retryAnalyzer = RetryAnalyzer.class)
 * Or use via the TestNG listener approach.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger LOG = LogManager.getLogger(RetryAnalyzer.class);
    private static final int MAX_RETRY = Integer.parseInt(
            System.getProperty("test.retry.count", "2"));

    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY) {
            retryCount++;
            LOG.warn("Retrying test '{}' — attempt {}/{}", result.getName(), retryCount, MAX_RETRY);
            return true;
        }
        LOG.error("Test '{}' failed after {} retries", result.getName(), MAX_RETRY);
        return false;
    }
}
