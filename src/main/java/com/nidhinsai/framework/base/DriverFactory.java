package com.nidhinsai.framework.base;

import com.nidhinsai.framework.utils.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public final class DriverFactory {

    private static final Logger LOG = LogManager.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    public static void initDriver() {
        String browser = ConfigReader.get("browser", "chrome").toLowerCase();
        boolean headless = Boolean.parseBoolean(ConfigReader.get("headless", "false"));
        LOG.info("Initialising WebDriver — browser={} headless={} thread={}", browser, headless,
                Thread.currentThread().getName());

        WebDriver driver;
        switch (browser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("-headless");
                }
                driver = new FirefoxDriver(firefoxOptions);
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) {
                    chromeOptions.addArguments(
                            "--headless=new",
                            "--window-size=1920,1080",  // maximize equivalent for headless
                            "--disable-gpu"
                    );
                }
                chromeOptions.addArguments(
                        "--no-sandbox",
                        "--disable-dev-shm-usage",
                        "--remote-allow-origins=*"
                );
                driver = new ChromeDriver(chromeOptions);
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(
                Integer.parseInt(ConfigReader.get("app.timeout.implicit", "5"))));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(
                Integer.parseInt(ConfigReader.get("app.timeout.pageLoad", "30"))));
        // maximize() throws in some headless Chrome versions; use window-size arg instead
        if (!headless) {
            driver.manage().window().maximize();
        }
        DRIVER.set(driver);
        LOG.info("WebDriver ready — session started on thread {}", Thread.currentThread().getName());
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            LOG.info("Quitting WebDriver on thread {}", Thread.currentThread().getName());
            driver.quit();
            DRIVER.remove();
        }
    }
}