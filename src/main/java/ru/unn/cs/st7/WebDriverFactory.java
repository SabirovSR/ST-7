package ru.unn.cs.st7;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public final class WebDriverFactory {

    private WebDriverFactory() {
    }

    /**
     * Путь к chromedriver: системное свойство {@code webdriver.chrome.driver}, затем
     * переменные окружения {@code WEBDRIVER_CHROME_DRIVER} / {@code CHROMEWEBDRIVER}.
     * Если не задано — используется Selenium Manager (автозагрузка драйвера).
     */
    public static WebDriver createChrome() {
        applyChromeDriverPathFromEnv();
        ChromeOptions options = new ChromeOptions();
        if (useHeadlessChrome()) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1280,900");
        }
        return new ChromeDriver(options);
    }

    private static boolean useHeadlessChrome() {
        if (Boolean.parseBoolean(System.getenv().getOrDefault("HEADLESS", "false"))) {
            return true;
        }
        return System.getenv("GITHUB_ACTIONS") != null;
    }

    private static void applyChromeDriverPathFromEnv() {
        String prop = System.getProperty("webdriver.chrome.driver");
        if (prop != null && !prop.trim().isEmpty()) {
            return;
        }
        String path = firstNonEmpty(
                System.getenv("WEBDRIVER_CHROME_DRIVER"),
                System.getenv("CHROMEWEBDRIVER"));
        if (path != null && !path.trim().isEmpty()) {
            System.setProperty("webdriver.chrome.driver", path.trim());
        }
    }

    private static String firstNonEmpty(String a, String b) {
        if (a != null && !a.trim().isEmpty()) {
            return a;
        }
        if (b != null && !b.trim().isEmpty()) {
            return b;
        }
        return null;
    }
}
