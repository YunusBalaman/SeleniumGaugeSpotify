package com.spotify.web.methods;

import com.google.common.base.Splitter;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.spotify.web.driver.Driver;
import io.restassured.response.Response;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.csv.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.graalvm.polyglot.Context;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.*;
import java.io.Writer;
import java.io.*;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Collator;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.fail;

public class MethodsUtil {
    
    private static final Logger logger = LogManager.getLogger(MethodsUtil.class);
    
    public MethodsUtil(){

    }

    public String getSplitStringBuilder(String value, String splitValue, String mapKeySuffix){

        String[] values = Splitter.on(splitValue).splitToList(value).toArray(new String[0]);
        StringBuilder stringBuilder = new StringBuilder();
        for (String text: values){
            if(text.endsWith(mapKeySuffix)){
                text = Driver.TestMap.get(text).toString();
            }
            stringBuilder.append(text);
        }
        return stringBuilder.toString();
    }

    public List<String> getSplitStringReturnAsList(String value,String splitValue){
        splitValue.replace("\"","");
        List<String> list = new ArrayList<>();
        list = Splitter.on(splitValue).splitToList(value);
        return list;
    }

    public void setStepJsonObjectValue(JsonObject jsonObject, String property, String value, String valueType) {

        if(!(valueType.equals("JsonArray") || valueType.equals("JsonObject") || valueType.equals("JsonElement"))
                && getMapKeyConditionCurlyBrackets(value)){
            value = replaceCurlyBrackets(value);
            if (Driver.TestMap.containsKey(value)){
                value = Driver.TestMap.get(value).toString();
            } else {
                fail(value + " degeri TestMap te bulunamadı");
            }
        }
        String bigDecimal = value;
        switch (valueType) {
            case "":
                break;
            case "JsonArray":
                jsonObject.add(property, (JsonArray) Driver.TestMap.get(replaceCurlyBrackets(value)));
                break;
            case "JsonObject":
                jsonObject.add(property, (JsonObject) Driver.TestMap.get(replaceCurlyBrackets(value)));
                break;
            case "JsonElement":
                jsonObject.add(property, (JsonElement) Driver.TestMap.get(replaceCurlyBrackets(value)));
                break;
            case "String":
                jsonObject.addProperty(property, value);
                break;
            case "Boolean":
                jsonObject.addProperty(property, Boolean.parseBoolean(value));
                break;
            case "Integer":
                jsonObject.addProperty(property, Integer.parseInt(value));
                break;
            case "Long":
                jsonObject.addProperty(property, Long.parseLong(value));
                break;
            case "Double":
                Number number = new Number() {
                    @Override
                    public int intValue() {
                        return 0;
                    }

                    @Override
                    public long longValue() {
                        return 0;
                    }

                    @Override
                    public float floatValue() {
                        return 0;
                    }

                    @Override
                    public double doubleValue() {
                        return 0;
                    }

                    @Override
                    public String toString() {
                        return new BigDecimal(String.valueOf(bigDecimal)).toPlainString();
                    }
                };
                jsonObject.addProperty(property, number);
                break;
            default:
                fail("Data tipi bulunamadı: " + valueType);
        }
    }

    public void setStepJsonArrayValue(JsonArray jsonArray, String value, String valueType) {

        if(!(valueType.equals("JsonArray") || valueType.equals("JsonObject") || valueType.equals("JsonElement"))
                && getMapKeyConditionCurlyBrackets(value)){
            value = replaceCurlyBrackets(value);
            if (Driver.TestMap.containsKey(value)){
                value = Driver.TestMap.get(value).toString();
            } else {
                fail(value + " degeri TestMap te bulunamadı");
            }
        }
        String bigDecimal = setValueWithMapKey(value);;
        switch (valueType) {
            case "":
                break;
            case "JsonArray":
                jsonArray.add((JsonArray) Driver.TestMap.get(replaceCurlyBrackets(value)));
                break;
            case "JsonObject":
                jsonArray.add((JsonObject) Driver.TestMap.get(replaceCurlyBrackets(value)));
                break;
            case "JsonElement":
                jsonArray.add((JsonElement) Driver.TestMap.get(replaceCurlyBrackets(value)));
                break;
            case "String":
                jsonArray.add(value);
                break;
            case "Boolean":
                jsonArray.add(Boolean.parseBoolean(value));
                break;
            case "Integer":
                jsonArray.add(Integer.parseInt(value));
                break;
            case "Long":
                jsonArray.add(Long.parseLong(value));
                break;
            case "Double":
                Number number = new Number() {
                    @Override
                    public int intValue() {
                        return 0;
                    }

                    @Override
                    public long longValue() {
                        return 0;
                    }

                    @Override
                    public float floatValue() {
                        return 0;
                    }

                    @Override
                    public double doubleValue() {
                        return 0;
                    }

                    @Override
                    public String toString() {
                        return new BigDecimal(String.valueOf(bigDecimal)).toPlainString();
                    }
                };
                jsonArray.add(number);
                break;
            default:
                fail("Data tipi bulunamadı: " + valueType);
        }
    }

