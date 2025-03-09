package com.spotify.web.methods.devtools;

import java.util.Map;

public class ResponseReceivedData {

    private String requestId;
    private String apiEndpoint;
    private Integer status;
    private String contentType;
    private Map<String, Object> header;
    private Long currentTimeMillis;
    private String responseTime;

    public ResponseReceivedData(String requestId, String apiEndpoint, Integer status, String contentType, Map<String, Object> header, Long currentTimeMillis, String responseTime) {

        this.requestId = requestId;
        this.apiEndpoint = apiEndpoint;
        this.status = status;
        this.contentType = contentType;
        this.header = header;
        this.currentTimeMillis = currentTimeMillis;
        this.responseTime = responseTime;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getApiEndpoint() {
        return apiEndpoint;
    }

    public void setApiEndpoint(String apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Map<String, Object> getHeader() {
        return header;
    }

    public void setHeader(Map<String, Object> header) {
        this.header = header;
    }

    public Long getCurrentTimeMillis() {
        return currentTimeMillis;
    }

    public void setCurrentTimeMillis(Long currentTimeMillis) {
        this.currentTimeMillis = currentTimeMillis;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }
}
