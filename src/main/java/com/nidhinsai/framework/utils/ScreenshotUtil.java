package com.nidhinsai.framework.utils;

import com.nidhinsai.framework.base.DriverFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * Captures screenshots and saves them to the evidence directory.
 * Returned bytes are ready to be attached directly into Cucumber reports.
 */
public final class ScreenshotUtil {

    private static final Logger LOG = LogManager.getLogger(ScreenshotUtil.class);
    private static final String SCREENSHOT_DIR = "test-output/screenshots";
    private static final DateTimeFormatter TIMESTAMP_FMT =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtil() {
    }

    /**
     * Takes a screenshot using the current thread's WebDriver.
     *
     * @param label A descriptive label used as the filename prefix (scenario name, test name, etc.)
     * @return PNG bytes of the screenshot, or an empty array if the driver is unavailable.
     */
    public static byte[] capture(String label) {
        WebDriver driver = DriverFactory.getDriver();
        if (driver == null) {
            LOG.warn("Screenshot requested but no active WebDriver for thread {}", Thread.currentThread().getName());
            return new byte[0];
        }

        try {
            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            String safeLabel = sanitise(label);
            String timestamp = LocalDateTime.now().format(TIMESTAMP_FMT);
            String fileName = timestamp + "_" + safeLabel + ".png";

            Path dir = Paths.get(SCREENSHOT_DIR);
            Files.createDirectories(dir);
            Path target = dir.resolve(fileName);
            Files.write(target, bytes);

            LOG.info("Screenshot saved → {}", target.toAbsolutePath());
            return bytes;
        } catch (IOException e) {
            LOG.error("Failed to save screenshot for '{}': {}", label, e.getMessage(), e);
            return new byte[0];
        }
    }

    /**
     * Convenience overload that also accepts a WebDriver explicitly (useful in non-Cucumber tests).
     */
    public static byte[] capture(WebDriver driver, String label) {
        if (driver == null) {
            LOG.warn("Screenshot requested with null WebDriver for label '{}'", label);
            return new byte[0];
        }
        try {
            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            String safeLabel = sanitise(label);
            String timestamp = LocalDateTime.now().format(TIMESTAMP_FMT);
            String fileName = timestamp + "_" + safeLabel + ".png";

            Path dir = Paths.get(SCREENSHOT_DIR);
            Files.createDirectories(dir);
            Path target = dir.resolve(fileName);
            Files.write(target, bytes);

            LOG.info("Screenshot saved → {}", target.toAbsolutePath());
            return bytes;
        } catch (IOException e) {
            LOG.error("Failed to save screenshot for '{}': {}", label, e.getMessage(), e);
            return new byte[0];
        }
    }

    // Replace characters that are invalid in file names with underscores.
    private static String sanitise(String input) {
        if (input == null || input.isBlank()) {
            return "unnamed";
        }
        return input.replaceAll("[^a-zA-Z0-9._-]", "_").replaceAll("_+", "_");
    }
}
