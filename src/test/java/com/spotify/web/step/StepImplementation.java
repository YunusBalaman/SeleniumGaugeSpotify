package com.spotify.web.step;

import com.spotify.web.driver.Driver;
import com.spotify.web.methods.selenium.Methods;
import com.spotify.web.methods.MethodsUtil;
import com.thoughtworks.gauge.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class StepImplementation {

    private final Logger logger = LogManager.getLogger(getClass());
    Methods methods;
    MethodsUtil methodsUtil;

    public StepImplementation() {
        if(Driver.webDriver != null  //!Driver.TestFileName.startsWith("/specs/ApiMigrosNext")
         ){
            methods = new Methods();
        }
        methodsUtil = new MethodsUtil();
    }

    @Step("<key> elementine tıkla")
    public void clickElement(String key) {

        if(Driver.browser.equalsIgnoreCase("safari")){
            methods.clickElementJs(methods.getBy(key));
        }else
            methods.click(methods.getBy(key));
       // methods.click(methods.getBy(key));
    }

    @Step("<key> elementi varsa tıkla <timeout>")
    public void clickElementIfExist(String key, long timeout) {

        By by = methods.getBy(key);
        if (methods.isElementVisible(by, timeout)){
            methods.click(by);
        }
    }

    @Step("<key> elementine tıkla if <ifCondition>")
    public void clickElementIf(String key, String ifCondition) {

        if (Boolean.parseBoolean(methodsUtil.getTextByMap(ifCondition))) {
            methods.click(methods.getBy(key));
        }
    }

    @Step("<key> elementinin var olup olmadığı durumunu <mapKey> keyinde tut <timeout>")
    public void saveConditionIfElementIsExist(String key, String mapKey, long timeout) {

        Driver.TestMap.put(mapKey, methods.isElementVisible(methods.getBy(key), timeout));
    }

    @Step("<key> elementine çift tıkla")
    public void doubleClickElementWithAction(String key) {

        methods.doubleClickElementWithAction(methods.getBy(key),true);
    }

    @Step("<key> elementine tıkla with staleElement")
    public void clickElementForStaleElement(String key) {

        methods.clickElementForStaleElement(methods.getBy(key));
    }

    @Step("<key> elementine js ile tıkla")
    public void clickElementJs(String key) {

        methods.clickElementJs(methods.getBy(key));
    }

    @Step("<key> elementine koordinat <x> x ve <y> y ile tıkla")
    public void clickByWebElementCoordinate(String key, int x, int y) {

        methods.clickByWebElementCoordinate(methods.getBy(key), x, y);
    }

    @Step("<key> elementine js ile tıkla <notClickByCoordinate>")
    public void clickElementJs(String key, boolean notClickByCoordinate) {

        methods.clickElementJs(methods.getBy(key), notClickByCoordinate);
    }

    @Step("<key> elementine <text> değerini yaz")
    public void sendKeysElement(String key, String text) {

        text = text.endsWith("KeyValue") ? Driver.TestMap.get(text).toString() : text;
        methods.sendKeys(methods.getBy(key), text);
    }

    @Step("<key> elementine <text> keyini yolla")
    public void sendKeysElementWithKeys(String key, String text) {

        text = text.endsWith("KeyValue") ? Driver.TestMap.get(text).toString() : text;
        methods.sendKeysWithKeys(methods.getBy(key), text);
    }

    @Step("<key> elementine <fileUpload> dosya yolunu yaz")
    public void sendKeysFileUpload(String key, String fileUpload) {

        methods.sendKeys(methods.getBy(key),Driver.userDir
                + Driver.slash + (Driver.slash.equals("/") ? fileUpload: fileUpload.replace("/","\\")));
    }

    @Step("<key> elementine <fileUpload> dosya yolunu yaz <customFileLocation>")
    public void sendKeysFileUpload(String key, String fileUpload, boolean customFileLocation) {

        methods.sendKeys(methods.getBy(key),customFileLocation ? fileUpload : Driver.userDir
                + Driver.slash + (Driver.slash.equals("/") ? fileUpload: fileUpload.replace("/","\\")));
    }

    @Step("<key> elementine <fileUpload> dosya yolunu yaz <multipleFileActive> çoklu dosya yolu <customFileLocation>")
    public void sendKeysFileUpload(String key, String fileUpload, boolean multipleFileActive, boolean customFileLocation) {

        StringBuilder multipleFile = new StringBuilder();
        if (multipleFileActive) {
            List<String> files = (List<String>) Driver.TestMap.get(fileUpload);
            multipleFile.append(customFileLocation ? files.get(0) : Driver.userDir + Driver.slash + files.get(0).replace("/","\\"));
            for (int i = 1; i < files.size(); i++) {
                multipleFile.append(" \n ").append(customFileLocation ? files.get(i) : Driver.userDir + Driver.slash + files.get(i).replace("/","\\"));
            }
        }else {
            multipleFile.append(customFileLocation ? fileUpload : Driver.userDir + Driver.slash + fileUpload.replace("/","\\"));
        }
        methods.sendKeys(methods.getBy(key), multipleFile.toString());
    }

    @Step("<key> elementine js ile <text> değerini yaz")
    public void sendKeysElementWithJsAndBackSpace(String key, String text) {

        methodsUtil.waitBySeconds(1);
        methods.jsExecuteScript("arguments[0].value = \"" + text + "\";"
                ,false, methods.findElement(methods.getBy(key)));
        methodsUtil.waitByMilliSeconds(500);
        methods.sendKeysWithKeys(methods.getBy(key),"BACK_SPACE");
        methodsUtil.waitByMilliSeconds(500);
        methods.sendKeys(methods.getBy(key),text.substring(text.length()-1));
    }

    @Step("<key> elementine <text> sayı değerini yaz")
    public void sendKeysElementWithNumpad(String key, String text) {

        methods.sendKeysWithNumpad(methods.getBy(key), text);
    }

    @Step("<url> navigateTo")
    public void navigateTo(String url) {

        url = methodsUtil.getTextByMap(url);
        url = methodsUtil.setValueWithMap(url);
        methods.navigateTo(url);
    }

    @Step("<url> adresine git")
    public void getUrl(String url) {

        url = methodsUtil.getTextByMap(url);
        url = methodsUtil.setValueWithMap(url);
        methods.get(url);
    }

    @Step("<url> adresine git <ifCondition>")
    public void getUrl(String url, String ifCondition) {

        if (Boolean.parseBoolean(methodsUtil.getTextByMap(ifCondition))) {
            getUrl(url);
        }
    }

    @Step("Şu anki url <url> ile <condition> durumunu sağlıyor mu <count>")
    public void doesUrl(String url, String condition, int count) {

        url = methodsUtil.getTextByMap(url);
        url = methodsUtil.setValueWithMap(url);
        assertTrue(methods.doesUrl(url, count, condition),"Beklenen url, sayfa url ine eşit değil");
    }

    @Step("<key> elementinin değerini temizle")
    public void clearElement(String key) {

        methods.clearElement(methods.getBy(key));
    }

    @Step("<key> elementinin değerini back space kullanarak temizle <value>")
    public void clearElementWithBackSpace(String key, String value) {

        methods.clearElementWithBackSpace(methods.getBy(key), value);
    }

    @Step("<key> elementinin görünür olması kontrol edilir")
    public void checkElementVisible(String key) {

        checkElementVisible(key,30);
    }

    @Step("<key> elementinin enable olduğu kontrol edilir")
    public void checkElementEnabled(String key) {

        assertTrue(methods.isElementEnabled(methods.getBy(key)),"");
    }

    @Step("<key> elementinin enable degeri kontrol edilir <isElementEnabled> loop count <count>")
    public void checkElementEnabledOrDisabled(String key, boolean isElementEnabled, int count) {

        assertTrue(methods.isElementEnabled(methods.getBy(key), count, isElementEnabled));
    }

    @Step("<key> elementinin disabled durumu js ile kontrol edilir <isElementDisabled> loop count <count>")
    public void checkElementDisabledOrEnabledWithJs(String key, boolean isElementDisabled, int count) {

        assertTrue(methods.isElementDisabledJs(methods.getBy(key), count, isElementDisabled));
    }

    @Step("<key> elementinin var olduğu kontrol edilir")
    public void checkElementExist(String key) {

        assertTrue(methods.doesElementExist(methods.getBy(key),80),"");
    }

    @Step("<key> elementinin var olduğu kontrol edilir <milliSeconds> count <count>")
    public void checkElementExist(String key, long milliSeconds, int count) {

        assertTrue(methods.doesElementExist(methods.getBy(key), count, milliSeconds),"");
    }

    @Step("<key> elementinin var olmadığı kontrol edilir")
    public void checkElementNotExist(String key) {

        assertTrue(methods.doesElementNotExist(methods.getBy(key),80),"");
    }

    @Step("<key> elementinin var olmadığı kontrol edilir <milliSeconds> count <count>")
    public void checkElementNotExist(String key, long milliSeconds, int count) {

        assertTrue(methods.doesElementNotExist(methods.getBy(key), count, milliSeconds),"");
    }

    @Step("<key> elementinin görünür olmadığı kontrol edilir")
    public void checkElementInVisible(String key) {

        checkElementInVisible(key,30);
    }

    @Step("<key> elementinin konumunu aldığı kontrol edilir")
    public void checkElementLocated(String key) {

        checkElementLocated(key,30);
    }

    @Step("<key> elementinin konumunu aldığı kontrol edilir <timeout>")
    public void checkElementLocated(String key, long timeout) {

        assertTrue(methods.isElementLocated(methods.getBy(key), timeout));
    }

    @Step("<key> elementinin görünür olması kontrol edilir <timeout>")
    public void checkElementVisible(String key, long timeout) {

        assertTrue(methods.isElementVisible(methods.getBy(key), timeout),"element görünür degil");
    }

    @Step("<key> elementinin görünür olmadığı kontrol edilir <timeout>")
    public void checkElementInVisible(String key, long timeout) {

        assertTrue(methods.isElementInVisible(methods.getBy(key), timeout),"elementi halen görünür");
    }

    @Step("<key> elementinin tıklanabilir olması kontrol edilir")
    public void checkElementClickable(String key) {

        checkElementClickable(key,30);
    }

    @Step("<key> elementinin tıklanabilir olması kontrol edilir <timeout>")
    public void checkElementClickable(String key, long timeout) {

        assertTrue(methods.isElementClickable(methods.getBy(key), timeout));
    }

    @Step("<key> elementinin text değerini <mapKey> keyinde tut <trim>")
    public void getElementTextAndSave(String key, String mapKey, String trim){

        String text = methods.getText(methods.getBy(key));
        text = methodsUtil.stringTrim(text, trim);
        logger.info(text);
        Driver.TestMap.put(mapKey, text);
    }

    @Step("<key> elementinin <attribute> attribute değerini <mapKey> keyinde tut <trim>")
    public void getElementAttributeAndSave(String key, String attribute, String mapKey, String trim){

        String value = methods.getDomAttribute(methods.getBy(key), attribute);
        value = methodsUtil.stringTrim(value, trim);
        logger.info(value);
        Driver.TestMap.put(mapKey, value);
    }

    @Step("<key> elementinin value değerini <mapKey> keyinde tut <trim>")
    public void getElementValueAndSave(String key, String mapKey, String trim){

        String value = methods.getValueJs(methods.getBy(key),"3").toString();
        value = methodsUtil.stringTrim(value, trim);
        logger.info(value);
        Driver.TestMap.put(mapKey, value);
    }

    @Step("<key> elementinin value değeriyle <expectedValue> değerinin <condition> durumu kontrol edilir <count> trim <trim>")
    public void checkElementValue(String key, String expectedValue, String condition, int count, String trim){

        expectedValue = methodsUtil.getTextByMap(expectedValue);
        methods.checkElementCondition(methods.getBy(key),"valueJs", expectedValue, condition, count, trim);
    }

    @Step("<key> elementinin validationMessage değeriyle <expectedValue> değerinin <condition> durumu kontrol edilir <count> trim <trim>")
    public void checkValidationMessage(String key, String expectedValue, String condition, int count, String trim){

        expectedValue = methodsUtil.getTextByMap(expectedValue);
        methods.checkElementCondition(methods.getBy(key),"validationMessage", expectedValue, condition, count, trim);
    }

    @Step("<key> elementinin checkValidity değeriyle <expectedValue> değerinin <condition> durumu kontrol edilir <count> trim <trim>")
    public void checkValidity(String key, String expectedValue, String condition, int count, String trim){

        expectedValue = methodsUtil.getTextByMap(expectedValue);
        methods.checkElementCondition(methods.getBy(key),"checkValidity", expectedValue, condition, count, trim);
    }

    @Step("<key> elementinin <attribute> niteliği <expectedValue> değerinin <condition> durumu kontrol edilir <count>")
    public void checkElementAttribute(String key, String attribute, String expectedValue, String condition, int count) {

        expectedValue = methodsUtil.getTextByMap(expectedValue);
        condition = methodsUtil.getTextByMap(condition);
        methods.checkElementCondition(methods.getBy(key),"attribute", expectedValue, condition, count,"false", attribute);
    }

    @Step("<key> elementinin text değeriyle <expectedValue> değerinin <condition> durumu kontrol edilir <count> trim <trim>")
    public void checkElementText(String key, String expectedValue, String condition, int count, String trim){

        expectedValue = methodsUtil.getTextByMap(expectedValue);
        methods.checkElementCondition(methods.getBy(key),"text", expectedValue, condition, count, trim);
    }

    @Step("<key> elementinin text content değeriyle <expectedValue> değerinin <condition> durumu kontrol edilir <count> trim <trim>")
    public void checkElementTextContentJs(String key, String expectedValue, String condition, int count, String trim){

        expectedValue = methodsUtil.getTextByMap(expectedValue);
        methods.checkElementCondition(methods.getBy(key),"textContentJs", expectedValue, condition, count, trim);
    }

    @Step("<key> mouseover element")
    public void mouseOver(String key) {

        methods.mouseOverJs(methods.getBy(key),"1");
    }

    @Step("<key> mouseout element")
    public void mouseOut(String key) {

        methods.mouseOutJs(methods.getBy(key),"1");
    }

    @Step("<key> hover element")
    public void hoverElementAction(String key) {

        methods.hoverElementAction(methods.getBy(key),true);
    }

    @Step("<key> hover element <isScrollElement>")
    public void hoverElementAction(String key, boolean isScrollElement) {

        methods.hoverElementAction(methods.getBy(key), isScrollElement);
    }

    @Step("<key> hover and click element")
    public void moveAndClickElement(String key) {

        methods.moveAndClickElement(methods.getBy(key),true);
    }
    @Step("<key> hover and double click element")
    public void moveAndDoubleClickElement(String key) {

        methods.moveAndDoubleClickElement(methods.getBy(key),true);
    }

    @Step("<key> scroll element")
    public void scrollElementJs(String key) {

        methods.scrollElementJs(methods.getBy(key),"3");
    }

    @Step("<key> scroll element If Needed")
    public void scrollIntoViewIfNeededJs(String key) {

        methods.scrollIntoViewIfNeededJs(methods.getBy(key),"3");
    }

    @Step("<key> scroll element center")
    public void scrollElementCenterJs(String key) {

        methods.scrollElementCenterJs(methods.getBy(key),"1");
    }

    @Step("<key> focus element")
    public void focusElement(String key) {

        methods.focusElementJs(methods.getBy(key));
    }

    @Step("<key> dropdown elementinin <index> indexine tıkla")
    public void selectItemByIndex(String key, int index){

        methods.selectItemByIndex(methods.getBy(key), index);
    }

    @Step("<key> select element by index <index>")
    public void selectElementByIndex(String key, int index) {

        methods.selectByIndex(methods.getBy(key), index);
    }

    @Step("<key> select element by value <value>")
    public void selectElementByValue(String key, String value) {

        methods.selectByValue(methods.getBy(key), value);
    }

    @Step("<key> select element by text <text>")
    public void selectElementByText(String key, String text) {

        methods.selectByVisibleText(methods.getBy(key), text);
    }

    @Step("<key> select element by index <index> js")
    public void selectElementByIndexJs(String key, int index) {

        methods.selectByIndexJs(methods.getBy(key), index,"3");
    }

    @Step("<key> select element by value <value> js")
    public void selectElementByValueJs(String key, String value) {

        methods.selectByValueJs(methods.getBy(key), value,"3");
    }

    @Step("<key> select element by text <text> js")
    public void selectElementByTextJs(String key, String text) {

        methods.selectByTextJs(methods.getBy(key), text,"3");
    }

    @Step("Open new tab <url>")
    public void openNewTabJs(String url){

        methods.openNewTabJs(url);
    }

    @Step("Open new window <url>")
    public void openNewWindowJs(String url){

        methods.openNewWindowJs(url);
    }

    @Step("Switch tab <switchTabNumber>")
    public void switchTab(int switchTabNumber){

        methods.switchTab(switchTabNumber);
    }

    @Step("<key> switch frame element")
    public void switchFrameWithKey(String key) {
        methods.switchFrameWithKey(methods.getBy(key));
    }

    @Step("Switch default content")
    public void switchDefaultContent() {
        methods.switchDefaultContent();
    }

    @Step("Switch parent frame")
    public void switchParentFrame() {
        methods.switchParentFrame();
    }

    @Step("Navigate to refresh")
    public void navigateToRefresh(){

        methods.navigateToRefresh();
    }

    @Step("Navigate to back")
    public void navigateToBack(){

        methods.navigateToBack();
    }

    @Step("Navigate to forward")
    public void navigateToForward(){

        methods.navigateToForward();
    }

    @Step("Get Cookies")
    public void getCookies(){

        logger.info("cookies: " + methods.getCookies().toString());
    }

    @Step("Delete All Cookies")
    public void deleteAllCookies(){

        methods.deleteAllCookies();
    }

    @Step("Get User Agent")
    public void getUserAgent(){

        logger.info("userAgent: " + methods.jsExecuteScript("return navigator.userAgent;", false).toString());
    }

    @Step("Get page source")
    public void getPageSource(){

        logger.info("pageSource: " + methods.getPageSource());
    }

    @Step("Get performance logs <logContainText>")
    public void getPerformanceLogs(String logContainText){

        LogEntries les = Driver.webDriver.manage().logs().get(LogType.PERFORMANCE);
        for (LogEntry le : les) {
            //if(le.getMessage().contains("\"method\":\"Network.responseReceived\"")) {
            if(le.getMessage().contains(logContainText)){
                System.out.println(le.getMessage());
            }
        }
    }

    @Step("Get Navigator Webdriver")
    public void getNavigatorWebdriver(){

        Object object = methods.jsExecuteScript("return navigator.webdriver;", false);
        logger.info("NavigatorWebdriver: " + (object == null ? "null" : object.toString()));
    }

    @Step("Get Navigator Webdriver Json")
    public void getNavigatorWebdriverJson(){

        Object object = methods.jsExecuteScript("return Object.defineProperty(navigator, 'webdriver', { get: () => undefined });", false);
        logger.info("NavigatorWebdriverJson: " + (object == null ? "null" : object.toString()));
    }

    @Step("Sekmeyi kapat")
    public void closeTab(){

        methods.close();
    }

    @Step("Tablar kontrol edilir")
    public void tabControl(){

        for (int i = 0; i < 20; i++){
            methodsUtil.waitByMilliSeconds(400);
            if (methods.listTabs().size() > 1){
                break;
            }
        }
    }

    @Step("<key> elementi için <deltaY> deltaY <offsetX> offsetX ve <offsetY> offsetY değerleriyle mouse tekerleğini kaydır")
    public void mouseWheel(String key, int deltaY, int offsetX, int offsetY){

        methods.getJsMethods().wheelElement(methods.findElement(methods.getBy(key)), deltaY, offsetX, offsetY);
    }

    @Step("<key> elementi için <deltaY> deltaY <offsetX> offsetX ve <offsetY> offsetY değerleriyle mouse tekerleğini kaydır 2")
    public void mouseWheelSimple(String key, int deltaY, int offsetX, int offsetY){

        methods.getJsMethods().wheelElementSimple(methods.findElement(methods.getBy(key)), deltaY, offsetX, offsetY);
    }

    @Step("waitPageLoadCompleteJs")
    public void waitPageLoadCompleteJs(){

        methods.waitPageLoadCompleteJs();
    }

    @Step("waitJQueryCompleteJs")
    public void waitJQueryCompleteJs(){

        methods.waitJQueryCompleteJs();
    }

    @Step("waitPageScrollingCompleteJs")
    public void waitPageScrollingCompleteJs(){

        methods.waitPageScrollingCompleteJs();
    }

    @Step("waitForAngularLoadJs")
    public void waitForAngularLoadJs(){

        methods.waitForAngularLoadJs();
    }

    @Step("<key> elementinin değerini <value> değeriyle değiştirerek <newKey> elementini oluştur")
    public void keyValueChangerMethod(String key, String value, String newKey){

        methods.keyValueChangerMethodWithNewElement(key,newKey, value,"|!");
    }

    @Step("<js> javascript komutunu çalıştır değeri <mapKey> keyinde tut")
    public void runConsoleJavaScript(String js, String mapKey){

        js = js.endsWith("KeyValue") ? Driver.TestMap.get(js).toString() : js;
        Object value = methods.jsExecuteScript(js,false);
        //logger.info(value.toString());
        Driver.TestMap.put(mapKey, value);
    }

    @Step("<js> javascript komutunu <arguments> ile çalıştır değeri <mapKey> keyinde tut")
    public void runConsoleJavaScript(String js, String arguments, String mapKey){

        js = js.endsWith("KeyValue") ? Driver.TestMap.get(js).toString() : js;
        Object value = methods.jsExecuteScript(js,false, arguments.split("\\|\\?"));
        //logger.info(value.toString());
        Driver.TestMap.put(mapKey, value);
    }

    @Step("<key> element location control with <key2> element <condition>")
    public void denemeLocation(String key, String key2, boolean condition){

        By by = methods.getBy(key);
        By by2 = methods.getBy(key2);
        WebElement element2 = methods.findElement(by2);
        Rectangle rect = element2.getRect();
        WebElement element = methods.findElement(by);
        Point point = element.getLocation();
        boolean xCondition = rect.x <= point.x && point.x <= rect.x + rect.getWidth();
        boolean yCondition = rect.y <= point.y && point.y < rect.y + rect.getHeight();
        logger.info("Elementlocation: " + point.toString() + " controlElementLocation " + rect.getPoint().toString()
                + " controlElementHeightAndWidth " + rect.getDimension().toString()
                + " " + xCondition + " " + yCondition);
        assertEquals(condition,xCondition && yCondition);
    }

    @Step("Take Screenshot")
    public void takeScreenshot() {

        methods.takeScreenshot();
    }

    @Step("Take Screenshot <key> element")
    public void takeScreenShotForElement(String key) {

        methods.takeScreenshotForElement(methods.getBy(key));
    }

    @Step("Read qr Code <key>")
    public void readQrCode(String key){

       logger.info(methodsUtil.readQR(methods.takeScreenshotForElement(methods.getBy(key))));
    }

    @Step("<keyName> selenium keys")
    public void keyDown(String keyName){

        methods.keyDownUp(keyName);
    }

    @Step("<text> Set Copied Text")
    public void setCopiedText(String text){

        methodsUtil.setCopiedText(text);
    }

    @Step("<expectedValue> control copied text <condition>")
    public void getCopiedText(String expectedValue, String condition){

        String actualValue = methodsUtil.getCopiedText();
        logger.info(actualValue);
        methodsUtil.conditionValueControl(expectedValue, actualValue, condition);
    }

    @Step("<key> highlight Element")
    public void highlightElement(String key) {

        methods.highlightElement(methods.getBy(key));
    }

    @Step("<key> shadowRoot elementini bul <jsActive> jsActive <mapKey> keyinde tut")
    public void getShadowRootElement(String key, boolean jsActive, String mapKey){

        WebElement element = methods.getShadowRootElement(key, jsActive);
        Driver.TestMap.put(mapKey, element);
    }

    private WebElement getWebElementInMap(String mapKey){

        Object o = Driver.TestMap.get(mapKey);
        if (!(o instanceof WebElement)){
            fail(mapKey + " keyindeki element hatalı");
        }
        return  (WebElement) o;
    }

    @Step("<mapKey> mapte tutulan elemente tıkla")
    public void clickElementInMap(String mapKey){

        getWebElementInMap(mapKey).click();
    }

    @Step("<mapKey> mapte tutulan elemente <text> değerini yaz")
    public void sendKeysElementInMap(String mapKey, String text){

        getWebElementInMap(mapKey).sendKeys(text);
    }

    @Step("<mapKey> mapte tutulan elementin textini <textMapKey> keyinde tut")
    public void getTextElementInMap(String mapKey, String textMapKey){

        String text = getWebElementInMap(mapKey).getText();
        Driver.TestMap.put(textMapKey, text);
    }

    @Step("<mapKey> mapte tutulan elementin <attribute> attribute değerini <attributeMapKey> keyinde tut")
    public void getAttributeElementInMap(String mapKey, String attribute, String attributeMapKey){

        String attributeValue = getWebElementInMap(mapKey).getDomAttribute(attribute);
        Driver.TestMap.put(attributeMapKey, attributeValue);
    }

    @Step("<mapKey> mapte tutulan elemente scroll edilir")
    public void scrollElementCenterInMap(String mapKey){

        methods.getJsMethods().scrollElementCenter(getWebElementInMap(mapKey));
    }

    @Step("Set Driver element enviroments <enviroment>")
    public void setDriverEnviroment(String enviroment){

        enviroment = methodsUtil.getTextByMap(enviroment);
        enviroment = methodsUtil.setValueWithMapKey(enviroment);
        Driver.elementEnviroments.add(enviroment);
    }

    @Step("Clear Local Storage <clearLocalStorage>")
    public void clearLocalStorage(String clearLocalStorage){

        clearLocalStorage = methodsUtil.getTextByMap(clearLocalStorage);
        if (Boolean.parseBoolean(clearLocalStorage)) {
            methods.clearLocalStorage();
        }
    }

}