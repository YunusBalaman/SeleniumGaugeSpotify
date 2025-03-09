package com.spotify.web.driver;

import com.spotify.web.methods.devtools.ResponseReceivedData;
import com.spotify.web.methods.devtools.utils.ProfilerPreciseCoverage;
import com.spotify.web.methods.devtools.utils.debuggerScriptParsed.DebuggerScriptParsed;
import com.spotify.web.methods.devtools.utils.networkRequestWillBeSent.RequestWillBeSentData;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.CdpInfo;
import org.openqa.selenium.devtools.CdpVersionFinder;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.noop.NoOpCdpInfo;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class SeleniumDevtools {

    public static ConcurrentHashMap<String, RequestWillBeSentData> requestWillBeSentMap = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, ResponseReceivedData> responseReceivedMap = new ConcurrentHashMap<>();
    public static DevTools devTools;
    public static Integer devtoolsVersion;
    public static CopyOnWriteArrayList<String> requestIdList = new CopyOnWriteArrayList<>();
    public static int requestIdListCurrentProcessNumber = 0;
    public static TreeMap<Long, List<String>> timeMillisAndRequestIdMap = new TreeMap<>();
    public static CopyOnWriteArrayList<ProfilerPreciseCoverage> clientCoverageList = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<String> executionContextsClearedList = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<DebuggerScriptParsed> debuggerScriptParsedList = new CopyOnWriteArrayList<>();

    public SeleniumDevtools() {

    }

    public static void clearMaps(){

        requestWillBeSentMap.clear();
        responseReceivedMap.clear();
        requestIdList.clear();
        requestIdListCurrentProcessNumber = 0;
        timeMillisAndRequestIdMap.clear();
    }

    public static void clearRequestIdList(){

        requestIdList.clear();
    }
    
    public static void setDevTools() {

        switch (Driver.browser) {
            case "edge":
                devTools = ((EdgeDriver) Driver.webDriver).getDevTools();
                break;
            case "chrome":
                devTools = ((ChromeDriver) Driver.webDriver).getDevTools();
                break;
            default:
                Assertions.fail(Driver.browser + "browser desteklenmemektedir");
                /**
                 Augmenter augmenter = new Augmenter();
                 WebDriver driver = augmenter.augment(Driver.webDriver);
                 devTools = ((HasDevTools) driver).getDevTools();
                 */
        }
        devTools.createSession();
        devtoolsVersion = getDevtoolsVersion();
        System.out.println("Devtools Version: " + devtoolsVersion);
    }

    public static void clearListeners(){

        SeleniumDevtools.devTools.clearListeners();
    }

    public static void closeDevtools(){

        devTools.disconnectSession();
        devTools.close();
    }

    public static Integer getDevtoolsVersion(){

        if (Driver.browser.equals("firefox")){
            return 85;
        }
        Capabilities capabilities = ((RemoteWebDriver) Driver.webDriver).getCapabilities();
        CdpInfo cdpInfo = new CdpVersionFinder()
                .match(capabilities.getBrowserVersion())
                .orElseGet(NoOpCdpInfo::new);
       return cdpInfo.getMajorVersion();
    }

}

