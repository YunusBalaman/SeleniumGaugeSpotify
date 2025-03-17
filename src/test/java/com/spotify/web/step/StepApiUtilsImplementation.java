package com.spotify.web.step;

import com.spotify.web.methods.MethodsUtil;
import com.spotify.web.methods.api.ReadJsonMethods;
import com.thoughtworks.gauge.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StepApiUtilsImplementation {

    private static final Logger logger = LogManager.getLogger(StepApiUtilsImplementation.class);
    MethodsUtil methodsUtil;
    ReadJsonMethods readJsonMethods;

    public StepApiUtilsImplementation(){

        methodsUtil = new MethodsUtil();
        readJsonMethods = new ReadJsonMethods();
    }

    @Step("<jsonMapKey> json üzerindeki <jsonPath> json yolunu oku <type> tipindeki degeri <mapKey> keyinde tut <valueTrim>")
    public void readJsonPath(String jsonMapKey, String jsonPath, String type, String mapKey, boolean valueTrim){

        readJsonMethods.setValueTrim(valueTrim);
        jsonPath = methodsUtil.setValueWithMapKey(jsonPath);
        readJsonMethods.readJsonPath(methodsUtil.getJsonElementByMap(jsonMapKey)
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

        if (Boolean.parseBoolean(methodsUtil.setValueWithMapKey(ifCondition))) {
            readJsonMethods.setJsonPathMultipleValue(jsonPath, value, type, id, mapKey, valueControlType, OrActive);
        }
    }

    @Step("<jsonMapKey> json üzerinden <jsonPathKey> json degerlerini al ve <mapKeySuffix> keyinde tut <keyTrim> <valueTrim>")
    public void getMultipleValue(String jsonMapKey, String jsonPathKey, String mapKeySuffix, boolean keyTrim, boolean valueTrim){

        readJsonMethods.setKeyTrim(keyTrim);
        readJsonMethods.setValueTrim(valueTrim);
        readJsonMethods.getMultipleValue(jsonPathKey, methodsUtil.getJsonElementByMap(jsonMapKey),"|?", mapKeySuffix);
    }

    @Step("<jsonMapKey> json listesi üzerinden <controlJsonPathKey> varsa degerleri kontrol et <jsonPathKey> json degerlerini listele ve <mapKeySuffix> keyinde tut <keyTrim> <valueTrim>")
    public void getMultipleListValue(String jsonMapKey, String controlJsonPathKey, String jsonPathKey, String mapKeySuffix, boolean keyTrim, boolean valueTrim){

        readJsonMethods.setKeyTrim(keyTrim);
        readJsonMethods.setValueTrim(valueTrim);
        readJsonMethods.getMultipleListValue(controlJsonPathKey, jsonPathKey
                , methodsUtil.getJsonElementByMap(jsonMapKey),"|?", mapKeySuffix);
    }

    @Step("<jsonMapKey> json listesi üzerinden <controlJsonPathKey> varsa degerleri kontrol et <jsonPathKey> json degerlerini listele ve <mapKeySuffix> keyinde tut")
    public void getMultipleListValue(String jsonMapKey, String controlJsonPathKey, String jsonPathKey, String mapKeySuffix){

        getMultipleListValue(jsonMapKey, controlJsonPathKey, jsonPathKey, mapKeySuffix,false,false);
    }
}
