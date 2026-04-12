package com.nidhinsai.framework.utils;

import com.nidhinsai.framework.base.DriverFactory;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

/**
 * Full-evidence capture: screenshot + page source + browser console logs.
 * Output lands in {@code test-output/evidence/{label}/}.
 */
public final class EvidenceCapture {

    private static final Logger LOG = LogManager.getLogger(EvidenceCapture.class);
    private static final String EVIDENCE_DIR = "test-output/evidence";
    private static final DateTimeFormatter TIMESTAMP_FMT =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private EvidenceCapture() {
    }

    /**
     * Captures a screenshot AND the page HTML source for the current thread's driver.
     * Both files share the same timestamp-based directory so they're easy to correlate.
     *
     * @param label Descriptive label (scenario name, test name, etc.)
     * @return Screenshot PNG bytes (also written to disk), empty array on failure.
     */
    public static byte[] captureAll(String label) {
        WebDriver driver = DriverFactory.getDriver();
        if (driver == null) {
            LOG.warn("Evidence capture requested but no active WebDriver for thread {}",
                    Thread.currentThread().getName());
            return new byte[0];
        }

        String safeLabel  = sanitise(label);
        String timestamp  = LocalDateTime.now().format(TIMESTAMP_FMT);
        Path evidenceDir  = Paths.get(EVIDENCE_DIR, timestamp + "_" + safeLabel);

        try {
            Files.createDirectories(evidenceDir);
        } catch (IOException e) {
            LOG.error("Cannot create evidence directory {}: {}", evidenceDir, e.getMessage());
            return new byte[0];
        }

        // 1. Screenshot
        byte[] png = ScreenshotUtil.capture(label);
        if (png.length > 0) {
            try {
                Files.write(evidenceDir.resolve("screenshot.png"), png);
                LOG.debug("[Evidence] Screenshot → {}/screenshot.png", evidenceDir);
            } catch (IOException e) {
                LOG.warn("Could not write screenshot to evidence dir: {}", e.getMessage());
            }
        }

        // 2. Page source
        try {
            String source = driver.getPageSource();
            if (source != null) {
                Path sourceFile = evidenceDir.resolve("page_source.html");
                Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
                LOG.debug("[Evidence] Page source → {}", sourceFile);
            }
        } catch (Exception e) {
            LOG.warn("Could not capture page source: {}", e.getMessage());
        }

        // 3. Current URL
        try {
            String url = driver.getCurrentUrl();
            Path urlFile = evidenceDir.resolve("current_url.txt");
            Files.writeString(urlFile, url != null ? url : "(unavailable)", StandardCharsets.UTF_8);
            LOG.debug("[Evidence] Current URL → {} → {}", url, urlFile);
        } catch (Exception e) {
            LOG.warn("Could not capture current URL: {}", e.getMessage());
        }

        LOG.info("[Evidence] Full evidence package written to {}", evidenceDir.toAbsolutePath());
        return png;
    }

    private static String sanitise(String input) {
        if (input == null || input.isBlank()) {
            return "unnamed";
        }
        return input.replaceAll("[^a-zA-Z0-9._-]", "_").replaceAll("_+", "_");
    }
}
