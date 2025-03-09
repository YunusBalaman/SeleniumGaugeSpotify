package com.spotify.web.methods.devtools.utils.networkRequestWillBeSent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter @Setter @AllArgsConstructor
public class RequestWillBeSentData {

    private String requestId;
    private String apiEndpoint;
    private String apiMethod;
    private String requestBody;
    private LinkedHashMap<String, String> requestBodyMap;
    private LinkedHashMap<String, String> urlEncodedMap;
    private Map<String, Object> header;
    private Long currentTimeMillis;
    private String documentUrl;
    private String url;
    private List<String> pathParams;
    private LinkedHashMap<String, String> queryParams;
    private LinkedHashMap<String, String> multipartFormDataMap;


    public RequestWillBeSentData(String requestId, String apiEndpoint, String apiMethod, String requestBody, Map<String, Object> header, Long currentTimeMillis, String documentUrl) {
        this.requestId = requestId;
        this.apiEndpoint = apiEndpoint;
        this.apiMethod = apiMethod;
        this.requestBody = requestBody;
        this.header = header;
        this.currentTimeMillis = currentTimeMillis;
        this.documentUrl = documentUrl;
    }

}
