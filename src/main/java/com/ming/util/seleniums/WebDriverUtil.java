package com.ming.util.seleniums;

import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;


public class WebDriverUtil {
    private static final String CHROME_DRIVER_BASE_URL = "https://chromedriver.storage.googleapis.com/";
    private static final String CHROME_BASE_URL = "https://dl.google.com/linux/direct/google-chrome-stable_current_";
    private static final String DRIVER_DIR = "drivers";
    private static final String BROWSER_DIR = "browsers";

    /**
     * 初始化环境：创建目录结构
     */
    public static void initEnvironment() throws IOException {
        Files.createDirectories(Paths.get(DRIVER_DIR));
        Files.createDirectories(Paths.get(BROWSER_DIR));
    }

    /**
     * 获取当前操作系统类型
     */
    private static String getOS() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return "win";
        } else if (osName.contains("linux")) {
            return "linux";
        } else {
            throw new UnsupportedOperationException("Unsupported OS: " + osName);
        }
    }

    /**
     * 下载指定版本的 ChromeDriver
     */
    public static void downloadChromeDriver(String version) throws IOException {
        String os = getOS();
        String driverUrl = CHROME_DRIVER_BASE_URL + version + "/chromedriver_" + getDriverOSExtension(os) + ".zip";
        String driverPath = downloadAndExtract(driverUrl, DRIVER_DIR, "chromedriver");
        setExecutablePermission(driverPath);
    }

    /**
     * 下载最新版 Chrome 浏览器并关闭自动更新
     */
    public static void installChromeBrowser() throws IOException {
        String os = getOS();
        String chromeUrl;
        if ("win".equals(os)) {
            chromeUrl = "https://dl.google.com/tag/s/dl/chrome/install/ChromeStandaloneSetup64.exe";
        } else {
            chromeUrl = CHROME_BASE_URL + "amd64.deb";
        }
        String installerPath = downloadFile(chromeUrl, BROWSER_DIR);
        installBrowser(installerPath);
        disableBrowserAutoUpdate();
    }

    /**
     * 获取 ChromeDriver 路径
     */
    public static String getChromeDriverPath() {
        String os = getOS();
        return Paths.get(DRIVER_DIR, "chromedriver" + ("win".equals(os) ? ".exe" : "")).toString();
    }

    /**
     * 创建 Chrome WebDriver 实例
     */
    public static WebDriver createChromeDriver() {
        System.setProperty("webdriver.chrome.driver", getChromeDriverPath());
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-browser-side-navigation");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        return new ChromeDriver(options);
    }

    //----------------------------- 私有工具方法 -----------------------------
    private static String getDriverOSExtension(String os) {
        return "win".equals(os) ? "win32" : "linux64";
    }

    private static String downloadFile(String url, String targetDir) throws IOException {
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        Path targetPath = Paths.get(targetDir, fileName);
        Path tempPath = Files.createTempFile(Paths.get(targetDir), "chrome-download-", ".tmp");

        try {
            FileUtils.copyURLToFile(new URL(url), tempPath.toFile());
            if (Files.exists(targetPath)) {
                Files.delete(targetPath); // 删除旧文件
            }
            Files.move(tempPath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            Files.deleteIfExists(tempPath);
            throw e;
        }

        return targetPath.toString();
    }

    private static String downloadAndExtract(String url, String targetDir, String binaryName) throws IOException {
        String zipPath = downloadFile(url, targetDir);
        Path extractDir = Paths.get(targetDir, "temp");
        FileUtils.forceMkdir(extractDir.toFile());

        // 解压 ZIP 文件
        try (ZipFile zipFile = new ZipFile(zipPath)) {
            Enumeration<? extends ZipArchiveEntry> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                Path entryPath = extractDir.resolve(entry.getName());

                if (entry.isDirectory()) {
                    Files.createDirectories(entryPath);
                } else {
                    // 如果文件已存在，先删除
                    if (Files.exists(entryPath)) {
                        Files.delete(entryPath);
                    }
                    try (InputStream is = zipFile.getInputStream(entry)) {
                        Files.copy(is, entryPath);
                    }
                }
            }
        }



        // 查找二进制文件
        Path binaryPath = findBinary(extractDir, binaryName);

        // 移动到目标目录
        Path targetPath = Paths.get(targetDir, binaryPath.getFileName().toString());
        Files.move(binaryPath, targetPath, StandardCopyOption.REPLACE_EXISTING);

        // 清理临时目录
        Files.walk(extractDir)
                .sorted(Comparator.reverseOrder())
                .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        return targetPath.toString();
    }


    private static Path findBinary(Path dir, String binaryName) throws IOException {
        String os = getOS();
        String targetName = binaryName + ("win".equals(os) ? ".exe" : ""); // 根据系统补全扩展名
        return Files.walk(dir) // 递归遍历目录
                .filter(path -> {
                    String fileName = path.getFileName().toString();
                    return fileName.equals(targetName) || fileName.equals(binaryName); // 匹配文件名
                })
                .findFirst()
                .orElseThrow(() -> new FileNotFoundException("Binary '" + binaryName + "' not found in extracted files"));
    }

    private static void setExecutablePermission(String path) {
        new File(path).setExecutable(true);
    }

    private static void installBrowser(String installerPath) throws IOException {
        String os = getOS();
        if ("win".equals(os)) {
            Runtime.getRuntime().exec(installerPath + " /silent /install");
        } else {
            Runtime.getRuntime().exec("sudo dpkg -i " + installerPath);
        }
    }

    private static void disableBrowserAutoUpdate() throws IOException {
        String os = getOS();
        if ("win".equals(os)) {
            // Windows: 修改注册表或组策略
            Runtime.getRuntime().exec("reg add HKEY_LOCAL_MACHINE\\SOFTWARE\\Policies\\Google\\Update /v AutoUpdateCheckPeriodMinutes /t REG_DWORD /d 0 /f");
        } else {
            // Linux: 禁用自动更新
            Files.write(Paths.get("/etc/apt/apt.conf.d/99noupdate"),
                    "APT::Periodic::Update-Package-Lists \"0\";\nAPT::Periodic::Unattended-Upgrade \"0\";".getBytes());
        }
    }


}
