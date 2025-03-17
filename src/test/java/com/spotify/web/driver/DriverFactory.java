package com.spotify.web.driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class DriverFactory {

    private static final Logger logger = LogManager.getLogger(DriverFactory.class);

    static WebDriver getLocalDriver() {

        WebDriver webDriver = null;
        String browserName = Driver.browser;
        switch (browserName){
            case "chrome":
                webDriver = getChromeDriver();
                break;
            case "edge":
                webDriver = getEdgeDriver();
                break;
            case "firefox":
                webDriver = getFirefoxDriver();
                break;
            case "safari":
                webDriver = getSafariDriver();
                break;
            default:
                throw new NullPointerException(browserName + " driver desteklenmiyor");
        }
        return webDriver;
    }
    private static ChromeDriver getChromeDriver(){

        DesiredCapabilities capabilities = new DesiredCapabilities();
        ChromeOptions options = new ChromeOptions();

        //LoggingPreferences logPrefs = new LoggingPreferences();
        //logPrefs.enable( LogType.PERFORMANCE, Level.ALL );
        //logPrefs.enable( LogType.BROWSER, Level.ALL );
       // options.setCapability( "goog:loggingPrefs", logPrefs );

        options.addArguments("--disable-blink-features");
        options.addArguments("--disable-blink-features=AutomationControlled");
       // options.addArguments("disable-extensions");
        options.addArguments("disable-translate");
        options.addArguments("disable-plugins");
        options.addArguments("--disable-notifications");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("disable-infobars");
        options.addArguments("--disable-popup-blocking");
        options.setExperimentalOption("excludeSwitches", List.of("enable-automation"));
        options.setExperimentalOption("useAutomationExtension",false);
        options.addArguments("--start-maximized");
        options.addArguments("--disable-web-security");
        options.addArguments("--no-proxy-server");
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
       // prefs.put("intl.accept_languages", "en-GB");
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--remote-allow-origins=*");
        //options.addArguments("--headless=new");
        //options.addArguments("--app=https://www.google.com");
        //options.addArguments("--kiosk");
        //options.addArguments("--incognito");
        //   options.addArguments("--remote-debugging-port=9222");
        //   options.addArguments("--remote-debugging-address=127.0.0.1");
        options = options.merge(capabilities);
        return new ChromeDriver(options);
    }

    private static EdgeDriver getEdgeDriver(){

        DesiredCapabilities capabilities = new DesiredCapabilities();
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.addArguments("disable-translate");
        edgeOptions.addArguments("disable-plugins");
        edgeOptions.addArguments("--disable-notifications");
        edgeOptions.addArguments("--ignore-certificate-errors");
        edgeOptions.addArguments("--disable-dev-shm-usage");
        edgeOptions.addArguments("--no-sandbox");
        edgeOptions.addArguments("--disable-gpu");
        edgeOptions.addArguments("--disable-infobars");
        edgeOptions.addArguments("--disable-popup-blocking");
        edgeOptions.setExperimentalOption("excludeSwitches", List.of("enable-automation"));
        //edgeOptions.addArguments("--disable-features=msHubApps");
        Map<String,Object> prefs = new HashMap<>();
        prefs.put("browser.show_hub_apps_tower", false);
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        edgeOptions.setExperimentalOption("prefs", prefs);
        edgeOptions.setExperimentalOption("useAutomationExtension",false);
        edgeOptions.addArguments("--remote-allow-origins=*");
       // edgeOptions.addArguments("--headless=new");
        //edgeOptions.addArguments("--start-maximized");
        //edgeOptions.addArguments("--disable-web-security");
        //edgeOptions.addArguments("--no-proxy-server");
        //edgeOptions.addArguments("--no-default-browser-check");
        //edgeOptions.addArguments("--suppress-message-center-popups");
        //edgeOptions.addArguments("--app=https://www.google.com");
        //edgeOptions.addArguments("inprivate");
        //edgeOptions.addArguments("--inprivate");
        //"--save-page-as-mhtml"
        edgeOptions = edgeOptions.merge(capabilities);
        return new EdgeDriver(edgeOptions);
    }

    private static WebDriver getFirefoxDriver(){

        DesiredCapabilities capabilities = new DesiredCapabilities();
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions = firefoxOptions.merge(capabilities);
        return new FirefoxDriver(firefoxOptions);
    }

    private static SafariDriver getSafariDriver(){

        DesiredCapabilities capabilities = new DesiredCapabilities();
        SafariOptions safariOptions = new SafariOptions();
        //safariOptions.setCapability("safari.cleanSession", true);
        //safariOptions.setUseTechnologyPreview(true);
        // options
        safariOptions = safariOptions.merge(capabilities);
        return new SafariDriver(safariOptions);
    }

    static WebDriver getRemoteDriver(String key){

        RemoteWebDriver webDriver = null;

        String browserName = Driver.browser;
        logger.info("Browser:" + Driver.browser);
        Capabilities options;
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("key", key);
        switch (browserName){
            case "chrome":
                options = getRemoteChromeDriver(capabilities);
                break;
            case "edge":
                options = getRemoteEdgeDriver(capabilities);
                break;
            case "firefox":
                options = getRemoteFirefoxDriver(capabilities);
                break;
            case "safari":
                options = getRemoteSafariDriver(capabilities);
                break;
            default:
                throw new NullPointerException(browserName + " driver desteklenmiyor");
        }
        try {
           webDriver = new RemoteWebDriver(new URL(""), options);
        } catch (MalformedURLException e) {
            throw new WebDriverException(e);
        }
        webDriver.setFileDetector(new LocalFileDetector());
        return webDriver;
    }

    private static ChromeOptions getRemoteChromeDriver(DesiredCapabilities capabilities){

        ChromeOptions options = new ChromeOptions();
        options.addArguments("disable-translate");
        options.addArguments("disable-plugins");
        options.addArguments("--disable-notifications");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("disable-infobars");
        options.setExperimentalOption("excludeSwitches", List.of("disable-popup-blocking", "enable-automation"));
        options.setExperimentalOption("useAutomationExtension",false);
        options.addArguments("--start-maximized");
        options.addArguments("--disable-web-security");
        options.addArguments("--no-proxy-server");
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--remote-allow-origins=*");
        options = options.merge(capabilities);
        return options;
    }

    private static EdgeOptions getRemoteEdgeDriver(DesiredCapabilities capabilities){

        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.addArguments("disable-translate");
        edgeOptions.addArguments("disable-plugins");
        edgeOptions.addArguments("--disable-notifications");
        edgeOptions.addArguments("--ignore-certificate-errors");
        edgeOptions.addArguments("--disable-dev-shm-usage");
        edgeOptions.addArguments("--no-sandbox");
        edgeOptions.addArguments("--disable-gpu");
        edgeOptions.addArguments("--disable-infobars");
        edgeOptions.setExperimentalOption("excludeSwitches", List.of("disable-popup-blocking", "enable-automation"));
        Map<String,Object> prefs = new HashMap<>();
        prefs.put("browser.show_hub_apps_tower", false);
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        edgeOptions.setExperimentalOption("prefs", prefs);
        edgeOptions.setExperimentalOption("useAutomationExtension",false);
        edgeOptions.addArguments("--remote-allow-origins=*");
        edgeOptions = edgeOptions.merge(capabilities);
        return edgeOptions;
    }

    private static FirefoxOptions getRemoteFirefoxDriver(DesiredCapabilities capabilities){

        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions = firefoxOptions.merge(capabilities);
        return firefoxOptions;
    }

    private static SafariOptions getRemoteSafariDriver(DesiredCapabilities capabilities){

        SafariOptions safariOptions = new SafariOptions();
        //safariOptions.setCapability("safari.cleanSession", true);
        //safariOptions.setUseTechnologyPreview(true);
        // options
        safariOptions = safariOptions.merge(capabilities);
        return safariOptions;
    }
}
