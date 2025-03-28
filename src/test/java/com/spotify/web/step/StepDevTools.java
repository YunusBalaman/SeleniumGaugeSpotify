package com.spotify.web.step;

import com.spotify.web.driver.Driver;
import com.spotify.web.driver.SeleniumDevtools;
import com.spotify.web.methods.selenium.CommonProcess;
import com.spotify.web.methods.selenium.Methods;
import com.spotify.web.methods.MethodsUtil;
import com.spotify.web.methods.devtools.MethodsDevTools;
import com.spotify.web.methods.devtools.RequestData;
import com.spotify.web.methods.devtools.SearchDevtoolsApi;
import com.spotify.web.methods.devtools.utils.ProfilerScriptCoverage;
import com.spotify.web.methods.devtools.utils.debuggerScriptParsed.DebuggerScriptParsed;
import com.spotify.web.methods.devtools.utils.networkRequestWillBeSent.RequestWillBeSentData;
import com.thoughtworks.gauge.Step;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class StepDevTools {

    MethodsUtil methodsUtil;
    MethodsDevTools methodsDevTools;
    Methods methods;
    CommonProcess commonProcess;
    SearchDevtoolsApi searchDevtoolsApi;

    public StepDevTools(){

        methodsUtil = new MethodsUtil();
        methodsDevTools = new MethodsDevTools();
        searchDevtoolsApi = new SearchDevtoolsApi();
        if(Driver.webDriver != null){
            methods = new Methods();
            commonProcess = new CommonProcess();
        }
    }

    @Step("Set Devtools")
    public void setDevtools(){

        SeleniumDevtools.setDevTools();
    }

    @Step("setGeolocationOverride")
    public void setGeolocationOverride(){

        // SeleniumDevtools.devTools.send(Emulation.setGeolocationOverride(Optional.of(28.613939),Optional.of(77.209023),Optional.of(100)));
        // SeleniumDevtools.devTools.send(Emulation.setGeolocationOverride(Optional.of(40.741895),Optional.of(-73.989308),Optional.of(100)));
        methodsDevTools.setGeolocationOverride(SeleniumDevtools.devtoolsVersion,28.613939,77.209023,100);
        /**HashMap<String,Object> coordinates = new HashMap()
        {{
            put("latitude", 50.2334);
            put("longitude", 0.2334);
            put("accuracy", 1);
        }};
        ((ChromeDriver)Driver.webDriver).executeCdpCommand("Emulation.setGeolocationOverride", coordinates);*/
    }

    @Step("Enable Network")
    public void enableNetwork(){

        methodsDevTools.enableNetwork(SeleniumDevtools.devtoolsVersion);
    }

    @Step("Listen network api requests")
    public void setDevtoolsAndListenNetworkApiRequest(){

       // methodsDevTools.listenRequestWillBeSent(SeleniumDevtools.devtoolsVersion);
       // methodsDevTools.listenResponseReceived(SeleniumDevtools.devtoolsVersion);
        methodsDevTools.listenNetworkApiRequest(SeleniumDevtools.devtoolsVersion);
    }

    @Step("Set devtools listen current request number <mapKey>")
    public void setDevtoolsCurrentRequestNumber(String mapKey){

        Driver.TestMap.put(mapKey, SeleniumDevtools.requestIdList.size());
    }

    @Step("Clear listeners and close devtools")
    public void clearListenerAndCloseDevtools(){

        methodsDevTools.clearListenerAndCloseDevtools();
        /**
        StringBuilder stringBuilder = new StringBuilder();
        SeleniumDevtools.requestIdList.forEach(a -> stringBuilder
                .append(SeleniumDevtools.requestWillBeSentMap.get(a).getApiEndpoint()).append(" ")
                .append(SeleniumDevtools.requestWillBeSentMap.get(a).getApiMethod()).append(" ")
                .append(SeleniumDevtools.requestWillBeSentMap.get(a).getDocumentUrl()).append("\n"));
        methodsUtil.writeFile(stringBuilder.toString(),Driver.userDir + Driver.slash + "denmedevtoolsProduct.txt");
         */
         SeleniumDevtools.requestIdList.forEach(a ->System.out.println(SeleniumDevtools.requestWillBeSentMap.get(a).getApiEndpoint()
             + " " + SeleniumDevtools.requestWillBeSentMap.get(a).getApiMethod() + " " + SeleniumDevtools.requestWillBeSentMap.get(a).getDocumentUrl()));
    }


    @Step("<apiEndpoint> apiEndpoint <apiMethod> Network <number> api response save map <apiResponseMapKey>")
    public void getApiResponse(String apiEndpoint, String apiMethod, String number, String apiResponseMapKey) {

        int numberDevtools = Integer.parseInt(methodsUtil.setValueWithMapKey(number));
        List<RequestData> requestDataList = searchDevtoolsApi.getRequestData(apiEndpoint, apiMethod.toUpperCase(), numberDevtools, 0, 10, 100, 0);
        if (!requestDataList.isEmpty()) {
            for (RequestData requestData :requestDataList) {
                RequestWillBeSentData requestWillBeSentData = SeleniumDevtools.requestWillBeSentMap.get(requestData.getRequestId());
                System.out.println("ApiMethod: " + requestWillBeSentData.getApiMethod());
                System.out.println("getHeader: " + requestWillBeSentData.getHeader());
                System.out.println("getPathParams: " + requestWillBeSentData.getPathParams());
                System.out.println("getMultipartFormDataMap: " + requestWillBeSentData.getMultipartFormDataMap());
                //  System.out.println("getRequestBody: " + requestWillBeSentData.getRequestBody());
                System.out.println("getRequestBodyMap: " + requestWillBeSentData.getRequestBodyMap());
                System.out.println("getQueryParams: " + requestWillBeSentData.getQueryParams());
                System.out.println(requestWillBeSentData.getUrl() + " " + requestData.getResponseBody());
                System.out.println(SeleniumDevtools.responseReceivedMap.get(requestData.getRequestId()).getStatus());
                //if (requestData.getResponseBody() != null)
                   // SeleniumDevtools.responseBodyMap.put(requestData.getRequestId(), requestData.getResponseBody());
                System.out.println("------------------------------------------------------------------------------------");
            }
            Driver.TestMap.put(apiResponseMapKey, requestDataList);
        }
    }

    @Step("<apiResponseMapKey> keyindeki api response body i <jsonLocation> json yoluna yaz")
    public void writeApiResponseJsonFile(String apiResponseMapKey, String jsonLocation){

        if(Driver.TestMap.containsKey(apiResponseMapKey)){
            Object o = Driver.TestMap.get(apiResponseMapKey);
            if (o instanceof List<?>){
                List<RequestData> requestDataList = (List<RequestData>) o;
                String responseBody = requestDataList.get(requestDataList.size()-1).getResponseBody();
                if (responseBody != null) {
                    methodsUtil.writeJson(responseBody, jsonLocation,true,true,false);
                    return;
                }
                Assertions.fail("responseBody null");
            }
            Assertions.fail("Data hatalı");
        }
        Assertions.fail("Data bulunamadı");
    }

    @Step("Get client coverage")
    public void getClientCoverage(){

        List<ProfilerScriptCoverage> profilerScriptCoverageList = methodsDevTools.takeClientCoverageDemo(SeleniumDevtools.devtoolsVersion);
        methodsUtil.writeJson(profilerScriptCoverageList,"./jsonFiles/clientCoverage" + methodsUtil.currentTimeMillis() + ".json",true,true,false);
    }

    @Step("listenPreciseCoverageDeltaUpdate")
    public void listenPreciseCoverageDeltaUpdate(){

        methodsDevTools.listenPreciseCoverageDeltaUpdate(SeleniumDevtools.devtoolsVersion);
    }

    @Step("listenDebuggerScriptParsed")
    public void listenDebuggerScriptParsed(){

        methodsDevTools.listenDebuggerScriptParsed(SeleniumDevtools.devtoolsVersion);
    }


    @Step("stopClientCoverage")
    public void stopClientCoverage(){

        methodsDevTools.stopClientCoverage(SeleniumDevtools.devtoolsVersion);
    }

    @Step("Clear Listeners")
    public void clearListeners(){

        SeleniumDevtools.clearListeners();
    }

    @Step("Close Devtools")
    public void closeDevtools(){

        SeleniumDevtools.closeDevtools();
    }

    @Step("PreciseCoverageDeltaUpdate")
    public void getPreciseCoverageDeltaUpdate(){

        SeleniumDevtools.clientCoverageList.forEach(preciseCoverageDeltaUpdate -> methodsUtil
                .writeJson(preciseCoverageDeltaUpdate,"./jsonFiles/clientCoverageListen" + methodsUtil.currentTimeMillis() + ".json"
                        ,true,true,false));
    }

    @Step("Demo script")
    public void demo(){

        String js = "";
        String scriptId = "";
        DebuggerScriptParsed debuggerScript = null;
        List<ProfilerScriptCoverage> list = SeleniumDevtools.clientCoverageList.get(SeleniumDevtools.clientCoverageList.size()-1).getResult();
        for (ProfilerScriptCoverage profilerScriptCoverage : list){
            if(profilerScriptCoverage.getUrl().equals(js)){
                scriptId = profilerScriptCoverage.getScriptId();
                break;
            }
        }
        if (!scriptId.isEmpty()) {
           List<DebuggerScriptParsed> debuggerScriptParsed = SeleniumDevtools.debuggerScriptParsedList;
           for (DebuggerScriptParsed debuggerScriptParsed1 : debuggerScriptParsed){
               if(debuggerScriptParsed1.getScriptId().equals(scriptId)){
                   debuggerScript = debuggerScriptParsed1;
               }
           }
           if (debuggerScript != null){

               System.out.println("getScriptLanguage: " + debuggerScript.getScriptLanguage());
               System.out.println("getLength: " + debuggerScript.getLength());
               System.out.println("getUrl: " + debuggerScript.getUrl());
               System.out.println("getStartLine: " + debuggerScript.getStartLine());
               System.out.println("getEndLine: " + debuggerScript.getEndLine());
               System.out.println("getStartColumn: " + debuggerScript.getStartColumn());
               System.out.println("getEndColumn: " + debuggerScript.getEndColumn());
               System.out.println("getScriptId: " + debuggerScript.getScriptId());
           }
        }

    }


}
