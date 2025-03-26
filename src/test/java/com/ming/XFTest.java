package com.ming;

import com.google.gson.JsonObject;
import com.ming.util.http.OkHttpUtil;
import com.ming.xf.AuthUtil;
import com.ming.xf.XfConstant;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 科大讯飞
 * https://www.xfyun.cn/doc/spark/character_simulation/%E8%A7%92%E8%89%B2%E6%A8%A1%E6%8B%9FWeb%E6%96%87%E6%A1%A3.html#%E4%BA%8C%E3%80%81%E9%89%B4%E6%9D%83%E8%AE%A4%E8%AF%81
 * @Author liming
 * @Date 2024/7/19 17:58
 */
public class XFTest {

    String appId = "fc319695";
    String secret = "ODhiODA5YjFjNTQ1ZjgzNjc4NmE0YmNi";
    Long timestamp = System.currentTimeMillis();

    @Test
    public void register(){
        String signature = AuthUtil.getSignature(XfConstant.appId, XfConstant.secret, timestamp);

        Map<String, Object> params = new HashMap<>();
        params.put("appId",appId);
        params.put("playerName","测试");


        LinkedHashMap<String, String> headMap = new LinkedHashMap<>();
        headMap.put("appId",appId);
        headMap.put("timestamp", String.valueOf(timestamp));
        headMap.put("signature",signature);

        String url ="https://ai-character.xfyun.cn/api/open/player/if-register";
        String s = OkHttpUtil.httpGetAndHead(url, params, headMap);
        System.out.println(s);
    }



    @Test
    public void playerAccount(){
        String signature = AuthUtil.getSignature(XfConstant.appId, XfConstant.secret, timestamp);

        Map<String, Object> params = new HashMap<>();
        params.put("appId",appId);
        params.put("playerName","测试2");
        params.put("playerType","玩家类型");
        params.put("description","玩家描述");
        params.put("senderIdentity","玩家职业身份（300字符以内，描述玩家的职业身份、和人格的关系、使命职责等）");


        LinkedHashMap<String, String> headMap = new LinkedHashMap<>();
        headMap.put("appId",appId);
        headMap.put("timestamp", String.valueOf(timestamp));
        headMap.put("signature",signature);

        String url ="https://ai-character.xfyun.cn/api/open/player/register";

        JsonObject jsonObject = OkHttpUtil.sendPostByJsonAndHeader(url, params, headMap);
        System.out.println(jsonObject);
    }


    /**
     * 基于 selenium webdrivermanager
     *         <dependency>
     *             <groupId>org.seleniumhq.selenium</groupId>
     *             <artifactId>selenium-java</artifactId>
     *             <version>3.141.59</version>
     *         </dependency>
     *
     *         <!-- ChromeDriver 自动管理 -->
     *         <dependency>
     *             <groupId>io.github.bonigarcia</groupId>
     *             <artifactId>webdrivermanager</artifactId>
     *             <version>5.6.3</version>
     *         </dependency>
     *
     *         <dependency>
     *             <groupId>org.apache.httpcomponents.client5</groupId>
     *             <artifactId>httpclient5</artifactId>
     *             <version>5.2</version> <!-- 使用最新稳定版本 -->
     *         </dependency>
     *
     * 1、设置 ChromeDriver: 下载与浏览器版本匹配的 ChromeDriver，并将其路径添加到系统环境变量或代码中。
     * 2、打开目标页面: 使用 Selenium 打开目标用户的 GitHub 主页。
     * 3、等待页面加载: 使用显式等待（WebDriverWait）确保 "Repositories" 部分加载完成。
     * 4、提取项目名称: 定位所有项目名称所在的元素，并提取其文本内容。
     * @param args
     */
    public static void main(String[] args) {
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

    // 数据模型类
    static class ItemData {
        String title;
        String price;
        String link;

        public ItemData(String title, String price, String link) {
            this.title = title;
            this.price = price;
            this.link = link;
        }

        @Override
        public String toString() {
            return String.format("标题：%s | 价格：%s | 链接：%s", title, price, link);
        }
    }
}
