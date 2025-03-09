package com.spotify.web.methods.devtools;

public class RequestData {

    private String requestId;
    private String responseBody;

    public RequestData(String requestId, String responseBody) {
        this.requestId = requestId;
        this.responseBody = responseBody;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }
}
