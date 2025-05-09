package com.ming.util.seleniums;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

@Component
@Slf4j
public class GoogleDriverUtil {

    private static final Set<WebDriver> WEB_DRIVERS = Collections.synchronizedSet(new HashSet<>());
    private static final ReentrantLock LOCK = new ReentrantLock();


    /**
     * 获取 Firefox WebDriver 实例
     *
     * @param downloadPath 文件下载路径（可为空）
     */
    public static WebDriver getFirefoxDriver(String downloadPath) {
        LOCK.lock();
        try {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless", "--disable-gpu");
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

            if (StringUtils.isNotBlank(downloadPath)) {
                FirefoxProfile profile = new FirefoxProfile();
                profile.setPreference("browser.download.folderList", 2);
                profile.setPreference("browser.download.dir", downloadPath);
                options.setProfile(profile);
            }

            System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");
            RemoteWebDriver driver = new FirefoxDriver(options);
            WEB_DRIVERS.add(driver);
            log.info("Created FirefoxDriver with sessionId: {}", driver.getSessionId());
            return driver;
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * 获取 Chrome WebDriver 实例
     *
     * @param downloadPath 文件下载路径（可为空）
     */
    public static WebDriver getChromeDriver(String downloadPath) {
        LOCK.lock();
        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments(
                    "--headless",
                    "--disable-gpu",
                    "--remote-allow-origins=*",
                    "--disable-images"
            );
            options.setExperimentalOption("useAutomationExtension", true);

            if (StringUtils.isNotBlank(downloadPath)) {
                options.addArguments("download.default_directory=" + downloadPath);
            }

            System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
            RemoteWebDriver driver = new ChromeDriver(options);
            WEB_DRIVERS.add(driver);
            log.info("Created ChromeDriver with sessionId: {}", driver.getSessionId());
            return driver;
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * 关闭单个 WebDriver
     */
    public static void quitDriver(WebDriver driver) {
        if (driver == null) return;

        LOCK.lock();
        try {
            SessionId sessionId = ((RemoteWebDriver) driver).getSessionId();
            log.info("Closing WebDriver with sessionId: {}", sessionId);
            driver.quit();
            WEB_DRIVERS.remove(driver);
        } catch (Exception e) {
            log.error("Error closing WebDriver", e);
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * Spring 容器关闭时清理所有 WebDriver
     */
    @PreDestroy
    public void destroyAllDrivers() {
        LOCK.lock();
        try {
            log.info("Spring容器关闭时清理所有 WebDriver");
            WEB_DRIVERS.forEach(driver -> {
                try {
                    driver.quit();
                } catch (Exception e) {
                    log.error("Spring容器关闭时清理时Error", e);
                }
            });
            WEB_DRIVERS.clear();
        } finally {
            LOCK.unlock();
        }
    }

}