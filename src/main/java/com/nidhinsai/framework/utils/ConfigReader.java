package com.nidhinsai.framework.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class ConfigReader {
    private static final Properties PROPERTIES = new Properties();

    static {
        load("config/config.properties");
        load("config/" + System.getProperty("env", "qa") + ".properties");
    }

    private ConfigReader() {
    }

    private static void load(String path) {
        try (FileInputStream fis = new FileInputStream(path)) {
            PROPERTIES.load(fis);
        } catch (IOException ignored) {
        }
    }

    public static String get(String key, String defaultValue) {
        String override = System.getProperty(key);
        if (override != null && !override.isBlank()) {
            return override;
        }
        return PROPERTIES.getProperty(key, defaultValue);
    }
}