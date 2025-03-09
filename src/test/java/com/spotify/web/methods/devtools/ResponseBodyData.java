package com.spotify.web.methods.devtools;

public class ResponseBodyData {

    private String requestId;
    private String requestBody;

    public ResponseBodyData(String requestId, String requestBody) {
        this.requestId = requestId;
        this.requestBody = requestBody;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }
}
