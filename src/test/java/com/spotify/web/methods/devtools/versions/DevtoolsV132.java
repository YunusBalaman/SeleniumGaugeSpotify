package com.spotify.web.methods.devtools.versions;

import com.spotify.web.driver.SeleniumDevtools;
import com.spotify.web.methods.MethodsUtil;
import com.spotify.web.methods.devtools.utils.networkRequestWillBeSent.RequestWillBeSentData;
import com.spotify.web.methods.devtools.ResponseBodyData;
import com.spotify.web.methods.devtools.ResponseReceivedData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.devtools.v132.emulation.Emulation;
import org.openqa.selenium.devtools.v132.network.Network;
import org.openqa.selenium.devtools.v132.network.model.RequestId;

import java.util.Optional;

public class DevtoolsV132 {

    private static final Logger logger = LogManager.getLogger(DevtoolsV132.class);
    MethodsUtil methodsUtil;

    public DevtoolsV132(){

        methodsUtil = new MethodsUtil();
    }

    public void enableNetwork(){

        SeleniumDevtools.devTools.send(Network.enable(Optional.empty(),Optional.empty(),Optional.empty()));
    }

    public void disableNetwork(){

        SeleniumDevtools.devTools.send(Network.disable());
    }

    public void listenNetworkApiRequest(){

        SeleniumDevtools.devTools.addListener(Network.requestWillBeSent(), requestWillBeSent -> {
            String url = requestWillBeSent.getRequest().getUrl();
            if (methodsUtil.urlFilter(url) && requestWillBeSent.getType().isPresent()
                    && requestWillBeSent.getType().get().toString().equals("XHR")) {
                RequestWillBeSentData requestWillBeSentData = new RequestWillBeSentData(requestWillBeSent.getRequestId().toString()
                        , url, requestWillBeSent.getRequest().getMethod()
                        , (requestWillBeSent.getRequest().getPostData().isPresent() ? requestWillBeSent.getRequest().getPostData().get() : "")
                        , requestWillBeSent.getRequest().getHeaders().toJson(), System.currentTimeMillis(), requestWillBeSent.getDocumentURL());

                SeleniumDevtools.requestIdList.add(requestWillBeSent.getRequestId().toString());
                SeleniumDevtools.requestWillBeSentMap.put(requestWillBeSent.getRequestId().toString(), requestWillBeSentData);
            }
        });
        SeleniumDevtools.devTools.addListener(Network.responseReceived(), responseReceived -> {

            String url = responseReceived.getResponse().getUrl();
            if (methodsUtil.urlFilter(url)
                    && responseReceived.getType().toString().equals("XHR")) {
                ResponseReceivedData responseReceivedData = new ResponseReceivedData(responseReceived.getRequestId().toString()
                        , url, responseReceived.getResponse().getStatus(), responseReceived.getResponse().getMimeType()
                        , responseReceived.getResponse().getHeaders().toJson(), System.currentTimeMillis()
                        , responseReceived.getResponse().getResponseTime().toString());
                SeleniumDevtools.responseReceivedMap.put(responseReceived.getRequestId().toString(), responseReceivedData);
            }
        });
    }

    public ResponseBodyData getResponseBody(String requestId){

        ResponseBodyData responseBodyData = null;
        try {
            Network.GetResponseBodyResponse responseBody = SeleniumDevtools.devTools.send(Network.getResponseBody(new RequestId(requestId)));
            responseBodyData = new ResponseBodyData(requestId, responseBody.getBody());
        } catch (Throwable e) {
            logger.warn("Hatalı requestId: " + requestId);
        }
        return responseBodyData;
    }

    public String getRequestPostData(String requestId){

        String requestPostData = null;
        try {
            requestPostData = SeleniumDevtools.devTools.send(Network.getRequestPostData(new RequestId(requestId)));
        } catch (Throwable e) {
            logger.warn("Hatalı requestId: " + requestId);
        }
        return requestPostData;
    }

    public void setGeolocationOverride(Number latitude, Number longitude, Number accuracy){

        /**HashMap<String,Object> coordinates = new HashMap<>();
         coordinates.put("latitude", 28.613939);
         coordinates.put("longitude", 77.209023);
         coordinates.put("accuracy", 100);

         ((ChromeDriver)Driver.webDriver).executeCdpCommand("Emulation.setGeolocationOverride", coordinates);
         */
        // SeleniumDevtools.devTools.send(Emulation.setGeolocationOverride(Optional.of(28.613939),Optional.of(77.209023),Optional.of(100)));
        // SeleniumDevtools.devTools.send(Emulation.setGeolocationOverride(Optional.of(40.741895),Optional.of(-73.989308),Optional.of(100)));

        SeleniumDevtools.devTools.send(Emulation.setGeolocationOverride(Optional.of(latitude),Optional.of(longitude),Optional.of(accuracy)));
    }
}