    public String setStringJson(String value, String splitValue, String newValues, String types){

        String[] arrayValue = Splitter.on(splitValue).splitToList(newValues).toArray(new String[0]);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < arrayValue.length; i++){
            list.add("data" + (i+1));
        }
        char[] arrayTypes = types.toCharArray();
        String newValue = String.format(value, list.toArray(new String[0]));
        String a = "";
        for (int i=0; i < arrayValue.length; i++){
            a = arrayValue[i];
            a = setJsonWithMapKey(a);
            switch (arrayTypes[i]){
                case 'S':
                    newValue = newValue.replace(list.get(i), a);
                    break;
                case 'B':
                case 'N':
                    newValue = newValue.replace("\"" + list.get(i) + "\"", a);
                    break;
                default:
                    fail("Hata");
            }
        }
        System.out.println(newValue);
        return newValue;
    }

    public String getRandomStringWithLength(Integer length, Boolean useLetters,Boolean useNumbers){
        return RandomStringUtils.random(length,useLetters,useNumbers);
    }

    public String getRandomStringWithLength(String length, String useLetters,String useNumbers){
        return getRandomStringWithLength(Integer.parseInt(length),Boolean.parseBoolean(useLetters),Boolean.parseBoolean(useNumbers));
    }


    public String getTime(String format){

        return DateTime.now()//.withZone(DateTimeZone.UTC)
                .toString(format);//"dd/MM/yyyy HH:mm:ss,SSS");
    }
    public String getDateTimeFormat(){

        return DateTime.now()//.withZone(DateTimeZone.UTC)
                .toString();//"dd/MM/yyyy HH:mm:ss,SSS");
    }

    public long getTimeMillis(){

        DateTime utc = new DateTime();
        return utc.getMillis();
    }

    public long currentTimeMillis(){

        return System.currentTimeMillis();
    }

    public Long getTimeMillisFromTime(String time, String format){

        return org.joda.time.LocalDateTime.parse(time, DateTimeFormat.forPattern(format))
                .toDateTime().getMillis();
    }

    public DateTime getDateTimeFromTime(String time, String format, String language, int forOffsetHours){

        return org.joda.time.LocalDateTime.parse(time, DateTimeFormat.forPattern(format).withLocale(Locale.forLanguageTag(language)))
                .toDateTime(DateTimeZone.forOffsetHours(forOffsetHours));
    }

    public Long getTimeMillisFromTime(String time, String format, int forOffsetHours){

        return org.joda.time.LocalDateTime.parse(time, DateTimeFormat.forPattern(format))
                .toDateTime(DateTimeZone.forOffsetHours(forOffsetHours)).getMillis();
    }

    public Long getTimeMillisFromTime(String time, String format, String language, int forOffsetHours){

        return org.joda.time.LocalDateTime.parse(time, DateTimeFormat.forPattern(format).withLocale(Locale.forLanguageTag(language)))
                .toDateTime(DateTimeZone.forOffsetHours(forOffsetHours)).getMillis();
    }

    public Long getTimeMillisFromTime(String time, String format, String zoneID){

        return org.joda.time.LocalDateTime.parse(time, DateTimeFormat.forPattern(format))
                .toDateTime(DateTimeZone.forID(zoneID)).getMillis();
    }

    public String getTimeFromMillis(String format, long millis){

        DateTime dateTime = new DateTime(millis);
        return dateTime.toString(format);
    }

    public String getTimeFromMillisPlus(String format, long millis, int year, int month
            , int day, int hour, int minute, int second, int millisecond){

        DateTime dateTime = new DateTime(millis);
        return dateTime.plusYears(year).plusMonths(month).plusDays(day).plusHours(hour)
                .plusMinutes(minute).plusSeconds(second).plusMillis(millisecond).toString(format);
    }

    public String getTimeFromMillisPlus(String format, long millis, int year, int month
            , int day, int hour, int minute, int second, int millisecond, String forOffsetHours, String language){

        DateTime dateTime = null;
        if (forOffsetHours.equals("null")){
            dateTime = new DateTime(millis);
        }else {
            dateTime = new DateTime(millis, DateTimeZone.forOffsetHours(Integer.parseInt(forOffsetHours)));
        }
        dateTime = dateTime.plusYears(year).plusMonths(month).plusDays(day).plusHours(hour)
                .plusMinutes(minute).plusSeconds(second).plusMillis(millisecond);
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(format);
        //"EEE, dd MMM yyyy HH:mm:ss"
        //"dd/MM/yyyy HH:mm:ss,SSS"
        String time = "";
        if (language.equals("null")){
            time = dateTimeFormatter.withLocale(Locale.getDefault()).print(dateTime);
        }else {
            time = dateTimeFormatter.withLocale(Locale.forLanguageTag(language)).print(dateTime);
        }
        return time;
    }

    public String getTimeFromMillis(String format, long millis, int forOffsetHours){

        DateTime dateTime = new DateTime(millis, DateTimeZone.forOffsetHours(forOffsetHours));
        return dateTime.toString(format);
    }

    public String getTimeFromMillisWithZoneId(String format, long millis, String zoneID){

        DateTime dateTime = new DateTime(millis, DateTimeZone.forID(zoneID));
        return dateTime.toString(format);
    }

    public String getTimeWithZoneId(String format, String zoneID){

        //"Europe/Istanbul"
        return DateTime.now(DateTimeZone.forID(zoneID))
                .toString(format);
    }

    public String getTimeWithForOffsetHours(String format, int forOffsetHours){

        return DateTime.now(DateTimeZone.forOffsetHours(forOffsetHours))
                .toString(format);
    }

    private void setDoubleGsonFormat(GsonBuilder gsonBuilder){

        gsonBuilder.registerTypeAdapter(BigDecimal.class, (JsonSerializer<BigDecimal>) (src, typeOfSrc, context) -> {

            Number n = //src.scale() >= 8 ? (
                    new Number() {

                        @Override
                        public long longValue() {
                            return 0;
                        }

                        @Override
                        public int intValue() {
                            return 0;
                        }

                        @Override
                        public float floatValue() {
                            return 0;
                        }

                        @Override
                        public double doubleValue() {
                            return 0;
                        }

                        @Override
                        public String toString() {
                            return new BigDecimal(String.valueOf(src)).toPlainString();
                        }
                    }; //) : src
            return new JsonPrimitive(n);
        });
    }

    public Boolean writeJson(String jsonString, String fileLocation, boolean prettyPrint, boolean serializeNulls, boolean isAppend){

        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileLocation, isAppend), StandardCharsets.UTF_8));
            JsonElement element = JsonParser.parseString(jsonString);
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.disableHtmlEscaping();
            setDoubleGsonFormat(gsonBuilder);
            if (prettyPrint) { gsonBuilder.setPrettyPrinting(); }
            if (serializeNulls) { gsonBuilder.serializeNulls(); }
            Gson gson = gsonBuilder.create();
            gson.toJson(element, writer);
            writer.close();
            return true;
        } catch (IOException e) {
            logger.error(getStackTraceLog(e));
        }
        return false;
    }

    public <PANDA> Boolean writeJson(PANDA panda, String fileLocation, boolean prettyPrint, boolean serializeNulls, boolean isAppend){

        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileLocation, isAppend), StandardCharsets.UTF_8));
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.disableHtmlEscaping();
            setDoubleGsonFormat(gsonBuilder);
            if (prettyPrint) { gsonBuilder.setPrettyPrinting(); }
            if (serializeNulls) { gsonBuilder.serializeNulls(); }
            Gson gson = gsonBuilder.create();
            gson.toJson(panda, writer);
            writer.close();
            return true;
        } catch (IOException e) {
            logger.error(getStackTraceLog(e));
        }
        return false;
    }

    public <PANDA> PANDA readJson(Type type, String fileLocationOrStringJson, boolean isFile){

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.disableHtmlEscaping();
        setDoubleGsonFormat(gsonBuilder);
        Gson gson = gsonBuilder.create();
        try {
            if(isFile){
                FileReader fileReader = new FileReader(fileLocationOrStringJson);
                return gson.fromJson(fileReader, type);
            }
            return gson.fromJson(fileLocationOrStringJson, type);
        } catch (FileNotFoundException e) {
            logger.error(getStackTraceLog(e));
        }
        return null;
    }

    public Type getClassTypeToken(Type panda, Type... pandaClasses){
        // Type elementType = new TypeToken<>(){}.getType();
        if (pandaClasses.length != 0)
            return TypeToken.getParameterized(panda, pandaClasses).getType();
        return TypeToken.getParameterized(panda).getType();
    }

    public String getJsonStringWithBufferedReader(String fileLocation){

        StringBuilder jsonStringBuilder = new StringBuilder();
        InputStream propertiesStream = null;
        try {
            propertiesStream = new FileInputStream(fileLocation);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(propertiesStream, StandardCharsets.UTF_8));
            String jsonString;
            while(true){
                if ((jsonString = bufferedReader.readLine()) == null) break;
                jsonStringBuilder.append(jsonString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonStringBuilder.toString();
    }

    public String getStringXmlFile(String fileLocation){

        String xmlString = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(fileLocation);
            xmlString = IOUtils.toString(fileInputStream, StandardCharsets.UTF_8);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return xmlString;
    }

    public int getRandomNumber(int length){

        Random random = new Random();
        return random.nextInt(length);
    }

    public Integer getRandomInt(int origin, int bound){

        return getRandomIteratorInt(origin, bound).nextInt();
    }

    public PrimitiveIterator.OfInt getRandomIteratorInt(int origin, int bound){

        Random random = new Random();
        return random.ints(origin, bound).iterator();
    }

    public Long getRandomLong(int origin, int bound){

        return getRandomIteratorLong(origin, bound).nextLong();
    }

    public PrimitiveIterator.OfLong getRandomIteratorLong(long origin, long bound){

        Random random = new Random();
        return random.longs(origin, bound).iterator();
    }

    public Integer randomNumber(int origin, int bound){

        return ThreadLocalRandom.current().nextInt(origin, bound);
    }

    public Double randomNumber(double origin, double bound){

        return ThreadLocalRandom.current().nextDouble(origin, bound);
    }

    public String calcHmacSha256(String secretKey, String value) {

        byte[] secretKeyDecodeByte = Base64.getDecoder().decode(secretKey.getBytes(StandardCharsets.UTF_8));
        byte[] valueByte = value.getBytes(StandardCharsets.UTF_8);
        byte[] hmacSha256 = null;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyDecodeByte, "HmacSHA256");
            mac.init(secretKeySpec);
            hmacSha256 = mac.doFinal(valueByte);
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate hmac-sha256", e);
        }
        return Base64.getEncoder().encodeToString(hmacSha256);
    }

    public String calcHmacSha1(String secretKey) {

        byte[] secretKeyDecodeByte = new Base32().decode(secretKey.getBytes(StandardCharsets.UTF_8));
        String googleAuthCode = "";
        try {
            Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
            long currentTimeSeconds = calendar.getTimeInMillis() / 1000L;
            long timeLong = currentTimeSeconds / (long)30;
            byte[] time = ByteBuffer.allocate(8).putLong(timeLong).array();
            Mac mac = Mac.getInstance("HMACSHA1");
            SecretKeySpec macKey = new SecretKeySpec(secretKeyDecodeByte,"RAW");
            mac.init(macKey);
            byte[] hash = mac.doFinal(time);
            int offset = hash[hash.length - 1] & 15;
            int binary = (hash[offset] & 127) << 24 | (hash[offset + 1] & 255) << 16 | (hash[offset + 2] & 255) << 8 | hash[offset + 3] & 255;
            int a = binary % 1000000;
            googleAuthCode = String.format("%06d", a);
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate hmac-sha1", e);
        }
        return googleAuthCode;
    }

    public String getStackTraceLog(Throwable e){

        StackTraceElement[] stackTraceElements = e.getStackTrace();
        String error = e.toString() + "\r\n";
        for (int i = 0; i < stackTraceElements.length; i++) {

            error = error + "\r\n" + stackTraceElements[i].toString();
        }
        return error;
    }

    public void waitByMilliSeconds(long milliSeconds, Boolean... condition){

        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (condition.length == 0) {
            logger.info(milliSeconds + " milisaniye beklendi");
        }
    }

    public void waitBySeconds(long seconds, Boolean... condition){

        waitByMilliSeconds(seconds*1000,true);
        if (condition.length == 0) {
            logger.info(seconds + " saniye beklendi");
        }
    }

    public String randomString(int stringLength){

        Random random = new Random();
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUWVXYZabcdefghijklmnopqrstuwvxyz0123456789".toCharArray();
        String stringRandom = "";
        for (int i = 0 ; i < stringLength ; i++){

            stringRandom = stringRandom + String.valueOf(chars[random.nextInt(chars.length)]);
        }

        return stringRandom;
    }

    public String randomStringExtended(int stringLength, String charType, String startCharType, int startCharLength){

        Random random = new Random();
        StringBuilder stringRandom = new StringBuilder();
        char[] chars = null;

        if(!startCharType.equals("")){

            chars = getChars(startCharType);
            for (int i = 0 ; i < startCharLength; i++) {
                stringRandom.append(String.valueOf(chars[random.nextInt(chars.length)]));
            }
            stringLength = stringLength - startCharLength;
        }

        chars = getChars(charType);

        for (int i = 0 ; i < stringLength; i++){

            stringRandom.append(String.valueOf(chars[random.nextInt(chars.length)]));
        }

        return stringRandom.toString();
    }

    public char[] getChars(String condition){

        String upperChars = "ABCDEFGHIJKLMNOPQRSTUWVXYZ";
        String lowerChars = "abcdefghijklmnopqrstuwvxyz";
        String numbers = "0123456789";
        String space = " ";
        String turkishCharUpper = "İÜÖŞÇĞ";
        String turkishCharLower = "ıüöşçğ";
        String specialChar = ".,:;%+#!'`=()[]&/\\?*-_<>|^≥≤µ~≈Ωæß∂@∑€®₺¥π¨~¬∆ƒ£";
        String specialChar2 = "☮✈♋웃유☠☯♥✌✖☢☣☤⚜❖Σ⊗♒♠Ω♤♣♧♡♦♢♔♕♚♛★☆✮✯☄☾☽☼۞۩✄✂✆✉✦✧∞♂♀☿❤❥❦❧™®©✗✘⊗♒▢◆◇○◎●◯Δ◕◔ʊϟღ回₪✓✔✕☥☦☧☨☩☪☫☬☭™©®¿¡½⅓⅔¼¾⅛⅜⅝⅞℅№⇨❝❞∃∧∠∨∩⊂⊃∪⊥∀ΞΓɐəɘεβɟɥλч∞ΣΠ⌥⌘文⑂ஜ๏";
        char[] chars = null;
        switch (condition){
            case "upper":
                chars = upperChars.toCharArray();
                break;
            case "lower":
                chars = lowerChars.toCharArray();
                break;
            case "char":
                chars = (upperChars + lowerChars).toCharArray();
                break;
            case "numeric":
                chars = numbers.toCharArray();
                break;
            case "upperCharNumber":
                chars = (upperChars + numbers).toCharArray();
                break;
            case "all":
                chars = (upperChars + lowerChars + numbers).toCharArray();
                break;
            case "allSpecialChar":
                chars = (upperChars + lowerChars + numbers + space + specialChar).toCharArray();
                break;
            default:
                fail("");
        }

        return chars;
    }

    public String getCopiedText() {

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents(null);
        String copiedText = null;
        try {
            copiedText = contents.getTransferData(DataFlavor.stringFlavor).toString();
        } catch (IOException | UnsupportedFlavorException e) {
            logger.error(getStackTraceLog(e));
        }
        return copiedText;
    }

    public void setCopiedText(String text){

        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection,null);
    }

    private Matcher getMatcherCurlyBrackets(String value){

        return Pattern.compile("\\{\\{[A-Za-z0-9_\\-?=:;,.%+*$&/()<>|ıİüÜöÖşŞçÇğĞ]+\\}\\}").matcher(value);
    }

    public boolean getMapKeyConditionCurlyBrackets(String value){

        return Pattern.matches("\\{\\{[A-Za-z0-9_\\-?=:;,.%+*$&/()<>|ıİüÜöÖşŞçÇğĞ]+\\}\\}", value);
    }

    public String replaceCurlyBrackets(String value){

        return value.replace("{{","").replace("}}","");
    }

    public String setValueWithMapKey(String value, List<String> list){

        Matcher matcher = getMatcherCurlyBrackets(value);
        while (matcher.find()){
            String t = matcher.group();
            value = value.replaceFirst(t.replace("{{","\\{\\{")
                    .replace("}}","\\}\\}"), setValueWithMapKey(list.get(0)));
            list.remove(0);
        }
        return value;
    }

    public String setValueWithMapKey(String value){

        Matcher matcher = getMatcherCurlyBrackets(value);
        while (matcher.find()){
            String t = matcher.group();
            value = value.replace(t, Driver.TestMap.get(t.replace("{{","")
                    .replace("}}","")).toString());
        }
        return value;
    }

    public String setJsonWithMapKey(String value){

        Matcher matcher = getMatcherCurlyBrackets(value);
        while (matcher.find()){
            String t = matcher.group();
            String mapValue = t.replace("{{","").replace("}}","");
            t = mapValue.endsWith("String") ? "\"" + t + "\"" : t;
            value = value.replace(t, Driver.TestMap.get(mapValue).toString());
        }
        return value;
    }

    public int randomNumber(int number){

        Random random = new Random();
        return random.nextInt(number);
    }

    public int integerProcess(String islemTipi, String value1, String value2){

        int total = 0;
        switch (islemTipi){

            case "topla":
                total = Integer.parseInt(value1) + Integer.parseInt(value2);
                break;
            case "cikar":
                total = Integer.parseInt(value1) - Integer.parseInt(value2);
                break;
            case "carp":
                total = Integer.parseInt(value1) * Integer.parseInt(value2);
                break;
            case "bol":
                total = Integer.parseInt(value1) / Integer.parseInt(value2);
                break;
            default:
                fail("İşlem Tipi Hatalı " + islemTipi);
        }

        return total;
    }

    public String getSelectorTypeName(String type){

        String selectorType = "";
        switch (type) {
            case "id":
                selectorType = "id";
                break;
            case "name":
                selectorType = "name";
                break;
            case "className":
                selectorType = "class";
                break;
            case "cssSelector":
                selectorType = "css";
                break;
            case "xpath":
                selectorType = "xpath";
                break;
            default:
                fail("HATA");
        }
        return selectorType;
    }

    public boolean conditionValueControl(String expectedValue, String actualValue, String condition){

        boolean result = false;
        switch (condition){
            case "equal":
                result = actualValue != null && actualValue.equals(expectedValue);
                break;
            case "equalsIgnoreCase":
                result = actualValue != null && actualValue.equalsIgnoreCase(expectedValue);
                break;
            case "contain":
                result = actualValue != null && actualValue.contains(expectedValue);
                break;
            case "startWith":
                result = actualValue != null && actualValue.startsWith(expectedValue);
                break;
            case "endWith":
                result = actualValue != null && actualValue.endsWith(expectedValue);
                break;
            case "notEqual":
                result = actualValue != null && !actualValue.equals(expectedValue);
                break;
            case "notContain":
                result = actualValue != null && !actualValue.contains(expectedValue);
                break;
            case "notStartWith":
                result = actualValue != null && !actualValue.startsWith(expectedValue);
                break;
            case "notEndWith":
                result = actualValue != null && !actualValue.endsWith(expectedValue);
                break;
            case "notNull":
                result = actualValue != null;
                break;
            case "null":
                result = actualValue == null;
                break;
            case "regex":
                result = actualValue != null && Pattern.matches(expectedValue, actualValue);
                break;
            case "doubleEqual":
                result = actualValue != null && Double.parseDouble(expectedValue) == Double.parseDouble(actualValue);
                break;
            case "notDoubleEqual":
                result = actualValue != null && Double.parseDouble(expectedValue) != Double.parseDouble(actualValue);
                break;
            default:
                fail("conditionValueControl geçersiz durum - " + condition);
        }
        return result;
    }

    public void getListSort(List<String> list, boolean asc, Locale locale){

        if (asc) {
            list.sort(new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    Collator collator = Collator.getInstance(locale);
                    return collator.compare(s1, s2);
                }
            });
        } else {
            list.sort(Collections.reverseOrder(new Comparator<String>() {
                @Override
                public int compare(String s1, String s2) {
                    Collator collator = Collator.getInstance(locale);
                    return collator.compare(s1, s2);
                }
            }));
        }
    }

    public <T extends Comparable<? super T>> void getListSort(List<T> list, boolean asc){

        if (asc) {
            Collections.sort(list);
        } else {
            list.sort(Collections.reverseOrder());
        }
    }


    public void updateCsvByRow(List<List<String>> csvDataListByRow, String fileLocation,boolean isAppend){
        try {
            BufferedWriter writer = createWriter(fileLocation, isAppend);
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.builder()
                    .setQuoteMode(QuoteMode.ALL)
                    .setQuote('"')
                    .setDelimiter(',')
                    .setTrim(true).build());

            for(List<String> dataRow: csvDataListByRow){
                csvPrinter.printRecord(dataRow);
            }
            csvPrinter.flush();
            csvPrinter.close();
            if(writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void updateCsvByColumn(List<List<String>> csvDataListByColumn, String fileLocation,boolean isAppend){
        List<List<String>> newList = new ArrayList<>();
        int headerSize = csvDataListByColumn.get(0).size();
        List<String> tempList = new ArrayList<>();
        for(int i=0;i<csvDataListByColumn.size();i++){
            for(int y =0;y<headerSize;y++){
                tempList.add(y,csvDataListByColumn.get(i).get(y));
            }
            newList.add(i,tempList);
        }
        for(List<String> list: newList){
            updateCsvByRow(newList,fileLocation,isAppend);
        }
    }


    public void createCsv(String[] header, List<List<String>> csvDataList, String fileLocation, boolean isAppend) {

        try {
            BufferedWriter writer = createWriter( fileLocation, isAppend);
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.builder()
                    .setHeader(header)
                    .setQuoteMode(QuoteMode.ALL)
                    .setQuote('"')
                    .setDelimiter(',')
                    .setTrim(true).build());

            for (List<String> list : csvDataList) {
                csvPrinter.printRecord(list);
            }
            csvPrinter.flush();
            csvPrinter.close();
            if(writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addCsv(List<String> csvDataList, String fileLocation, boolean isAppend) {

        try {
            BufferedWriter writer = createWriter(fileLocation, isAppend);
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.builder()
                    .setQuoteMode(QuoteMode.ALL)
                    .setQuote('"')
                    .setDelimiter(',')
                    .setTrim(true).build());

            csvPrinter.printRecord(csvDataList);
            csvPrinter.flush();
            csvPrinter.close();
            if(writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<List<String>> readCsv(String fileLocation){

        CSVParser csvParser = null;
        try {
            csvParser = CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .setQuoteMode(QuoteMode.ALL)
                    .setQuote('"')
                    .setDelimiter(',')
                    .setTrim(true).build()
                    .parse(Files.newBufferedReader(Paths.get(fileLocation), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<List<String>> csvList = new ArrayList<List<String>>();
       // System.out.println(csvParser.getHeaderNames());
        csvList.add(csvParser.getHeaderNames());
        for (CSVRecord record : csvParser) {
            List<String> list = new ArrayList<String>();
            for (int k=0; k < record.size(); k++) {
                list.add(record.get(k));
            }
            csvList.add(list);
        }
        try {
            if (csvParser != null)
                csvParser.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return csvList;
    }

    public static BufferedWriter createWriter(String dir, boolean appendCondition) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(dir, StandardCharsets.UTF_8, appendCondition));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer;
    }

    public static BufferedReader createReader(String dir) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(dir), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reader;
    }

    public Object getMathExpression(String value){

        try {
            Context context = Context.create();
            return context.eval("js", value).as(Object.class);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public String readQR(String path) {

        String charset = StandardCharsets.UTF_8.toString();
        BinaryBitmap binaryBitmap = null;
        Result result = null;
        try {
            binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                    new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(path)))));
            Map<DecodeHintType, String> map = new ConcurrentHashMap<>();
            map.put(DecodeHintType.CHARACTER_SET, charset);
            result = new MultiFormatReader().decode(binaryBitmap, map);
        } catch (IOException | NotFoundException e) {
            logger.error(getStackTraceLog(e));
        }
        return result != null ? result.getText() : null;
    }

    public void createQR(String data, String path, int width, int height) {

        String charset = StandardCharsets.UTF_8.toString();
        BitMatrix matrix = null;
        try {
            matrix = new MultiFormatWriter().encode(
                    new String(data.getBytes(charset), charset),
                    BarcodeFormat.QR_CODE, width, height);
            MatrixToImageWriter.writeToPath(matrix,
                    path.substring(path.lastIndexOf('.') + 1),
                    new File(path).toPath());
        } catch (IOException | WriterException e) {
            logger.error(getStackTraceLog(e));
        }
    }

    public String stringTrim(String value, String trimCondition){

        if (value == null){
            return null;
        }
        switch (trimCondition){
            case "true":
                value = value.trim();
                break;
            case "clearSpace":
                value = value.replace("\r","").replace("\n","").trim();
                break;
            case "false":
            case "":
                break;
            default:
                fail(trimCondition + " condition hatalı");
        }
        return value;
    }

    public void writeFile(String text, String fileName){

        try {
        BufferedWriter writer3 = createWriter(fileName,false);
        writer3.append(text);writer3.newLine();
        Thread.sleep(1000);
        writer3.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getStringListSameOrDifference(List<String> list1, List<String> list2, boolean condition){

        List<String> list = new ArrayList<>();
        for(String value: list1){
            boolean isValueExist = list2.contains(value);
            if (condition && isValueExist){
                list.add(value);
            }
            if (!condition && !isValueExist){
                list.add(value);
            }
        }
        return list;
    }

    public List<Integer> getListSameOrDifference(List<Integer> list1, List<Integer> list2, boolean condition){

        List<Integer> list = new ArrayList<>();
        for(Integer value: list1){
            boolean isValueExist = list2.contains(value);
            if (condition && isValueExist){
                list.add(value);
            }
            if (!condition && !isValueExist){
                list.add(value);
            }
        }
        return list;
    }

    public Instant getTimeToInstant(int yearAmount){

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, yearAmount);
        return calendar.toInstant();
    }

    public Date getTimeDate(int yearAmount){

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, yearAmount);
        return calendar.getTime();
    }

    public String convertXmlStringToJson(String xmlString){

        JSONObject jsonObj = null;
        try {
            jsonObj = XML.toJSONObject(xmlString);
        } catch (JSONException e) {
            fail("hata");
        }
        return jsonObj.toString();
    }

    public boolean urlFilter(String url){

        if (Driver.baseUriList.isEmpty())
            return true;
        return Driver.baseUriList.stream().anyMatch(baseUri -> {
            if (baseUri.startsWith("{regex}")) {
                return Pattern.matches(baseUri.replace("{regex}",""),url);
            }
            return url.startsWith(baseUri);
        });
    }

    public String getResourceTargetPath(String path){

        URI uri = null;
        String resourcePath = "";
        try {
            uri = new URI(Objects.requireNonNull(this.getClass().getClassLoader().getResource(path)).getFile());
            File file = new File(uri.getPath());
            resourcePath = file.getAbsolutePath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new NullPointerException("File Directory Is Not Found!");
        }
        return resourcePath;
    }

    public JsonElement getJsonElementByMap(String jsonMapKey){

        String jsonString;
        JsonElement element = null;
        if(Driver.apiMap.containsKey(jsonMapKey)){
            jsonString = ((Response)Driver.apiMap.get(jsonMapKey).get("response")).asString();
            element = JsonParser.parseString(jsonString);
        }else if(getMapKeyConditionCurlyBrackets(jsonMapKey)){
            Object value = Driver.TestMap.get(replaceCurlyBrackets(jsonMapKey));
            if(value instanceof JsonElement){
                element = (JsonElement) value;
            }else if(value instanceof String){
                element = JsonParser.parseString(value.toString());
            }else
                fail("Hatalı json data");
        }else {
            element = JsonParser.parseString(jsonMapKey);
        }
        return element;
    }

    public String getBase64AuthorizationKey(String secretId, String secretKey){

        String authorizationKey = Base64.getEncoder().encodeToString((secretId + ":" + secretKey).getBytes(StandardCharsets.UTF_8));
        System.out.println(authorizationKey);
        return authorizationKey;
    }

}
