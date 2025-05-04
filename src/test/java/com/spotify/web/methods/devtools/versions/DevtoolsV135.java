package com.spotify.web.methods.devtools.versions;

import com.spotify.web.driver.SeleniumDevtools;
import com.spotify.web.methods.MethodsUtil;
import com.spotify.web.methods.devtools.utils.networkRequestWillBeSent.RequestWillBeSentData;
import com.spotify.web.methods.devtools.ResponseBodyData;
import com.spotify.web.methods.devtools.ResponseReceivedData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.devtools.v135.emulation.Emulation;
import org.openqa.selenium.devtools.v135.network.Network;
import org.openqa.selenium.devtools.v135.network.model.PostDataEntry;
import org.openqa.selenium.devtools.v135.network.model.RequestId;
import org.openqa.selenium.devtools.v135.network.model.ResourceType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DevtoolsV135 {

    private static final Logger logger = LogManager.getLogger(DevtoolsV135.class);
    MethodsUtil methodsUtil;

    public DevtoolsV135(){

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
            Optional<ResourceType> type = requestWillBeSent.getType();
            if (methodsUtil.urlFilter(url) && type.isPresent()
                    && (type.get().toString().equals("XHR") || type.get().toString().equals("Fetch"))) {
                RequestWillBeSentData requestWillBeSentData = new RequestWillBeSentData(requestWillBeSent.getRequestId().toString()
                        , url, requestWillBeSent.getRequest().getMethod()
                        , (requestWillBeSent.getRequest().getPostDataEntries().isPresent() ? setRequestPostData(requestWillBeSent.getRequest().getPostDataEntries().get()).toString() : "")
                        , requestWillBeSent.getRequest().getHeaders().toJson(), System.currentTimeMillis(), requestWillBeSent.getDocumentURL());

                SeleniumDevtools.requestIdList.add(requestWillBeSent.getRequestId().toString());
                SeleniumDevtools.requestWillBeSentMap.put(requestWillBeSent.getRequestId().toString(), requestWillBeSentData);
            }
        });
        SeleniumDevtools.devTools.addListener(Network.responseReceived(), responseReceived -> {
            String url = responseReceived.getResponse().getUrl();
            ResourceType type = responseReceived.getType();
            if (methodsUtil.urlFilter(url)
                    && (type.toString().equals("XHR") || type.toString().equals("Fetch"))) {
                ResponseReceivedData responseReceivedData = new ResponseReceivedData(responseReceived.getRequestId().toString()
                        , url, responseReceived.getResponse().getStatus(), responseReceived.getResponse().getMimeType()
                        , responseReceived.getResponse().getHeaders().toJson(), System.currentTimeMillis()
                        , responseReceived.getResponse().getResponseTime().toString());
                SeleniumDevtools.responseReceivedMap.put(responseReceived.getRequestId().toString(), responseReceivedData);
            }
        });
    }

    public List<String> setRequestPostData(List<PostDataEntry> postDataEntries){

        List<String> postDataList = new ArrayList<>();
        postDataEntries.forEach(postDataEntry -> postDataEntry.getBytes().ifPresent(postDataList::add));
        return postDataList;
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
