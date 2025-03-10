package com.spotify.web.step;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.spotify.web.driver.Driver;
import com.spotify.web.methods.api.ApiMethods;
import com.spotify.web.methods.MethodsUtil;
import com.spotify.web.methods.api.ReadJsonMethods;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StepApiImplementation {

    private static final Logger logger = LogManager.getLogger(StepApiImplementation.class);
    MethodsUtil methodsUtil;
    ApiMethods apiMethods;
    ReadJsonMethods readJsonMethods;

    public StepApiImplementation(){

        methodsUtil = new MethodsUtil();
        apiMethods = new ApiMethods();
        readJsonMethods = new ReadJsonMethods();
    }

    @Step("<apiMapKey> api testi için map key olustur")
    public void setKey(String apiMapKey){

        Driver.apiMap.put(apiMapKey, new ConcurrentHashMap<String, Object>());
    }

    @Step("<baseUri> baseUri ı <apiMapKey> e ekle")
    public void setKeyBaseUri(String baseUri, String apiMapKey){

        baseUri = methodsUtil.getTextByMap(baseUri);
        baseUri = methodsUtil.setValueWithMapKey(baseUri);
        logger.info("BaseUri: " + baseUri);
        Driver.apiMap.get(apiMapKey).put("baseUri", baseUri);
    }

    @Step("<isRelaxedHTTPSValidation> relaxedHTTPSValidation ı <apiMapKey> e ekle")
    public void setRelaxedHTTPSValidation(Boolean isRelaxedHTTPSValidation, String apiMapKey){

        logger.info("isRelaxedHTTPSValidation: " + isRelaxedHTTPSValidation);
        Driver.apiMap.get(apiMapKey).put("isRelaxedHTTPSValidation", isRelaxedHTTPSValidation);
    }

    @Step("<acceptValue> accept degerini <apiMapKey> e ekle")
    public void setKeyAccept(String acceptValue, String apiMapKey){

        Driver.apiMap.get(apiMapKey).put("accept", acceptValue);
    }

    @Step("<contentType> contentType degerini <apiMapKey> e ekle")
    public void setKeyContentType(String contentType, String apiMapKey){

        contentType = methodsUtil.getTextByMap(contentType);
        logger.info("Content-Type: " + contentType);
        Driver.apiMap.get(apiMapKey).put("contentType", contentType);
    }

    //unchecked
    @Step("<headerKey> <headerValue> header degerini <apiMapKey> e ekle")
    public void setKeyHeaders(String headerKey, String headerValue, String apiMapKey){

        headerKey = methodsUtil.getTextByMap(headerKey);
        headerValue = methodsUtil.getTextByMap(headerValue);
        headerValue = methodsUtil.setValueWithMapKey(headerValue);
        logger.info("**Headers** " + headerKey + " : " + (!headerKey.equals("Authorization") ? headerValue : "Bearer ******* token gizlendi"));
        if (Driver.apiMap.get(apiMapKey).containsKey("headers")){

            ((ConcurrentHashMap<String, String>) Driver.apiMap.get(apiMapKey).get("headers")).put(headerKey, headerValue);
        }else {
            ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
            map.put(headerKey, headerValue);
            Driver.apiMap.get(apiMapKey).put("headers", map);
        }
    }

    @Step("<headerKey> <headerValue> header degerini <apiMapKey> e ekle <ifCondition>")
    public void setKeyHeaders(String headerKey, String headerValue, String apiMapKey, String ifCondition){

        if (Boolean.parseBoolean(methodsUtil.getTextByMap(ifCondition))){
            setKeyHeaders(headerKey, headerValue, apiMapKey);
        }
    }

    @Step("<username> <password> basic auth degerini <apiMapKey> e ekle preemptive <preemptive>")
    public void setBasicAuth(String username, String password, String apiMapKey, Boolean preemptive) {

        username = methodsUtil.getTextByMap(username);
        password = methodsUtil.getTextByMap(password);
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<String, Object>();
        map.put("username", username);
        map.put("password", password);
        map.put("preemptive", preemptive);
        Driver.apiMap.get(apiMapKey).put("basicAuth", map);
    }

    @Step("<token> değerini auth2 için <apiMapKey> e ekle")
    public void setAuth2(String token, String apiMapKey) {

        token = methodsUtil.getTextByMap(token);
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<String, Object>();
        map.put("token", token);
        Driver.apiMap.get(apiMapKey).put("auth2", map);
    }

    @Step("<host> <port> <username> <password> yada <uri> degerleriyle proxy i <mapKey> e ekle <onlyTestinium>")
    public void setProxy(String host, String port, String username, String password, String uri, String mapKey, Boolean onlyTestinium) {

        if (!onlyTestinium) {
            setProxy(host,port,username,password,uri,mapKey);
        }
    }

    @Step("<host> <port> <username> <password> yada <uri> degerleriyle proxy i <apiMapKey> e ekle")
    public void setProxy(String host, String port, String username, String password, String uri, String apiMapKey) {

        host = methodsUtil.getTextByMap(host);
        port = methodsUtil.getTextByMap(port);
        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<String, Object>();
        if (!host.equals("") && !port.equals("")) {
            map.put("host", host);
            map.put("port", port);
        }
        username = methodsUtil.getTextByMap(username);
        password = methodsUtil.getTextByMap(password);
        if (!username.equals("") && !password.equals("")) {
            map.put("username", username);
            map.put("password", password);
        }
        uri = methodsUtil.getTextByMap(uri);
        if (!uri.equals("")) {
            map.put("uri", uri);
        }
        Driver.apiMap.get(apiMapKey).put("proxy", map);
    }

    @SuppressWarnings("unchecked")
    @Step("<cookiesKey> <cookiesValue> cookies degerini <apiMapKey> e ekle")
    public void setCookies(String cookiesKey, String cookiesValue, String apiMapKey){

        cookiesKey = methodsUtil.getTextByMap(cookiesKey);
        cookiesValue = methodsUtil.getTextByMap(cookiesValue);
        logger.info("**Cookies** " + cookiesKey + " : " + cookiesValue);
        if (Driver.apiMap.get(apiMapKey).containsKey("cookies")){

            ((ConcurrentHashMap<String, String>) Driver.apiMap.get(apiMapKey).get("cookies"))
                    .put(cookiesKey, cookiesValue);
        }else {
            ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
            map.put(cookiesKey, cookiesValue);
            Driver.apiMap.get(apiMapKey).put("cookies", map);
        }
    }

    @Step("<cookiesKey> <cookiesValue> cookies degerini <apiMapKey> e ekle <ifCondition>")
    public void setCookies(String cookiesKey, String cookiesValue, String apiMapKey, String ifCondition) {

        if (Boolean.parseBoolean(methodsUtil.getTextByMap(ifCondition))) {
            setCookies(cookiesKey, cookiesValue, apiMapKey);
        }
    }

    @SuppressWarnings("unchecked")
    private void setParamsCommon(String paramType, String paramKey, String paramValue, String apiMapKey){

        paramKey = methodsUtil.getTextByMap(paramKey);
        paramValue = methodsUtil.getTextByMap(paramValue);
        logger.info(paramKey + " " + paramValue);
        if (Driver.apiMap.get(apiMapKey).containsKey(paramType)){

            ((ConcurrentHashMap<String, Object>) Driver.apiMap.get(apiMapKey).get(paramType))
                    .put(paramKey, paramValue);
        }else {
            ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<String, Object>();
            map.put(paramKey, paramValue);
            Driver.apiMap.get(apiMapKey).put(paramType, map);
        }
    }

    @Step("<paramKey> <paramValue> parametre degerini <apiMapKey> e ekle")
    public void setKeyParams(String paramKey, String paramValue, String apiMapKey){

        setParamsCommon("params", paramKey, paramValue, apiMapKey);
    }

    @Step("<paramKey> <paramValue> parametre degerini <apiMapKey> e ekle if <ifCondition>")
    public void setKeyParams(String paramKey, String paramValue, String apiMapKey, String ifCondition){

        if (Boolean.parseBoolean(methodsUtil.getTextByMap(ifCondition))){
            setKeyParams(paramKey, paramValue, apiMapKey);
        }
    }

    @Step("<paramKey> <paramValue> path parametre degerini <apiMapKey> e ekle")
    public void setKeyPathParams(String paramKey, String paramValue, String apiMapKey){

        setParamsCommon("pathParams", paramKey, paramValue, apiMapKey);
    }

    @Step("<paramKey> <paramValue> query parametre degerini <apiMapKey> e ekle")
    public void setKeyQueryParams(String paramKey, String paramValue, String apiMapKey){

        setParamsCommon("queryParams", paramKey, paramValue, apiMapKey);
    }

    @Step("<paramKey> <paramValue> form parametre degerini <apiMapKey> e ekle")
    public void setKeyFormParams(String paramKey, String paramValue, String apiMapKey){

        setParamsCommon("formParams", paramKey, paramValue, apiMapKey);
    }

    @Step("<apiMapKey> keyine parametre degerlerini table olarak ekle <paramTable>")
    public void setKeyParams(String apiMapKey, Table paramTable) {

        Driver.apiMap.get(apiMapKey).put("params", setKeyParams(paramTable));
    }

    @Step("<apiMapKey> keyine query parametre degerlerini table olarak ekle <paramTable>")
    public void setKeyQueryParams(String apiMapKey, Table paramTable) {

        Driver.apiMap.get(apiMapKey).put("queryParams", setKeyParams(paramTable));
    }

    @Step("<apiMapKey> keyine form parametre degerlerini table olarak ekle <paramTable>")
    public void setKeyFormParams(String apiMapKey, Table paramTable) {

        Driver.apiMap.get(apiMapKey).put("formParams", setKeyParams(paramTable));
    }

    public ConcurrentHashMap<String, Object> setKeyParams(Table paramTable) {

        ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<String, Object>();
        String columnName1 = paramTable.getColumnName(0);
        String columnName2 = paramTable.getColumnName(1);
        assertEquals("key", columnName1,"table first column name key olmalı: " + columnName1);
        assertEquals("value", columnName2,"table second column name value olmalı: " + columnName2);
        List<String> keyList = paramTable.getColumnValues("key");
        List<String> valueList = paramTable.getColumnValues("value");
        for (int i = 0; i < keyList.size(); i++) {
            map.put(keyList.get(i), valueList.get(i));
        }
       return map;
    }

    @Step("<body> body degerini <bodyType> tipiyle <apiMapKey> e ekle")
    public void setKeyBody(String body, String bodyType, String apiMapKey){

        if(bodyType.equals("String")){
            body = methodsUtil.getTextByMap(body);
            body = methodsUtil.setValueWithMapKey(body);
        }
        Driver.apiMap.get(apiMapKey).put("body", body);
        Driver.apiMap.get(apiMapKey).put("bodyType", "body" + bodyType);
        logger.info(body);
    }

    @Step("<multipartKey> <multipartValue> multipart degerini <type> type ile <apiMapKey> e ekle")
    public void setMultipart(String multipartKey, String multipartValue, String type, String apiMapKey){

        multipartKey = methodsUtil.getTextByMap(multipartKey);
        multipartValue = methodsUtil.getTextByMap(multipartValue);
        logger.info(multipartKey + " " + multipartValue);
        HashMap<String, Object> map;
        if (Driver.apiMap.get(apiMapKey).containsKey("multipart")){
            map = ((HashMap<String, Object>) Driver.apiMap.get(apiMapKey).get("multipart"));
        }else {
            map = new HashMap<String, Object>();
        }
        switch (type){
            case "File":
                map.put(multipartKey, new File(multipartValue));
                break;
            default:
                map.put(multipartKey, multipartValue);
        }
        Driver.apiMap.get(apiMapKey).put("multipart", map);
    }

    @Step("<requestType> requestType ve <requestPath> i <apiMapKey> e ekle <requestPathType>")
    public void setKeyRequestType(String requestType, String requestPath, String apiMapKey, String requestPathType){

        requestPath = methodsUtil.getTextByMap(requestPath);
        requestPath = methodsUtil.setValueWithMapKey(requestPath);
        logger.info(requestPath);
        Driver.apiMap.get(apiMapKey).put("requestType", requestType);
        Driver.apiMap.get(apiMapKey).put("requestPathType", requestPathType);
        Driver.apiMap.get(apiMapKey).put("requestPath", requestPath);
    }

    @Step("<apiMapKey> api testi için istek at, log=<logActive> if <ifCondition>")
    public void sendRequest(String apiMapKey, boolean logActive, String ifCondition){

        if(Boolean.parseBoolean(methodsUtil.getTextByMap(ifCondition))) {
            sendRequest(apiMapKey, logActive);
        }else {
            logger.info("false durumu sebebiyle api isteği atılmadı");
        }
    }

    @Step("<apiMapKey> api testi için istek at <loopCountForStatusCode500> status code 500, log=<logActive> if <ifCondition>")
    public void sendRequestWithLoop(String apiMapKey, int loopCountForStatusCode500, boolean logActive, String ifCondition){

        Response response;
        if(Boolean.parseBoolean(methodsUtil.getTextByMap(ifCondition))) {
            response = apiMethods.getResponse(Driver.apiMap.get(apiMapKey));
            for (int i = 0; i < loopCountForStatusCode500; i++) {
                if (response.statusCode() >= 500){
                    logger.warn("WARNING: statusCode " + response.statusCode());
                    methodsUtil.waitBySeconds(2);
                    response = apiMethods.getResponse(Driver.apiMap.get(apiMapKey));
                }else {
                    break;
                }
            }
            logger.info(response.statusLine());
            if (logActive) {
                logger.info(apiMapKey + "Response:\n" + "" + response.asPrettyString());
            }
            logger.info("responseTime: " + response.getTime());
            Driver.apiMap.get(apiMapKey).put("response", response);
        }else {
            logger.info("false durumu sebebiyle api isteği atılmadı");
        }
    }

    @Step("<apiMapKey> api testi statusCode değeri <statusCode> değerine eşit mi if <ifCondition>")
    public void statusCodeControl(String apiMapKey, String statusCode, String ifCondition){

        if(Boolean.parseBoolean(methodsUtil.getTextByMap(ifCondition))) {
            statusCodeControl(apiMapKey, statusCode);
        }
    }

    @Step("<apiMapKey> api testi için istek at")
    public void sendRequest(String apiMapKey){

        sendRequest(apiMapKey,false);
    }

    @Step("<apiMapKey> api testi için istek at, log=<logActive>")
    public void sendRequest(String apiMapKey, boolean logActive){

        Response response = apiMethods.getResponse(Driver.apiMap.get(apiMapKey));
        logger.info(response.statusLine());
        logger.info("responseTime: " + response.getTime());
        if (logActive) {
            //logger.info("" + response.statusCode());
            logger.info(apiMapKey + "Response:\n" + "" + response.asPrettyString());
            //logger.info("" + response.getCookies().toString());
            //logger.info(response.getHeaders().toString());
        }
        Driver.apiMap.get(apiMapKey).put("response", response);
    }

    @Step("<apiMapKey> api testi statusCode değeri <statusCode> değerine eşit mi")
    public void statusCodeControl(String apiMapKey, String statusCode){

        statusCode = statusCode.endsWith("KeyValue") ? Driver.TestMap.get(statusCode).toString() : statusCode;
        Response response = (Response) Driver.apiMap.get(apiMapKey).get("response");
        assertEquals(Integer.parseInt(statusCode), response.statusCode());
    }

    @Step("<property> <value> json objesi değeri olarak <mapJsonObjectKey> e ekle <valueType>")
    public void addJsonObject(String property, String value, String mapKey, String valueType){

        JsonObject jsonObject;
        if(Driver.TestMap.containsKey(mapKey)){
            Object o = Driver.TestMap.get(mapKey);
            if (!(o instanceof JsonObject)){
                Assertions.fail(mapKey + " keyi JsonObject değeri tutmuyor");
            }
            jsonObject = (JsonObject) o;
            methodsUtil.setStepJsonObjectValue(jsonObject, property, value, valueType);
        }else {
            jsonObject = new JsonObject();
            methodsUtil.setStepJsonObjectValue(jsonObject, property, value, valueType);
            Driver.TestMap.put(mapKey, jsonObject);
        }
        logger.info("" + Driver.TestMap.get(mapKey).toString());
    }

    @Step("<mapJsonObjectKey> json array olustur <isOverwrite>")
    public void createJsonArray(String mapKey, Boolean isOverwrite){

        if(isOverwrite || !Driver.TestMap.containsKey(mapKey)) {
            JsonArray jsonArray = new JsonArray();
            Driver.TestMap.put(mapKey, jsonArray);
        }
    }

    @Step("<mapJsonObjectKey> json object olustur <isOverwrite>")
    public void createJsonObject(String mapKey, Boolean isOverwrite){

        if(isOverwrite || !Driver.TestMap.containsKey(mapKey)) {
            JsonObject jsonObject = new JsonObject();
            Driver.TestMap.put(mapKey, jsonObject);
        }
    }

    @Step("<value> json dizisi değerini <mapJsonObjectKey> e ekle <valueType>")
    public void addJsonArray(String value, String mapKey, String valueType){

        if(Driver.TestMap.containsKey(mapKey)){
            JsonArray jsonArray = (JsonArray) Driver.TestMap.get(mapKey);
            methodsUtil.setStepJsonArrayValue(jsonArray, value, valueType);
        }else {
            JsonArray jsonArray = new JsonArray();
            methodsUtil.setStepJsonArrayValue(jsonArray, value, valueType);
            Driver.TestMap.put(mapKey, jsonArray);
        }
    }

    @Step("<mapJsonObjectKey> de tutulan json objesini text olarak tut")
    public void saveJsonObjectToString(String mapKey){

        JsonObject jsonObject = (JsonObject) Driver.TestMap.get(mapKey);
        String jsonObjectString = jsonObject.toString();
        logger.info(jsonObjectString);
        Driver.TestMap.put(mapKey, jsonObjectString);
    }

    @Step("<mapJsonObjectKey> de tutulan json dizisini text olarak tut")
    public void saveJsonArrayToString(String mapKey){

        JsonArray jsonArray = (JsonArray) Driver.TestMap.get(mapKey);
        String jsonArrayString = jsonArray.toString();
        logger.info(jsonArrayString);
        Driver.TestMap.put(mapKey, jsonArrayString);
    }

    @Step("<mapKey> için bearer token ekle")
    public void setBearerToken(String mapKey){

        setKeyHeaders("Authorization","Bearer " + Driver.TestMap.get("token").toString(), mapKey);
    }

    @Step("<apiMapKey> keyini api map ten sil")
    public void removeApiMapKey(String apiMapKey){

        logger.info("" + Driver.apiMap.containsKey(apiMapKey));
        Driver.apiMap.remove(apiMapKey);
        logger.info("" + Driver.apiMap.containsKey(apiMapKey));
    }

    @Step("Api map i temizle")
    public void clearApiMap(){

        Driver.apiMap.clear();
        logger.info("" + Driver.apiMap.size());
    }

    @Step("<number> sayısından rastgele <count> kadar sayıyı listele <mapKey> keyinde tut")
    public void getRamdomNumberList(String number, String count, String mapKey){

        number = methodsUtil.getTextByMap(number);
        count = methodsUtil.getTextByMap(count);
        List<Integer> integerList = methodsUtil.getRandomNumberList(Integer.parseInt(number), Integer.parseInt(count));
        Driver.TestMap.put(mapKey, integerList);
        Driver.TestMap.put("size_" + mapKey, integerList.size());
    }

    @Step("<property> <valueListMapKey> json objesi değeri olarak <mapJsonObjectKeySuffix> e ekle <valueType> loop")
    public void addJsonObjectLoopForArray(String property, String valueListMapKey, String mapJsonObjectKeySuffix, String valueType){

        boolean trimActive = Driver.TestMap.containsKey("trimOptionsKeyValue")
                && Driver.TestMap.get("trimOptionsKeyValue").toString().equals("true");
        List<String> list = (List<String>) Driver.TestMap.get(valueListMapKey);
        for(int i = 0; i < list.size(); i++) {
            String value = trimActive ? list.get(i).trim() : list.get(i);
            if (Driver.TestMap.containsKey(i + mapJsonObjectKeySuffix)) {
                JsonObject jsonObject = (JsonObject) Driver.TestMap.get(i + mapJsonObjectKeySuffix);
                methodsUtil.setStepJsonObjectValue(jsonObject, property, value, valueType);
            } else {
                JsonObject jsonObject = new JsonObject();
                methodsUtil.setStepJsonObjectValue(jsonObject, property, value, valueType);
                Driver.TestMap.put(i + mapJsonObjectKeySuffix, jsonObject);
            }
        }
    }

    @Step("<mapJsonObjectKeySuffix> json dizisi değerini <mapJsonArrayKey> e ekle <valueType> loop <number>")
    public void setJsonArrayWithJsonObjectLoop(String mapJsonObjectKeySuffix, String mapJsonArrayKey, String valueType, String number){

        number = methodsUtil.getTextByMap(number);
        JsonArray jsonArray = new JsonArray();
        if (valueType.equals("JsonObject")) {
            for (int i = 0; i < Integer.parseInt(number); i++) {
                methodsUtil.setStepJsonArrayValue(jsonArray,i + mapJsonObjectKeySuffix,"JsonObject");
                Driver.TestMap.put(mapJsonArrayKey, jsonArray);
                Driver.TestMap.remove(i + mapJsonObjectKeySuffix);
            }
        }else {
            List<String> list = (List<String>) Driver.TestMap.get(mapJsonObjectKeySuffix);
            for (int i = 0; i < Integer.parseInt(number); i++) {
                methodsUtil.setStepJsonArrayValue(jsonArray, list.get(i), valueType);
                Driver.TestMap.put(mapJsonArrayKey, jsonArray);
            }
        }
    }

    @Step("<jsonMapKey> json üzerindeki <jsonPath> json yolunu oku <type> tipindeki degeri <mapKey> keyinde tut <valueTrim>")
    public void readJsonPath(String jsonMapKey, String jsonPath, String type, String mapKey, boolean valueTrim){

        readJsonMethods.setValueTrim(valueTrim);
        jsonPath = methodsUtil.setValueWithMapKey(jsonPath);
        readJsonMethods.readJsonPath(readJsonMethods.getJsonElementByMap(jsonMapKey)
                , readJsonMethods.getJsonPathAsList(jsonPath,"|?"), type, mapKey);
    }

    @Step("<jsonMapKey> json üzerindeki <jsonPath> json yolunu oku <type> tipindeki degeri <mapKey> keyinde tut")
    public void readJsonPath(String jsonMapKey, String jsonPath, String type, String mapKey){

        readJsonPath(jsonMapKey, jsonPath, type, mapKey,false);
    }

    @Step("<jsonPath> json path <value> value <type> type ve <id> degerini <mapKey> keyinde tut")
    public void setJsonPathMultipleValue(String jsonPath, String value, String type, String id, String mapKey){

        setJsonPathMultipleValue(jsonPath, value, type, id, mapKey,"equal",false);
    }

    @Step("<jsonPath> json path <value> value <type> type ve <id> degerini <mapKey> keyinde tut <valueControlType>")
    public void setJsonPathMultipleValue(String jsonPath, String value, String type, String id, String mapKey, String valueControlType){

        setJsonPathMultipleValue(jsonPath, value, type, id, mapKey, valueControlType,false);
    }

    @Step("<jsonPath> json path <value> value <type> type ve <id> degerini <mapKey> keyinde tut <valueControlType> OrActive <OrActive>")
    public void setJsonPathMultipleValue(String jsonPath, String value, String type, String id, String mapKey, String valueControlType, boolean OrActive){

        readJsonMethods.setJsonPathMultipleValue(jsonPath, value, type, id, mapKey, valueControlType, OrActive);
    }

    @Step("<jsonPath> json path <value> value <type> type ve <id> degerini <mapKey> keyinde tut <valueControlType> OrActive <OrActive> if <ifCondition>")
    public void setJsonPathMultipleValue(String jsonPath, String value, String type, String id, String mapKey, String valueControlType, boolean OrActive, String ifCondition){

        if (Boolean.parseBoolean(methodsUtil.getTextByMap(ifCondition))) {
            readJsonMethods.setJsonPathMultipleValue(jsonPath, value, type, id, mapKey, valueControlType, OrActive);
        }
    }

    @Step("<jsonMapKey> json üzerinden <jsonPathKey> json degerlerini al ve <mapKeySuffix> keyinde tut <keyTrim> <valueTrim>")
    public void getMultipleValue(String jsonMapKey, String jsonPathKey, String mapKeySuffix, boolean keyTrim, boolean valueTrim){

        readJsonMethods.setKeyTrim(keyTrim);
        readJsonMethods.setValueTrim(valueTrim);
        readJsonMethods.getMultipleValue(jsonPathKey, readJsonMethods.getJsonElementByMap(jsonMapKey),"|?", mapKeySuffix);
    }

    @Step("<jsonMapKey> json listesi üzerinden <controlJsonPathKey> varsa degerleri kontrol et <jsonPathKey> json degerlerini listele ve <mapKeySuffix> keyinde tut <keyTrim> <valueTrim>")
    public void getMultipleListValue(String jsonMapKey, String controlJsonPathKey, String jsonPathKey, String mapKeySuffix, boolean keyTrim, boolean valueTrim){

        readJsonMethods.setKeyTrim(keyTrim);
        readJsonMethods.setValueTrim(valueTrim);
        readJsonMethods.getMultipleListValue(controlJsonPathKey, jsonPathKey
                , readJsonMethods.getJsonElementByMap(jsonMapKey),"|?", mapKeySuffix);
    }

    @Step("<jsonMapKey> json listesi üzerinden <controlJsonPathKey> varsa degerleri kontrol et <jsonPathKey> json degerlerini listele ve <mapKeySuffix> keyinde tut")
    public void getMultipleListValue(String jsonMapKey, String controlJsonPathKey, String jsonPathKey, String mapKeySuffix){

        getMultipleListValue(jsonMapKey, controlJsonPathKey, jsonPathKey, mapKeySuffix,false,false);
    }

    @Step("<jsonString> json textini <fileLocation> dosya yoluna yaz")
    public void writeJson(String jsonString, String fileLocation){

        String json = readJsonMethods.getJsonElementByMap(jsonString).toString();
        fileLocation = methodsUtil.setValueWithMap(methodsUtil.getTextByMap(fileLocation));
        methodsUtil.writeJson(json, fileLocation,true,true,false);
        System.out.println("File Location: " + fileLocation);
    }

    @Step("<mapJsonKey> json liste sayısını <mapKey> degerinde tut")
    public void getJsonListSize(String mapJsonKey, String mapKey){

        int i = readJsonMethods.getJsonListSize((JsonArray) Driver.TestMap.get(mapJsonKey));
        Driver.TestMap.put(mapKey,i);
        logger.info("JsonListSize: " + i);
    }
}
