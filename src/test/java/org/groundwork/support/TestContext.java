// Created by Viacheslav (Slava) Skryabin 04/01/2011

/*
MIT License

Copyright (c) 2018 Viacheslav (Slava) Skryabin

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

package org.groundwork.support;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverLogLevel;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverService;
import org.openqa.selenium.safari.SafariDriver;
import static org.groundwork.support.DateHelper.nowWithTime;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestContext {

    private static WebDriver driver;

    public static WebDriver getDriver() {
        return driver;
    }

    public static Config getConfig() {
        Config config = null;
        try {
            config = new YAMLMapper().readValue(getStream("config"), Config.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }

    public static InputStream getStream(String fileBasename) {
        ClassLoader classLoader = TestContext.class.getClassLoader();
        String fileName = fileBasename + ".yml";
        // read file[s] from a resources folder or root of the classpath; works in JAR file
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }

    //start browser
    public static void initialize() {
        initialize(getConfig().getBrowser(), getConfig().getEnvironment(), getConfig().getHeadless());
    }

    public static void teardown() {
        driver.quit();
    }

    public static void initialize(String browser, String testEnv, boolean isHeadless) {
        Dimension size = new Dimension(getConfig().getBrowserWidth(),getConfig().getBrowserHeight());
        Point position = new Point(0, 0);
        if (testEnv.equals("local")) {
            switch (browser) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    Map<String, Object> chromePreferences = new HashMap<>();
                    chromePreferences.put("profile.default_content_settings.geolocation", 2);
                    chromePreferences.put("profile.default_content_settings.popups", 0);
                    chromePreferences.put("download.prompt_for_download", false);
                    chromePreferences.put("download.directory_upgrade", true);
                    chromePreferences.put("download.default_directory", System.getProperty("user.dir") + "/src/test/resources/downloads");
                    chromePreferences.put("safebrowsing.enabled", false);
                    chromePreferences.put("plugins.always_open_pdf_externally", true);
                    chromePreferences.put("plugins.plugins_disabled", new ArrayList<String>(){{ add("Chrome PDF Viewer"); }});
                    chromePreferences.put("credentials_enable_service", false);
                    chromePreferences.put("password_manager_enabled", false);
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--start-maximized");
                    chromeOptions.setExperimentalOption("prefs", chromePreferences);
                    chromeOptions.addArguments("--remote-allow-origins=*");
                    System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY,
                                                           System.getProperty("user.dir") + "/target/chromedriver.log");
//                                                           DriverService.LOG_STDOUT);
//                    chromeOptions.setLogLevel(ChromeDriverLogLevel.DEBUG);

                    if (isHeadless) {
                        chromeOptions.addArguments("--headless=new");
                        chromeOptions.addArguments("--window-size=" + size.getWidth() + "," + size.getWidth());
                        chromeOptions.addArguments("--disable-gpu");
                        chromeOptions.addArguments("--no-sandbox");
                        System.setProperty("webdriver.chrome.whitelistedIps", "");
                    }
                    driver = new ChromeDriver(chromeOptions);
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    if (isHeadless) {
                        FirefoxBinary firefoxBinary = new FirefoxBinary();
                        firefoxBinary.addCommandLineOptions("--headless");
                        firefoxOptions.setBinary(firefoxBinary);
                    }
                    System.setProperty(GeckoDriverService.GECKO_DRIVER_LOG_PROPERTY,
                                                          System.getProperty("user.dir") + "/target/geckodriver.log");
//                                                          DriverService.LOG_STDOUT);
//                    firefoxOptions.setLogLevel(FirefoxDriverLogLevel.DEBUG);
                    driver = new FirefoxDriver(firefoxOptions);
                    break;
                case "safari":
                    driver = new SafariDriver();
                    driver.manage().window().setPosition(position);
                    break;
                case "edge":
                    WebDriverManager.edgedriver().setup();
                    driver = new EdgeDriver();
                    break;
                case "ie":
                    WebDriverManager.iedriver().setup();
                    driver = new InternetExplorerDriver();
                    break;
                default:
                    throw new RuntimeException("Driver is not implemented for: " + browser);
            }
            if ((size.getWidth() >= 0) && (size.getHeight() >= 0)) {
                driver.manage().window().setSize(size);
            } else {
                driver.manage().window().maximize();
            }
        } else if (testEnv.equals("grid")){
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setBrowserName(browser);
            capabilities.setPlatform(Platform.ANY);
            capabilities.setCapability("enableVNC",true); // Selenoid supports showing browser screen during test execution.
            capabilities.setCapability("enableVideo",true); // To enable video recording for session
            capabilities.setCapability("videoName","selenoid-video-"+ nowWithTime()+".mp4"); // To enable video recording for session
            capabilities.setCapability("enableLog",true); // To enable saving logs for a session
            switch (browser) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--start-maximized");
                    capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
                    break;
                case "firefox":
                    break;
                default:
                    throw new RuntimeException("Driver is not implemented for: " + browser);
            }
            try {
                URL hubUrl = new URL(getConfig().getHub());
                driver = new RemoteWebDriver(hubUrl, capabilities);
                driver.manage().window().maximize();
                ((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            throw new RuntimeException("Unsupported test environment: " + testEnv);
        }
    }

    public static String getExperimentsUrl() {
        if (getConfig().getEnvironment().equals("local")) {
            return "http://localhost:8889";
        }
        // running from Jenkins' dockerized agent on the 'selenoid' bridge network with alias 'agent'
        // selenoid and browsers by default are also connected to the docker's 'selenoid' bridge network
        if (!getConfig().getHub().contains("localhost")) {
            return "http://agent:8889";
        }
        // running from IDE, containerized browser should reach localhost:8889 of the host
        // selenoid's browser configurations in browsers.json should have added
        // "hosts" : ["host.docker.internal:host-gateway"]
        // use "--browsers-json [dir_name]/browsers.json --config-dir [dir_name]" of selenoid configuration manager
        return "http://host.docker.internal:8889";
    }
}