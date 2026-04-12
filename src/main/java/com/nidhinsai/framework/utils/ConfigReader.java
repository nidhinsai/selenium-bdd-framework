package com.nidhinsai.framework.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Thread-safe, immutable config reader.
 * Load order: base config.properties → env-specific override (e.g. qa.properties).
 * System properties always win over file values.
 */
public final class ConfigReader {

    private static final Logger LOG = LogManager.getLogger(ConfigReader.class);
    private static final Properties PROPERTIES = new Properties();

    static {
        load("config/config.properties");
        String env = System.getProperty("env", "qa");
        load("config/" + env + ".properties");
        LOG.info("ConfigReader initialised for env='{}'", env);
    }

    private ConfigReader() {
    }

    /**
     * Loads a properties file from the classpath (jar-safe).
     * Falls back to file-system lookup so IDE runs also work.
     */
    private static void load(String resourcePath) {
        // 1. Try classpath (works in jar / fat-jar / CI)
        try (InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is != null) {
                PROPERTIES.load(is);
                LOG.debug("Loaded config from classpath: {}", resourcePath);
                return;
            }
        } catch (IOException e) {
            LOG.warn("IOException while reading classpath resource '{}': {}", resourcePath, e.getMessage());
        }

        // 2. Try file-system relative path (Maven convention from project root)
        java.nio.file.Path fsPath = java.nio.file.Paths.get(resourcePath);
        if (java.nio.file.Files.exists(fsPath)) {
            try (InputStream is = java.nio.file.Files.newInputStream(fsPath)) {
                PROPERTIES.load(is);
                LOG.debug("Loaded config from file-system: {}", resourcePath);
            } catch (IOException e) {
                LOG.warn("IOException while reading file-system resource '{}': {}", resourcePath, e.getMessage());
            }
        } else {
            LOG.warn("Config resource not found (neither classpath nor file-system): {}", resourcePath);
        }
    }

    public static String get(String key, String defaultValue) {
        String override = System.getProperty(key);
        if (override != null && !override.isBlank()) {
            return override;
        }
        return PROPERTIES.getProperty(key, defaultValue);
    }

    /** Convenience getter that throws if a required key is missing. */
    public static String getRequired(String key) {
        String value = get(key, null);
        if (value == null) {
            throw new IllegalStateException("Required config key not found: '" + key + "'");
        }
        return value;
    }
}