package com.spotify.web.methods.devtools;


import com.spotify.web.driver.SeleniumDevtools;
import com.spotify.web.methods.MethodsUtil;
import com.spotify.web.methods.devtools.utils.networkRequestWillBeSent.RequestWillBeSentData;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

public class SearchDevtoolsApi {

    MethodsUtil methodsUtil;
    MethodsDevTools methodsDevTools;

    public SearchDevtoolsApi(){

        methodsUtil = new MethodsUtil();
        methodsDevTools = new MethodsDevTools();
    }

    public List<RequestData> getRequestData(String targetUrl, String apiMethod, int startIndex, long timeMillis, int requestWillBeSentLoopCount, int loopCount, int dataSize){

        boolean success = false;
        int status = 0;
        List<RequestData> requestDataList = new ArrayList<>();
        RequestWillBeSentData requestWillBeSentData = null;
        // ResponseReceivedData responseReceivedData = null;
        ResponseBodyData responseBodyData = null;
        List<String> requestIdList = new ArrayList<>();
        for (int i = 0; i < requestWillBeSentLoopCount; i++) {
            List<String> list = new ArrayList<>(SeleniumDevtools.requestIdList);
            if(list.size() > startIndex){
                for (int j = startIndex; j < list.size(); j++) {
                    String requestId = list.get(j);
                    requestWillBeSentData = SeleniumDevtools.requestWillBeSentMap.get(requestId);
                    String apiEndpoint = requestWillBeSentData.getApiEndpoint();
                    if (requestWillBeSentData.getUrl() == null) {
                        LinkedHashMap<String, String> queryParamMap;
                        LinkedHashMap<String, String> multipartFormDataMap;
                        LinkedHashMap<String, String> requestPostDataMap;
                        if (apiEndpoint.contains("?")) {
                            apiEndpoint = URLDecoder.decode(apiEndpoint, StandardCharsets.UTF_8);
                            String[] urlAndParams = apiEndpoint.split("\\?");
                            apiEndpoint = urlAndParams[0];
                            queryParamMap = new LinkedHashMap<>();
                            String[] params = urlAndParams[1].split("&");
                            String finalApiEndpoint = apiEndpoint;
                            Arrays.asList(params).forEach(e -> {
                                String[] keyValue = e.split("=");
                                if (keyValue.length == 2) {
                                    queryParamMap.put(keyValue[0], keyValue[1]);
                                }else {
                                   // System.out.println(finalApiEndpoint);
                                  //  System.out.println(e);
                                    queryParamMap.put(keyValue[0],"");
                                }
                            });
                            requestWillBeSentData.setQueryParams(queryParamMap);
                        }
                        String contentType = requestWillBeSentData.getHeader().containsKey("Content-Type") ? requestWillBeSentData
                                .getHeader().get("Content-Type").toString().split(";")[0] : "";
                        String postData = methodsDevTools.getRequestPostData(SeleniumDevtools.devtoolsVersion, requestId);
                        if (postData != null) {
                            switch (contentType) {
                                case "multipart/form-data":
                                    multipartFormDataMap = new LinkedHashMap<>();
                                    //String boundary = contentType.split("boundary=")[1];
                                    // .replace("--" + boundary + "--","").replace("--" + boundary,"")
                                    postData =  postData.split("Content-Disposition: form-data;")[1]
                                        .split("Content-Type:")[0];
                                    for (String a : postData.split(";")) {
                                        String[] array = a.split("=");
                                        multipartFormDataMap.put(array[0].replace("\"", ""), array[1].replace("\"", ""));
                                    }
                                    requestWillBeSentData.setMultipartFormDataMap(multipartFormDataMap);
                                    break;
                                case "application/x-www-form-urlencoded":
                                    requestPostDataMap = new LinkedHashMap<>();
                                    //requestWillBeSentData.setRequestBody(formData);
                                    for (String a : postData.split("&")) {
                                        String[] array = a.split("=");
                                        requestPostDataMap.put(array[0], array[1]);
                                    }
                                    requestWillBeSentData.setRequestBodyMap(requestPostDataMap);
                                    requestWillBeSentData.setUrlEncodedMap(requestPostDataMap);
                                    break;
                                default:
                                    requestPostDataMap = new LinkedHashMap<>();
                                    requestPostDataMap.put("data", postData);
                                    requestWillBeSentData.setRequestBodyMap(requestPostDataMap);
                            }
                        }

                        requestWillBeSentData.setUrl(apiEndpoint);
                    }
                    String url = requestWillBeSentData.getUrl();
                    if (targetUrl.contains("{pathParams}")) {
                        String[] targetUrlSplit = targetUrl.split("\\{pathParams\\}");
                        success = Pattern.matches(targetUrl.replace("{pathParams}","[A-Za-z0-9]+"), url);
                        AtomicReference<String> data = new AtomicReference<>("");
                        data.set(url);
                        Arrays.stream(targetUrlSplit).forEach(e -> data.set(data.toString().replace(e, "|?")));
                        requestWillBeSentData.setPathParams(Arrays.asList((data.toString().replaceFirst("\\|\\?","").split("\\|\\?"))));
                        if (!success && url.startsWith(targetUrlSplit[0]) && url.contains(targetUrlSplit[1])){
                            System.out.println("url: " + url);
                        }
                    }else {
                        success = url.equals(targetUrl);
                    }
                    success = success && (apiMethod.isEmpty() || requestWillBeSentData.getApiMethod().equals(apiMethod))
                            && (timeMillis == 0 || timeMillis < requestWillBeSentData.getCurrentTimeMillis());
                    if (success){
                        requestIdList.add(requestId);
                        //System.out.println(url);
                        // break;
                    }
                    startIndex = j + 1;
                }
            }
            if (!requestIdList.isEmpty() && (dataSize <= 0 || dataSize == requestIdList.size())){
                break;
            }
            else
                methodsUtil.waitByMilliSeconds(250,false);
        }
        for (String requestId : requestIdList) {
            for (int i = 0; i < loopCount; i++) {
                success = SeleniumDevtools.responseReceivedMap.containsKey(requestId);
                if (success) {
                    status = SeleniumDevtools.responseReceivedMap.get(requestId).getStatus();
                    break;
                }
                methodsUtil.waitByMilliSeconds(250,false);
            }
            if (success && status != 204) {
                for (int i = 0; i < 4; i++) {
                    responseBodyData = methodsDevTools.getResponseBody(SeleniumDevtools.devtoolsVersion, requestId);
                    success = responseBodyData != null;
                    if (success) {
                        break;
                    }
                    methodsUtil.waitByMilliSeconds(250,false);
                }
            }
            requestDataList.add(new RequestData(requestId, success ? responseBodyData.getRequestBody() : null));
        }
        return requestDataList;
    }
}
