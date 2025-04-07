package com.ming;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @Description
 * @Author liming
 * @Date 2025/4/7 10:33
 */
public class SeleniumTest {

    public static void main(String[] args) {
        String path = "src\\main\\resources\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", path);
        ChromeDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 20); // 显式等待
    }

}
