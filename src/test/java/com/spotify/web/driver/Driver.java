package com.spotify.web.driver;

import com.spotify.web.helper.StoreHelper;
import com.spotify.web.methods.database.ConnectDatabase;
import com.spotify.web.model.ElementInfo;
import com.spotify.web.utils.FindOS;
import com.thoughtworks.gauge.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

public class Driver {

    private static final Logger logger = LogManager.getLogger(Driver.class);
    public static WebDriver webDriver;
    public static String osName = FindOS.getOperationSystemName();
    public static String osNameSpecific = System.getProperty("os.name");
    public static String browser = "chrome";//chrome edge firefox safari
    public static String url = "https://open.spotify.com/";
    public static String baseUri = "";
    public static List<String> baseUriList;
    public static HashMap<String, Object> TestMap;
    public static ConcurrentHashMap<String, ConcurrentHashMap<String,Object>> apiMap;
    public static String userDir = Paths.get("").toAbsolutePath().toString();
    public static String slash = FileSystems.getDefault().getSeparator();
    public static String TestFileName = "";
    public static String TestCaseName = "";
    public static String platformName;
    public static List<String> elementEnviroments = new ArrayList<>();
    public static ElementInfo lastElement;
    public static HashMap<String,Object> DatabaseMap;
    public static long waitElementTimeout = 20;
    public static long pollingEveryValue = 250;
    public static String databaseConnectionMapKeyName = "connectionMapKeyList";

    @BeforeSuite
    public void beforeSuite(ExecutionContext executionContext){

        baseUriList = new ArrayList<>();
        baseUriList.addAll(List.of(new String[]{"{regex}https://[a-z0-9-.]+\\.spotify\\.com/.*"}));
//        baseUriList.addAll(List.of(new String[]{"https://gew4-spclient.spotify.com", "https://api-partner.spotify.com",
//                "https://www.spotify.com/api","https://clienttoken.spotify.com","https://apresolve.spotify.com"
//                ,"https://spclient.wg.spotify.com"}));
        System.out.println(Locale.getDefault());
        Locale.setDefault(Locale.ENGLISH);
        System.out.println(Locale.getDefault());
        logger.info(osNameSpecific);
        logger.info("*************************************************************************");
        logger.info("------------------------TEST PLAN-------------------------");
        TestMap = new HashMap<String, Object>();
        apiMap = new ConcurrentHashMap<String, ConcurrentHashMap<String,Object>>();
        DatabaseMap = new HashMap<String, Object>();
        System.setProperty("polyglot.engine.WarnInterpreterOnly", "false");
        // System.setProperty("webdriver.http.factory", "jdk-http-client");
        Driver.TestMap.put("baseUri", baseUri);
    }

    @BeforeSpec
    public void beforeSpec(ExecutionContext executionContext){

        logger.info("=========================================================================");
        logger.info("------------------------SPEC-------------------------");
        String fileName = executionContext.getCurrentSpecification().getFileName();
        TestFileName = fileName.replace(userDir,"");
        logger.info("SPEC FILE NAME: " + TestFileName);
        logger.info("SPEC NAME: " + executionContext.getCurrentSpecification().getName());
        logger.info("SPEC TAGS: " + executionContext.getCurrentSpecification().getTags());
    }

    @BeforeScenario
    public void beforeScenario(ExecutionContext executionContext) {


        logger.info("_________________________________________________________________________");
        logger.info("------------------------SCENARIO-------------------------");
        TestCaseName = executionContext.getCurrentScenario().getName();
        logger.info("SCENARIO NAME: " + TestCaseName);
        logger.info("SCENARIO TAG: " + executionContext.getCurrentScenario().getTags().toString());
        logger.info("---------------------------------------------------------");
        elementEnviroments.clear();
        String specPath = osName.equals("WINDOWS") ? TestFileName.replace("\\","/"): TestFileName;
        if (specPath.startsWith("/specs/ApiTest")){
            platformName = osName;
            logger.info("====================Api Test====================");
            return;
        }
        platformName = osName;
        webDriver = DriverFactory.getLocalDriver();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        webDriver.manage().window().maximize();
        //webDriver.manage().window().maximize();
        logger.info("url: " + url);
        webDriver.navigate().to(url);
        // webDriver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
        System.out.println(((RemoteWebDriver)webDriver).getCapabilities().toString());
        System.out.println(webDriver.manage().window().getSize().toString());
        lastElement = null;
    }

    @BeforeStep
    public void beforeStep(ExecutionContext executionContext) {

        logger.info("═════════  " + executionContext.getCurrentStep().getDynamicText() + "  ═════════");
    }

    @AfterStep
    public void afterStep(ExecutionContext executionContext) {

        if (executionContext.getCurrentStep().getIsFailing()) {
            if (lastElement != null) {
                ElementInfo elementInfo = lastElement;
                int i = elementInfo.getFileNameIndex();
                logger.warn("LastElement: " + elementInfo.getKey() + " elementi " + elementInfo.getType() + " " + elementInfo.getValue() + " Json dosya yolu: "
                        + (i == -1 ? "Element createElementInfo metoduyla oluşturulmuş" : StoreHelper.INSTANCE.getFileName(i)));
            }
           }
    }

    @AfterScenario
    public void afterScenario(ExecutionContext executionContext){

        ConnectDatabase.closeDatabaseConnections();
        if (webDriver != null) {
            webDriver.quit();
        }
        if (executionContext.getCurrentScenario().getIsFailing()) {
            logger.error("TEST BAŞARISIZ");
        } else {
            logger.info("TEST BAŞARILI");
        }
        logger.info("_________________________________________________________________________");
    }

    @AfterSpec
    public void afterSpec(ExecutionContext executionContext) {

        logger.info("=========================================================================");
    }

    @AfterSuite
    public void afterSuite(ExecutionContext executionContext) {

        logger.info("*************************************************************************");
    }

}