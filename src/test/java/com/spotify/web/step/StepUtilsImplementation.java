package com.spotify.web.step;

import com.spotify.web.driver.Driver;
import com.spotify.web.methods.MethodsUtil;
import com.spotify.web.utils.ReadProperties;
import com.thoughtworks.gauge.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.platform.commons.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class StepUtilsImplementation {

    private static final Logger logger = LogManager.getLogger(StepImplementation.class);
    MethodsUtil methodsUtil;

    public StepUtilsImplementation() {

        methodsUtil = new MethodsUtil();
    }

    @Step("<mapKey> keyinde tutulan değeri log olarak ekle")
    public void logTextByMap(String log){

        log = methodsUtil.getTextMap(log);
        logger.info(log);
    }

    @Step("<log> log olarak ekle")
    public void logText(String log){

        log =  methodsUtil.getTextByMap(log);
        logger.info(log);
    }

    @Step("Get time millis and save <mapKey>")
    public void getMillisAndSave(String mapKey) {

        long currentTime = methodsUtil.currentTimeMillis();
        logger.info(String.valueOf(currentTime));
        Driver.TestMap.put(mapKey, currentTime);
    }

    @Step("Zamanı milisaniye olarak al ve <mapKey> de tut")
    public void saveCurrentTimeMillis(String mapKey){
        logger.info(String.valueOf(methodsUtil.currentTimeMillis()));
        Driver.TestMap.put(mapKey, methodsUtil.currentTimeMillis());
    }

    @Step("Zamanı DateTime olarak al ve <mapKey> de tut")
    public void saveCurrentDateTime(String mapKey){
        logger.info(methodsUtil.getDateTimeFormat());
        Driver.TestMap.put(mapKey, methodsUtil.getDateTimeFormat());
    }

    @Step("<timeMillis> milisaniye tipindeki zamanı <format> formatındaki zamana çevir ve <mapKey> de tut")
    public void saveTimeFromCurrentTimeMillis(String timeMillis, String format, String mapKey){

        timeMillis = methodsUtil.getTextByMap(timeMillis);
        //String time = methodsUtil.getTimeFromMillisWithZoneId(format, Long.parseLong(timeMillis),"Europe/Istanbul");
        String time = methodsUtil.getTimeFromMillis(format, Long.parseLong(timeMillis));
        logger.info(timeMillis + " " + time);
        Driver.TestMap.put(mapKey, time);
    }

    @Step("<timeMillis> milisaniye tipindeki zamanı <format> formatını <year> year, <month> month, <day> day <hour>:<minute>:<second> degerlerini kullanarak <forOffsetHours> zamanı donustur ve <mapKey> de tut")
    public void saveTimeFromCurrentTimeMillis(String timeMillis, String format, int year, int month
            , int day, int hour, int minute, int second, String forOffsetHours, String mapKey){

        timeMillis = methodsUtil.getTextByMap(timeMillis);
        String time = methodsUtil.getTimeFromMillisPlus(format, Long.parseLong(timeMillis)
                , year, month, day, hour, minute, second,0, forOffsetHours,"null");
        logger.info(timeMillis + " " + time);
        Driver.TestMap.put(mapKey, time);
    }

    @Step("<timeMillis> milisaniye tipindeki zamanı <language> language <format> formatını <year> year, <month> month, <day> day <hour>:<minute>:<second> degerlerini kullanarak <forOffsetHours> zamanı donustur ve <mapKey> de tut")
    public void saveTimeFromCurrentTimeMillis(String timeMillis, String language, String format, int year, int month
            , int day, int hour, int minute, int second, String forOffsetHours, String mapKey){

        timeMillis = methodsUtil.getTextByMap(timeMillis);
        String time = methodsUtil.getTimeFromMillisPlus(format, Long.parseLong(timeMillis)
                , year, month, day, hour, minute, second,0, forOffsetHours, language);
        logger.info(timeMillis + " " + time);
        Driver.TestMap.put(mapKey, time);
    }

    @Step("<timeMillis> milisaniye tipindeki zamanı <format> formatındaki zamana <forOffsetHour> offset le çevir ve <mapKey> de tut")
    public void saveTimeFromCurrentTimeMillis(String timeMillis, String format, int forOffsetHour, String mapKey){

        timeMillis = methodsUtil.getTextByMap(timeMillis);
        String time = methodsUtil.getTimeFromMillis(format, Long.parseLong(timeMillis), forOffsetHour);
        logger.info(timeMillis + " " + time);
        Driver.TestMap.put(mapKey, time);
    }

    @Step("<time> zamanı <format> formatından milisaniyeye çevir ve <mapKey> de tut")
    public void saveTimeMillisFromTime(String time, String format, String mapKey){

        time = methodsUtil.getTextByMap(time);
        Long timeMillis = methodsUtil.getTimeMillisFromTime(time.trim(), format);
        logger.info(time + " " + timeMillis);
        Driver.TestMap.put(mapKey, timeMillis);
    }

    @Step("<time> zamanı <format> formatından milisaniyeye <forOffsetHour> offset le çevir ve <mapKey> de tut")
    public void saveTimeMillisFromTime(String time, String format, int forOffsetHour, String mapKey){

        time = methodsUtil.getTextByMap(time);
        Long timeMillis = methodsUtil.getTimeMillisFromTime(time.trim(), format, forOffsetHour);
        logger.info(time + " " + timeMillis);
        Driver.TestMap.put(mapKey, timeMillis);
    }

    @Step("<value> textinin <oldValue> degeriyle <newValue> degerini degistir ve <mapKey> değerinde tut")
    public void replaceText(String value, String oldValue, String newValue, String mapKey){

        value = value.endsWith("KeyValue") ? Driver.TestMap.get(value).toString() : value;
        value = value.replace(oldValue,newValue);
        Driver.TestMap.put(mapKey, value);
    }

    @Step("<jsonFileLocation> json dosyasini string olarak <mapKey> keyinde tut")
    public void readJsonFile(String jsonFileLocation, String mapKey){

        Driver.TestMap.put(mapKey, methodsUtil.getJsonStringWithBufferedReader(jsonFileLocation));
    }

    @Step("<jsonFileLocation> json dosyasinin degerlerini doldur ve string olarak <mapKey> keyinde tut")
    public void readAndSetJson(String jsonFileLocation, String mapKey){

        String json = methodsUtil.getJsonStringWithBufferedReader(jsonFileLocation);
        json = methodsUtil.setJsonWithMapKey(json);
        logger.info(json);
        Driver.TestMap.put(mapKey, json);
    }

    @Step("<xmlFileLocation> xml dosyasini string olarak <mapKey> keyinde tut")
    public void readXmlFile(String xmlFileLocation, String mapKey){

        Driver.TestMap.put(mapKey, methodsUtil.getStringXmlFile(xmlFileLocation));
    }

    @Step("<jsonString> json textini <values> degerleriyle <types> degistir ve <mapKey> keyinde tut")
    public void setJsonString(String jsonString, String values, String types, String mapKey){

        jsonString = jsonString.endsWith("KeyValue") ? Driver.TestMap.get(jsonString).toString() : jsonString;
        Driver.TestMap.put(mapKey, methodsUtil.setStringJson(jsonString,"!!", values, types));
    }

    @Step("<propertiesFileDir> properties dosyasını oku ve <mapKey> keyinde tut")
    public void saveResourceBundlePropertiesFile(String propertiesFileDir, String mapKey){

        ResourceBundle resourceBundle = ReadProperties.readPropDir(propertiesFileDir);
        Driver.TestMap.put(mapKey, resourceBundle);
    }

    @Step("<value> değeriyle <propertiesMapKey> properties dosyasında karşılık gelen değeri <mapKey> keyinde tut")
    public void setValueWithPropertiesFileAndSave(String value, String propertiesMapKey, String mapKey){

        ResourceBundle resourceBundle = (ResourceBundle) Driver.TestMap.get(propertiesMapKey);
        if(value.endsWith("KeyValue")){
            value = Driver.TestMap.get(value).toString();
        }
        logger.info(resourceBundle.getString(value));
        Driver.TestMap.put(mapKey, resourceBundle.getString(value));
    }

    @Step("<envKey> keyiyle enviroment ta tutulan bir deger varsa <mapKey> keyinde tut")
    public void saveMapTestiniumGetEnv(String envKey, String mapKey){

        String env = System.getenv(envKey);
        if(StringUtils.isNotBlank(env)){
            Driver.TestMap.put(mapKey, env);
        }
    }

    @Step("<envKey> keyiyle system property ta tutulan bir deger varsa <mapKey> keyinde tut")
    public void saveMapTestiniumGetProperty(String envKey, String mapKey){

        String env = System.getProperty(envKey);
        if(StringUtils.isNotBlank(env)){
            Driver.TestMap.put(mapKey, env);
        }
    }

    @Step("<value> degerini bigdecimal olarak al <scale> scale ve <roundingMode> modunda yuvarla <mapKey> keyinde tut")
    public void bigDecimalRoundingMode(String value, String scale, String roundingMode, String mapKey){

        // FLOOR  HALF_UP   HALF_DOWN   HALF_EVEN   UP   DOWN
        value = methodsUtil.getTextByMap(value);
        String newValue = new BigDecimal(value).setScale(Integer.parseInt(scale), RoundingMode.valueOf(roundingMode)).toPlainString();
        Driver.TestMap.put(mapKey, newValue);
    }

    @Step("<islemTipi> islem tipiyle <value1> ve <value2> degerleriyle islemi gerçeklestir <scale> ve <roundingMode> modunda yuvarla <mapKey> keyinde tut")
    public void bigDecimalProcesses(String islemTipi, String value1, String value2, String scale, String roundingMode, String mapKey){

        // FLOOR  HALF_UP   HALF_DOWN   HALF_EVEN   UP   DOWN --> rounding mode yerine koyulacaklar (yukarı-aşağı yuvarla vs...)
        value1 = methodsUtil.getTextByMap(value1);
        value2 = methodsUtil.getTextByMap(value2);
        int newScale = Integer.parseInt(scale);
        String newValue = "";
        switch (islemTipi){

            case "topla":
                newValue = new BigDecimal(value1).add(new BigDecimal(value2)).setScale(newScale, RoundingMode.valueOf(roundingMode)).toPlainString();
                break;
            case "cikar":
                newValue = new BigDecimal(value1).add(new BigDecimal("-" + value2)).setScale(newScale, RoundingMode.valueOf(roundingMode)).toPlainString();
                break;
            case "carp":
                newValue = new BigDecimal(value1).multiply(new BigDecimal(value2)).setScale(newScale, RoundingMode.valueOf(roundingMode)).toPlainString();
                break;
            case "bol":
                newValue = new BigDecimal(value1).divide(new BigDecimal(value2), newScale, RoundingMode.valueOf(roundingMode)).toPlainString();
                break;
            default:
                fail("İşlem Tipi Hatalı " + islemTipi);
        }

        System.out.println(newValue);
        Driver.TestMap.put(mapKey, newValue);
    }

    @Step("<expectedValue> expectedValue ile <actualValue> actualValue eşit mi")
    public void controlValuesEqual(String expectedValue, String actualValue) {

       controlValuesEqual(expectedValue, actualValue,"false");
    }

    @Step("<expectedValue> expectedValue null mü")
    public void controlValueNull(String expectedValue) {
        assertNull(methodsUtil.getTextByMap(expectedValue));
    }

    @Step("<expectedValue> expectedValue ile <actualValue> actualValue eşit mi <trim>")
    public void controlValuesEqual(String expectedValue, String actualValue, String trim) {

        expectedValue = methodsUtil.getTextByMap(expectedValue);
        actualValue = methodsUtil.getTextByMap(actualValue);
        logger.info("Beklenen deger: " + expectedValue);
        logger.info("Alınan deger: " + actualValue);
        assertEquals(methodsUtil.stringTrim(expectedValue, trim), methodsUtil.stringTrim(actualValue, trim),"Değerler eşit değil");
        logger.info("Değerler eşit");
    }

    @Step("<expectedValue> expectedValue ile <actualValue> actualValue <condition> durumunu sağlıyor mu <trim> if <ifCondition>")
    public void controlValuesCondition(String expectedValue, String actualValue, String condition, String trim, String ifCondition) {

        if (Boolean.parseBoolean(methodsUtil.getTextByMap(ifCondition))) {
            controlValuesCondition(expectedValue, actualValue, condition, trim);
        }
    }

    @Step("<expectedValue> expectedValue ile <actualValue> actualValue <condition> durumunu sağlıyor mu <trim>")
    public void controlValuesCondition(String expectedValue, String actualValue, String condition, String trim) {

        expectedValue = methodsUtil.getTextByMap(expectedValue);
        actualValue = methodsUtil.getTextByMap(actualValue);
        logger.info("Beklenen deger: " + expectedValue);
        logger.info("Alınan deger: " + actualValue);
        assertTrue(methodsUtil.conditionValueControl(methodsUtil.stringTrim(expectedValue, trim)
                , methodsUtil.stringTrim(actualValue, trim), condition),"Değerler " + condition + " durumunu sağlamıyor");
        logger.info("Değerler " + condition + " durumunu sağlıyor");
    }

    @Step("<expectedValue> expectedValue ile <actualValue> actualValue <condition> durumunu sağlıyor mu")
    public void controlValuesCondition(String expectedValue, String actualValue, String condition) {

        controlValuesCondition(expectedValue, actualValue, condition,"false");
    }

    @Step("<expectedValue> expectedValue ile <actualValue> actualValue <condition> durumunu <mapKey> keyinde tut <trim>")
    public void saveControlResult(String expectedValue, String actualValue, String condition, String mapKey, String trim) {

        expectedValue = methodsUtil.getTextByMap(expectedValue);
        actualValue = methodsUtil.getTextByMap(actualValue);
        Driver.TestMap.put(mapKey, methodsUtil.conditionValueControl(methodsUtil.stringTrim(expectedValue, trim)
                , methodsUtil.stringTrim(actualValue, trim), condition));
    }

    @Step("<expectedValue> expectedValue ile <actualValue> actualValue <condition> durumunu <mapKey> keyinde tut")
    public void saveControlResult(String expectedValue, String actualValue, String condition, String mapKey) {

        saveControlResult(expectedValue, actualValue, condition, mapKey,"false");
    }

    @Step("<length> uzunluğunda <charType> tipinde karakteri random olarak oluştur <mapkey> inde tut")
    public void randomChar(int length, String charType, String mapKey) {

        String value = methodsUtil.randomStringExtended(length, charType,"",0);
        Driver.TestMap.put(mapKey, value);
    }

    @Step("<value> text değerlerini birleştir <mapKey> keyinde tut")
    public void stringBuilderAndSave(String value, String mapKey){

        Driver.TestMap.put(mapKey, methodsUtil
                .getSplitStringBuilder(value,"?!","KeyValue"));
        logger.info(Driver.TestMap.get(mapKey).toString());
    }

    @Step("<number> rastgele sayı üret <mapKey> keyinde tut")
    public void randomNumber(String number, String mapKey){

        number = number.endsWith("KeyValue") ? Driver.TestMap.get(number).toString() : number;
        int value = methodsUtil.randomNumber(Integer.parseInt(number));
        System.out.println(value);
        Driver.TestMap.put(mapKey, value);
    }

    @Step("<number> rastgele double sayı üret <mapKey> keyinde tut")
    public void randomDoubleNumber(String number, String mapKey){

        number = number.endsWith("KeyValue") ? Driver.TestMap.get(number).toString() : number;
        int value = methodsUtil.randomNumber(Integer.parseInt(number));
        System.out.println(value);
        Driver.TestMap.put(mapKey, value);
    }

    @Step("<value> değeri <replaceValues> degerlerini temizle ve <mapKey> değerinde tut <trim>")
    public void clearText(String value, String replaceValues, String mapKey, boolean trim){

        value = methodsUtil.getTextByMap(value);
        String[] splitValues = replaceValues.split("\\?!");
        for (String splitValue: splitValues){
            if (!splitValue.equals("")){
                splitValue = splitValue.endsWith("KeyValue") ? Driver.TestMap.get(splitValue).toString() : splitValue;
                value = value.replace(splitValue,"");
            }
        }
        value = trim ? value.trim() : value;
        Driver.TestMap.put(mapKey, value);
    }

    @Step("<value> değeri <oldChars> değerlerini <newChars> degerleriyle değiştir ve <mapKey> değerinde tut <trim>")
    public void replaceText(String value, String oldChars, String newChars, String mapKey, String trim){

        value = methodsUtil.getTextByMap(value);
        String[] oldCharArray = oldChars.split("\\?!");
        String[] newCharArray = newChars.split("\\?!");
        String oldChar = "";
        String newChar = "";
        for (int i = 0; i < oldCharArray.length; i++){
            oldChar = oldCharArray[i];
            newChar = newCharArray[i];
            if (!oldChar.equals("")){
                oldChar = methodsUtil.getTextByMap(oldChar);
                newChar = methodsUtil.getTextByMap(newChar);
                value = value.replace(oldChar, newChar);
            }
        }
        value = methodsUtil.stringTrim(value,trim);
        Driver.TestMap.put(mapKey, value);
    }

    @Step("<value> değeri <regex> regex değerini <newValue> degeriyle değiştir ve <mapKey> değerinde tut <trim>")
    public void replaceWithRegex(String value, String regex, String newValue, String mapKey, boolean trim){

        value = methodsUtil.getTextByMap(value);
        newValue = methodsUtil.getTextByMap(newValue);
        Matcher matcher = Pattern.compile(regex).matcher(value);
        while (matcher.find()){
            String t = matcher.group();
            value = value.replace(t, newValue);
           // System.out.println(t);
        }
        value = trim ? value.trim() : value;
        Driver.TestMap.put(mapKey, value);
    }

    @Step("<value> değerini <splitValue> degeriyle parçala ve <mapKey> değerinde tut <trim>")
    public void splitAndSave(String value, String splitValue, String mapKey, boolean trim){

        value = methodsUtil.getTextByMap(value);
        String[] values = value.split(splitValue);
        List<String> list = new ArrayList<>();
        for (String a : values){
            a = trim ? a.trim() : a;
            list.add(a);
        }
        Driver.TestMap.put(mapKey, list);
    }

    @Step("<islemTipi> işlem tipiyle <value1> ve <value2> integer değerleriyle işlemi gerçekleştir ve <mapKey> keyinde tut")
    public void integerProcess(String islemTipi, String value1, String value2, String mapKey){

        value1 = methodsUtil.getTextByMap(value1);
        value2 = methodsUtil.getTextByMap(value2);
        int total = methodsUtil.integerProcess(islemTipi, value1, value2);
        Driver.TestMap.put(mapKey, total);
    }

    @Step("<value> degerini <mapKey> keyinde tut")
    public void saveData(String value, String mapKey) {

        value = methodsUtil.getTextByMap(value);
        Driver.TestMap.put(mapKey, value);
    }

    @Step("<value> degerini <mapKey> keyinde tut if <condition>")
    public void saveData(String value, String mapKey, String condition) {

        boolean reverse = condition.startsWith("!");
        condition = reverse ? condition.substring(1) : condition;
        boolean isTrue = condition.endsWith("KeyValue") ? Boolean.parseBoolean(Driver.TestMap.get(condition).toString())
                : Boolean.parseBoolean(condition);
        isTrue = reverse != isTrue;
        if (isTrue) {
            value = methodsUtil.getTextByMap(value);
            Driver.TestMap.put(mapKey, value);
        }
    }

    @Step("<valueMapKey> mapte tutulan degeri <mapKey> keyinde tut")
    public void saveMapData(String valueMapKey, String mapKey) {

        valueMapKey = Driver.TestMap.get(valueMapKey).toString();
        Driver.TestMap.put(mapKey, valueMapKey);
    }

    @Step("<valueMapKey> mapte tutulan objeyi <mapKey> keyinde tut")
    public void saveMapDataObject(String valueMapKey, String mapKey) {

        Object object = Driver.TestMap.get(valueMapKey);
        Driver.TestMap.put(mapKey, object);
    }

    @Step("<valueMapKey> degeri mapte var mı <mapKey>")
    public void controlMapKey(String valueMapKey, String mapKey) {

        boolean condition = Driver.TestMap.containsKey(valueMapKey);
        System.out.println(condition);
        Driver.TestMap.put(mapKey, condition);
    }

    @Step("<stringLength> uzunluğunda <charType> tipinde rastgele string üret ve <mapKey> keyinde tut")
    public void randomStringExtended(int stringLength, String charType, String mapKey) {

        String randomString = methodsUtil.randomStringExtended(stringLength, charType,"",0);
        logger.info(randomString);
        Driver.TestMap.put(mapKey, randomString);
    }

    @Step("<seconds> saniye bekle")
    public void waitBySeconds(long seconds) {

        methodsUtil.waitBySeconds(seconds);
    }

    @Step("<milliseconds> milisaniye bekle")
    public void waitByMilliSeconds(long milliseconds) {

        methodsUtil.waitByMilliSeconds(milliseconds);
    }

    /**@Step("<minioPath> dosyasını minio dan <localPath> yoluna indir")
    public void downloadMinioFile(String minioPath, String localPath){

    methodsUtil.downloadFileMinio("https://minio.migrosnext.com/","minio","devminio123"
    , "testscenarios", minioPath,System.getProperty("user.dir") + localPath);
    } */

    @Step("<listMapKey> listesini <asc> artan yada azalan olarak listele ve <mapKey> keyinde tut")
    public void getListSort(String listMapKey, boolean asc, String mapKey){

        List<String> list = new ArrayList<>((List<String>) Driver.TestMap.get(listMapKey));
        methodsUtil.getListSort(list, asc, new Locale("tr","TR"));
        Driver.TestMap.put(mapKey, list);
    }

    @Step("<list1MapKey> listesi <list2MapKey> listesine eşit mi")
    public void getListSort(String list1MapKey, String list2MapKey){

        List<String> list1 = new ArrayList<>((List<String>) Driver.TestMap.get(list1MapKey));
        List<String> list2 = new ArrayList<>((List<String>) Driver.TestMap.get(list2MapKey));
        assertEquals(list1, list2,"listeler eşit değil");
    }

    @Step("<value> textinin karakter uzunluğunu <mapKey> keyinde tut")
    public void charLength(String value, String mapKey){

        value = methodsUtil.getTextByMap(value);
        int i = value.toCharArray().length;
        Driver.TestMap.put(mapKey,i);
    }

    @Step("<value> değerini listeye ekle <mapKey> keyinde tut")
    public void setList(String value, String mapKey){

        value = methodsUtil.getTextByMap(value);
        if(Driver.TestMap.containsKey(mapKey)){
            List<String> list = (List<String>) Driver.TestMap.get(mapKey);
            list.add(value);
        }else {
            List<String> list = new ArrayList<>();
            list.add(value);
            Driver.TestMap.put(mapKey,list);
        }
    }

    @Step("<mapKey> keyine boş bir liste oluştur if <condition>")
    public void createBlankList(String mapKey, String condition){

        if (Boolean.parseBoolean(methodsUtil.getTextByMap(condition))) {

            Driver.TestMap.put(mapKey, new ArrayList<>());
        }
    }


    @Step("<csvListKeyValue> csv dosyası için listeye ekle <mapKey>")
    public void addCsvList(String csvListKeyValue, String mapKey){

        List<String> csvList = (List<String>) Driver.TestMap.get(csvListKeyValue);
        if(Driver.TestMap.containsKey(mapKey)){
            List<List<String>> list = (List<List<String>>) Driver.TestMap.get(mapKey);
            list.add(csvList);
        }else {
            List<List<String>> list = new ArrayList<>();
            list.add(csvList);
            Driver.TestMap.put(mapKey,list);
        }
    }

    @Step("<csvListMapKey> listesindeki değerlerle <fileLocation> dosya yoluna csv dosyasını oluştur <isValueListCsvFormat>")
    public void createCsv(String csvListMapKey, String fileLocation, boolean isValueListCsvFormat){

        List<List<String>> list = (List<List<String>>) Driver.TestMap.get(csvListMapKey);
        String[] header = list.get(0).toArray(new String[0]);
        List<List<String>> csvList = new ArrayList<>();
        if (isValueListCsvFormat){
            for (int i = 1; i < list.size(); i++) {
                csvList.add(list.get(i));
            }
        }else {
            for (int i = 1; i < list.size(); i++) {
                List<String> valueList = list.get(i);
                for (int j = 0; j < valueList.size(); j++) {
                    if (csvList.size() != j+1){
                        csvList.add(new ArrayList<String>());
                    }
                    csvList.get(j).add(valueList.get(j));
                }
            }
        }
        methodsUtil.createCsv(header, csvList, fileLocation,false);
    }

    @Step("<fileLocation> csv dosyasını oku <mapKeySuffix> if <condition>")
    public void readCsvFile(String fileLocation, String mapKeySuffix, String condition) {

        if (Boolean.parseBoolean(methodsUtil.getTextByMap(condition).toString())){
            readCsvFile(fileLocation, mapKeySuffix);
        }
    }

    @Step("<fileLocation> csv dosyasını oku <mapKeySuffix>")
    public void readCsvFile(String fileLocation, String mapKeySuffix){

        List<List<String>> csvLists = methodsUtil.readCsv(fileLocation);
        List<String> headerList = csvLists.get(0);
        for (int i = 1; i < csvLists.size(); i++){
            List<String> csvList = csvLists.get(i);
            for (int j = 0; j < csvList.size(); j++){
                String mapKey = headerList.get(j) + "List" + mapKeySuffix;
                if(Driver.TestMap.containsKey(mapKey)){
                    List<String> list = (List<String>) Driver.TestMap.get(mapKey);
                    list.add(csvList.get(j));
                }else {
                    List<String> list = new ArrayList<>();
                    list.add(csvList.get(j));
                    Driver.TestMap.put(mapKey,list);
                }
            }
        }
    }

    @Step("<csvListKeyValue> degerleri <fileLocation> dosya yoluna data ekle <condition>")
    public void addToCsvFile(String csvListKeyValue, String fileLocation, String condition){

        boolean value = condition.startsWith("!");
        condition = value ? condition.substring(1) : condition;
        boolean isTrue = condition.endsWith("KeyValue") ? Boolean.parseBoolean(Driver.TestMap.get(condition).toString())
                : Boolean.parseBoolean(condition);
        isTrue = value != isTrue;
        if (isTrue) {
            List<String> csvList = (List<String>) Driver.TestMap.get(csvListKeyValue);
            methodsUtil.addCsv(csvList, fileLocation, true);
        }
    }

    @Step("<listMapKey> listesinin <value> value değerine eşit elemanının indexini <mapKey> keyinde tut <trim>")
    public void getListNumber(String listMapKey, String value, String mapKey, boolean trim){

        value = methodsUtil.getTextByMap(value);
        List<String> list = new ArrayList<>((List<String>) Driver.TestMap.get(listMapKey));
        String listValue = "";
        for (int i = 0; i < list.size(); i++){

            listValue = list.get(i);
            if (trim){
                listValue = listValue.trim();
                value = value.trim();
            }
           if (listValue.equals(value)){
               Driver.TestMap.put(mapKey, i);
               break;
           }
           if (i == list.size()-1)
               Assert.fail("Aranan değer listede bulunamadı " + value);
        }
    }

    @Step("<listMapKey> listesinin elemanlarını key olarak <listMapNames> listelerini value olarak <mapKeySuffix> keyinde tut <valueTrim>")
    public void setMapWithList(String listMapKey, String listMapNames, String mapKeySuffix, boolean valueTrim){

        List<String> keyList = new ArrayList<>((List<String>) Driver.TestMap.get(listMapKey.trim()));
        List<String> mapValueList = new ArrayList<>();
        List<List<String>> valuesList = new ArrayList<>();
        List<String> cacheKeyList = new ArrayList<>();
        String key = "";
        for (String listName: listMapNames.split(",")){
            valuesList.add((List<String>) Driver.TestMap.get(listName.trim()));
        }
        for (int i = 0; i < keyList.size(); i++){

            mapValueList = new ArrayList<>();
            for (List<String> list : valuesList){
                mapValueList.add(valueTrim ? list.get(i).trim(): list.get(i));
            }
            key = keyList.get(i).trim() + mapKeySuffix;
            cacheKeyList.add(key);
            Driver.TestMap.put(key, mapValueList);
        }
        Driver.TestMap.put("cacheKeyList" + mapKeySuffix, cacheKeyList);
    }

    @Step("<listMapKey> listesinin elemanlarını key olarak <listMapNames> listelerini value olarak <mapKeySuffix> keyinde tut")
    public void setMapWithList(String listMapKey, String listMapNames, String mapKeySuffix){

        List<Object> keyList = new ArrayList<>((List<Object>) Driver.TestMap.get(listMapKey.trim()));
        List<Object> mapValueList = new ArrayList<>();
        List<List<Object>> valuesList = new ArrayList<>();
        List<String> cacheKeyList = new ArrayList<>();
        String key = "";
        for (String listName: listMapNames.split(",")){
            valuesList.add((List<Object>) Driver.TestMap.get(listName.trim()));
        }
        for (int i = 0; i < keyList.size(); i++){

            mapValueList = new ArrayList<>();
            for (List<Object> list : valuesList){
                mapValueList.add(list.get(i));
            }
            key = keyList.get(i).toString().trim() + mapKeySuffix;//.trim() + mapKeySuffix;
            cacheKeyList.add(key);
            Driver.TestMap.put(key, mapValueList);
        }
        Driver.TestMap.put("cacheKeyList" + mapKeySuffix, cacheKeyList);
    }

    @Step("<listMapKey> text tipindeki listeyi sayısal bir listeye çevir <mapKey> keyinde tut <isDouble>")
    public void saveListStringToNumber(String listMapKey, String mapKey, boolean isDouble){

        List<String> list = new ArrayList<>((List<String>) Driver.TestMap.get(listMapKey));
        List<Double> doubleList = new ArrayList<>();
        List<Integer> integerList = new ArrayList<>();
        for (String value: list){
            if (isDouble) {
                doubleList.add(Double.parseDouble(value.trim()));
            } else
                integerList.add(Integer.parseInt(value.trim()));
        }
        if (isDouble)
            Driver.TestMap.put(mapKey, doubleList);
        else
            Driver.TestMap.put(mapKey, integerList);
    }

    @Step("<list1MapKey> listesinin <list2MapKey> listesinden ortak yada farklı kısmını <mapKey> keyinde tut <condition>")
    public void saveListContain(String list1MapKey, String list2MapKey, String mapKey, boolean condition){

        List<String> list = new ArrayList<>();
        List<String> list1 = new ArrayList<>((List<String>) Driver.TestMap.get(list1MapKey));
        List<String> list2 = new ArrayList<>((List<String>) Driver.TestMap.get(list2MapKey));
        for(String value: list1){
            boolean isValueExist = list2.contains(value);
            if (condition && isValueExist){
                    list.add(value);
            }
            if (!condition && !isValueExist){
                list.add(value);
            }
        }
        Driver.TestMap.put(mapKey, list);
    }

    @Step("<mathExpressionScript> scriptini kullanarak matematiksel işlemi gerçekleştir <mapKey> keyinde tut")
    public void mathExpression(String mathExpressionScript, String mapKey){

        mathExpressionScript = methodsUtil.getTextByMap(mathExpressionScript);
        mathExpressionScript = methodsUtil.setValueWithMapKey(mathExpressionScript);
        Object result = methodsUtil.getMathExpression(mathExpressionScript);
        logger.info(result.toString());
        Driver.TestMap.put(mapKey, result);
    }

    @Step("<value> değerini double olarak <mapKey> keyinde tut")
    public void saveValueAsDouble(String value, String mapKey){

        value = methodsUtil.getTextByMap(value);
        Driver.TestMap.put(mapKey, Double.parseDouble(value));
    }

    @Step("<value> değerini float olarak <mapKey> keyinde tut")
    public void saveValueAsFloat(String value, String mapKey){

        value = methodsUtil.getTextByMap(value);
        Driver.TestMap.put(mapKey, Float.parseFloat(value));
    }

    @Step("<listMapKey> listesinin <number> elemanını <mapKey> keyinde tut")
    public void getListValue(String listMapKey, String number, String mapKey){

        number = methodsUtil.getTextByMap(number);
        Driver.TestMap.put(mapKey, ((List) Driver.TestMap.get(listMapKey)).get(Integer.parseInt(number)));
    }

    @Step("<listMapKey> listesinin <number> elemanını <mapKey> keyinde tut <trim>")
    public void getListValue(String listMapKey, String number, String mapKey, boolean trim){

        number = methodsUtil.getTextByMap(number);
        String value = ((List) Driver.TestMap.get(listMapKey)).get(Integer.parseInt(number)).toString();
        Driver.TestMap.put(mapKey, trim ? value.trim(): value);
    }

    @Step("<listMapKey> listesinin eleman sayısını <mapKey> keyinde tut")
    public void getListSize(String listMapKey, String mapKey){

        int listSize = ((List)Driver.TestMap.get(listMapKey)).size();
        logger.info("size: " + listSize);
        Driver.TestMap.put(mapKey, listSize);
    }

    @Step("<mapKey> keyini map ten sil")
    public void removeMapKey(String mapKey){

        logger.info(mapKey + ": " + Driver.TestMap.containsKey(mapKey));
        Driver.TestMap.remove(mapKey);
        logger.info(mapKey + ": " + Driver.TestMap.containsKey(mapKey));
    }

    @Step("<listMapKey> keylerini map ten sil")
    public void removeListMapKey(String listMapKey){

        if(Driver.TestMap.containsKey(listMapKey)) {
            List<String> list = (List<String>) Driver.TestMap.get(listMapKey);
            logger.info(list.size() + " " + list.toString());
            for (String mapKey : list) {
                Driver.TestMap.remove(mapKey);
            }
        }
    }

    @Step("Map i temizle")
    public void clearMap(){

        Driver.TestMap.clear();
        logger.info("" + Driver.TestMap.size());
    }

    @Step("<mapKey> write file <fileLocation>")
    public void writeFile(String mapKey, String fileLocation){

        String json = methodsUtil.getJsonValueWithMap(mapKey);
        methodsUtil.writeFile(json, fileLocation);
    }

    @Step("Get time <yearAmount> instant degerini <mapKey> keyinde tut")
    public void getTimeToInstant(int yearAmount, String mapKey){

        Driver.TestMap.put(mapKey, methodsUtil.getTimeToInstant(yearAmount).toString());
    }

    @Step("Get time <yearAmount> date degerini <mapKey> keyinde tut")
    public void getTimeDate(int yearAmount, String mapKey){

        Driver.TestMap.put(mapKey,  methodsUtil.getTimeDate(yearAmount).toString());
    }

    @Step("Delete baseUriList")
    public void deleteBaseUriList(){

        Driver.baseUriList.clear();
    }

    @Step("<baseUri> Add to baseUrlList")
    public void addToBaseUriList(String baseUri){

        Driver.baseUriList.add(baseUri);
    }
}
