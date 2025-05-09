package com.ming;

import com.ming.util.seleniums.WebDriverUtil;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * @Description
 * @Author liming
 * @Date 2025/4/7 10:33
 */
@Slf4j
public class SeleniumTest {

    /**
     * 自动下载chromedriver
     */
    @Test
    public void autoWebDriver() {
        // 自动下载并配置 ChromeDriver
        WebDriverManager.chromedriver().setup();

        // 配置浏览器选项（可选）
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");                // 无头模式（不显示浏览器界面）
        options.addArguments("--disable-gpu");            // 禁用GPU加速
        options.addArguments("--disable-images");         // 禁用图片加载（提升速度）

        // 初始化 WebDriver
        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, 20); // 显式等待

        try {
            // 1. 打开目标网页
            String url = "https://github.com/liming91";
            driver.get(url);

            // 2. 等待列表容器加载完成
            By listContainerSelector = By.id("367036522");
            WebElement repositoriesSection = wait.until(ExpectedConditions.presenceOfElementLocated(listContainerSelector));

            List<WebElement> repoSpans = repositoriesSection.findElements(By.cssSelector("a.min-width-0 span.repo"));
            // 提取并打印项目名称
            System.out.println("用户 liming91 的项目列表：");
            for (WebElement span : repoSpans) {
                System.out.println(span.getText().trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭浏览器
            driver.quit();
        }
    }


    /**
     * 支持谷歌火狐下载驱动
     */
    public void downloadChromeDriver(){
        try {
            // 1. 初始化环境
            WebDriverUtil.initEnvironment();

            // 2. 安装浏览器并关闭更新（只需执行一次）
            WebDriverUtil.installChromeBrowser();

            // 3. 下载指定版本的 ChromeDriver（例如 114.0.5735.90）
            WebDriverUtil.downloadChromeDriver("114.0.5735.90");

            // 4. 创建 WebDriver 实例
            WebDriver driver = WebDriverUtil.createChromeDriver();

            // 5. 执行测试操作
            driver.get("https://www.google.com");
            System.out.println("页面标题: " + driver.getTitle());

            // 6. 关闭浏览器
            driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        String path = "src\\main\\resources\\chromedriver.exe";
//        System.setProperty("webdriver.chrome.driver", path);
//        ChromeDriver driver = new ChromeDriver();
//        WebDriverWait wait = new WebDriverWait(driver, 20); // 显式等待


    }

}
