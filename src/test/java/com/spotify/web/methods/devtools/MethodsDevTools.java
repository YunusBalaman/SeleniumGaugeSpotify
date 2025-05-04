package com.spotify.web.methods.devtools;

import com.spotify.web.driver.SeleniumDevtools;
import com.spotify.web.methods.MethodsUtil;
import com.spotify.web.methods.devtools.utils.ProfilerScriptCoverage;
import com.spotify.web.methods.devtools.versions.Devtoolsv136;
import com.spotify.web.methods.devtools.versions.DevtoolsV135;
import com.spotify.web.methods.devtools.versions.DevtoolsV134;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MethodsDevTools {

    private static final Logger logger = LogManager.getLogger(MethodsDevTools.class);

    MethodsUtil methodsUtil;
    Devtoolsv136 devtoolsV136;
    DevtoolsV135 devtoolsV135;
    DevtoolsV134 devtoolsV134;

    public MethodsDevTools(){

        methodsUtil = new MethodsUtil();
        devtoolsV136 = new Devtoolsv136();
        devtoolsV135 = new DevtoolsV135();
        devtoolsV134 = new DevtoolsV134();
    }

    public void clearListenerAndCloseDevtools(){

        if (SeleniumDevtools.devTools != null) {
            SeleniumDevtools.clearListeners();
            disableNetwork(SeleniumDevtools.devtoolsVersion);
            SeleniumDevtools.closeDevtools();
        }
    }

    public void enableNetwork(Integer devtoolsVersion){

        switch (devtoolsVersion) {
            case 136:
                devtoolsV136.enableNetwork();
                break;
            case 135:
                devtoolsV135.enableNetwork();
                break;
            case 134:
                devtoolsV134.enableNetwork();
                break;
            default:
                Assertions.fail(devtoolsVersion + " devtools version hatalı");
        }
    }

    public void disableNetwork(Integer devtoolsVersion){

        switch (devtoolsVersion) {
            case 136:
                devtoolsV136.disableNetwork();
                break;
            case 135:
                devtoolsV135.disableNetwork();
                break;
            case 134:
                devtoolsV134.disableNetwork();
                break;
            default:
                Assertions.fail(devtoolsVersion + " devtools version hatalı");
        }
    }

    public void listenNetworkApiRequest(Integer devtoolsVersion){

        switch (devtoolsVersion){
            case 136:
                devtoolsV136.listenNetworkApiRequest();
                break;
            case 135:
                devtoolsV135.listenNetworkApiRequest();
                break;
            case 134:
                devtoolsV134.listenNetworkApiRequest();
                break;
            default:
                fail(devtoolsVersion + " devtools version hatalı");
        }
    }

    public ResponseBodyData getResponseBody(Integer devtoolsVersion, String requestId){

        ResponseBodyData responseBodyData = null;
        switch (devtoolsVersion){
            case 136:
                responseBodyData = devtoolsV136.getResponseBody(requestId);
                break;
            case 135:
                responseBodyData = devtoolsV135.getResponseBody(requestId);
                break;
            case 134:
                responseBodyData = devtoolsV134.getResponseBody(requestId);
                break;
            default:
                fail(devtoolsVersion + " devtools version hatalı");
        }
        return responseBodyData;
    }

    public String getRequestPostData(Integer devtoolsVersion, String requestId){

        String requestPostData = null;
        switch (devtoolsVersion){
            case 136:
                requestPostData = devtoolsV136.getRequestPostData(requestId);
                break;
            case 135:
                requestPostData = devtoolsV135.getRequestPostData(requestId);
                break;
            case 134:
                requestPostData = devtoolsV134.getRequestPostData(requestId);
                break;
            default:
                fail(devtoolsVersion + " devtools version hatalı");
        }
        return requestPostData;
    }

    public void setGeolocationOverride(Integer devtoolsVersion, Number latitude, Number longitude, Number accuracy){

        switch (devtoolsVersion) {
            case 136:
                devtoolsV136.setGeolocationOverride(latitude, longitude, accuracy);
                break;
            case 135:
                devtoolsV135.setGeolocationOverride(latitude, longitude, accuracy);
                break;
            case 134:
                devtoolsV134.setGeolocationOverride(latitude, longitude, accuracy);
                break;
            default:
                Assertions.fail(devtoolsVersion + " devtools version hatalı");
        }
    }

    public void clientCoverageDemo(Integer devtoolsVersion){

        switch (devtoolsVersion) {
            case 136:
                devtoolsV136.clientCoverageDemo();
                break;
            case 135:
               // devtoolsV129;
                break;
            case 134:
                //devtoolsV128;
                break;
            default:
                Assertions.fail(devtoolsVersion + " devtools version hatalı");
        }
    }

    public  List<ProfilerScriptCoverage> takeClientCoverageDemo(Integer devtoolsVersion){

        List<ProfilerScriptCoverage> profilerScriptCoverageList = null;
        switch (devtoolsVersion) {
            case 136:
                profilerScriptCoverageList = devtoolsV136.takeClientCoverageDemo();
                break;
            case 135:
                // devtoolsV129;
                break;
            case 134:
                //devtoolsV128;
                break;
            default:
                Assertions.fail(devtoolsVersion + " devtools version hatalı");
        }
        return profilerScriptCoverageList;
    }

    public void listenPreciseCoverageDeltaUpdate(Integer devtoolsVersion){

        switch (devtoolsVersion) {
            case 136:
                devtoolsV136.listenPreciseCoverageDeltaUpdate();
                break;
            case 135:
                // devtoolsV129;
                break;
            case 134:
                //devtoolsV128;
                break;
            default:
                Assertions.fail(devtoolsVersion + " devtools version hatalı");
        }
    }

    public void listenDebuggerScriptParsed(Integer devtoolsVersion){

        switch (devtoolsVersion) {
            case 136:
                devtoolsV136.listenDebuggerScriptParsed();
                break;
            case 135:
                // devtoolsV129;
                break;
            case 134:
                //devtoolsV128;
                break;
            default:
                Assertions.fail(devtoolsVersion + " devtools version hatalı");
        }
    }

    public void stopClientCoverage(Integer devtoolsVersion){

        switch (devtoolsVersion) {
            case 136:
                devtoolsV136.stopClientCoverage();
                break;
            case 135:
                // devtoolsV129;
                break;
            case 134:
                //devtoolsV128;
                break;
            default:
                Assertions.fail(devtoolsVersion + " devtools version hatalı");
        }
    }

}
