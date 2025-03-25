package com.spotify.web.methods.devtools;

import com.spotify.web.driver.SeleniumDevtools;
import com.spotify.web.methods.MethodsUtil;
import com.spotify.web.methods.devtools.utils.ProfilerScriptCoverage;
import com.spotify.web.methods.devtools.versions.Devtoolsv134;
import com.spotify.web.methods.devtools.versions.DevtoolsV133;
import com.spotify.web.methods.devtools.versions.DevtoolsV132;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MethodsDevTools {

    private static final Logger logger = LogManager.getLogger(MethodsDevTools.class);

    MethodsUtil methodsUtil;
    Devtoolsv134 devtoolsV134;
    DevtoolsV133 devtoolsV133;
    DevtoolsV132 devtoolsV132;

    public MethodsDevTools(){

        methodsUtil = new MethodsUtil();
        devtoolsV134 = new Devtoolsv134();
        devtoolsV133 = new DevtoolsV133();
        devtoolsV132 = new DevtoolsV132();
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
            case 134:
                devtoolsV134.enableNetwork();
                break;
            case 133:
                devtoolsV133.enableNetwork();
                break;
            case 132:
                devtoolsV132.enableNetwork();
                break;
            default:
                Assertions.fail(devtoolsVersion + " devtools version hatalı");
        }
    }

    public void disableNetwork(Integer devtoolsVersion){

        switch (devtoolsVersion) {
            case 134:
                devtoolsV134.disableNetwork();
                break;
            case 133:
                devtoolsV133.disableNetwork();
                break;
            case 132:
                devtoolsV132.disableNetwork();
                break;
            default:
                Assertions.fail(devtoolsVersion + " devtools version hatalı");
        }
    }

    public void listenNetworkApiRequest(Integer devtoolsVersion){

        switch (devtoolsVersion){
            case 134:
                devtoolsV134.listenNetworkApiRequest();
                break;
            case 133:
                devtoolsV133.listenNetworkApiRequest();
                break;
            case 132:
                devtoolsV132.listenNetworkApiRequest();
                break;
            default:
                fail(devtoolsVersion + " devtools version hatalı");
        }
    }

    public ResponseBodyData getResponseBody(Integer devtoolsVersion, String requestId){

        ResponseBodyData responseBodyData = null;
        switch (devtoolsVersion){
            case 134:
                responseBodyData = devtoolsV134.getResponseBody(requestId);
                break;
            case 133:
                responseBodyData = devtoolsV133.getResponseBody(requestId);
                break;
            case 132:
                responseBodyData = devtoolsV132.getResponseBody(requestId);
                break;
            default:
                fail(devtoolsVersion + " devtools version hatalı");
        }
        return responseBodyData;
    }

    public String getRequestPostData(Integer devtoolsVersion, String requestId){

        String requestPostData = null;
        switch (devtoolsVersion){
            case 134:
                requestPostData = devtoolsV134.getRequestPostData(requestId);
                break;
            case 133:
                requestPostData = devtoolsV133.getRequestPostData(requestId);
                break;
            case 132:
                requestPostData = devtoolsV132.getRequestPostData(requestId);
                break;
            default:
                fail(devtoolsVersion + " devtools version hatalı");
        }
        return requestPostData;
    }

    public void setGeolocationOverride(Integer devtoolsVersion, Number latitude, Number longitude, Number accuracy){

        switch (devtoolsVersion) {
            case 134:
                devtoolsV134.setGeolocationOverride(latitude, longitude, accuracy);
                break;
            case 133:
                devtoolsV133.setGeolocationOverride(latitude, longitude, accuracy);
                break;
            case 132:
                devtoolsV132.setGeolocationOverride(latitude, longitude, accuracy);
                break;
            default:
                Assertions.fail(devtoolsVersion + " devtools version hatalı");
        }
    }

    public void clientCoverageDemo(Integer devtoolsVersion){

        switch (devtoolsVersion) {
            case 134:
                devtoolsV134.clientCoverageDemo();
                break;
            case 133:
               // devtoolsV129;
                break;
            case 132:
                //devtoolsV128;
                break;
            default:
                Assertions.fail(devtoolsVersion + " devtools version hatalı");
        }
    }

    public  List<ProfilerScriptCoverage> takeClientCoverageDemo(Integer devtoolsVersion){

        List<ProfilerScriptCoverage> profilerScriptCoverageList = null;
        switch (devtoolsVersion) {
            case 134:
                profilerScriptCoverageList = devtoolsV134.takeClientCoverageDemo();
                break;
            case 133:
                // devtoolsV129;
                break;
            case 132:
                //devtoolsV128;
                break;
            default:
                Assertions.fail(devtoolsVersion + " devtools version hatalı");
        }
        return profilerScriptCoverageList;
    }

    public void listenPreciseCoverageDeltaUpdate(Integer devtoolsVersion){

        switch (devtoolsVersion) {
            case 134:
                devtoolsV134.listenPreciseCoverageDeltaUpdate();
                break;
            case 133:
                // devtoolsV129;
                break;
            case 132:
                //devtoolsV128;
                break;
            default:
                Assertions.fail(devtoolsVersion + " devtools version hatalı");
        }
    }

    public void listenDebuggerScriptParsed(Integer devtoolsVersion){

        switch (devtoolsVersion) {
            case 134:
                devtoolsV134.listenDebuggerScriptParsed();
                break;
            case 133:
                // devtoolsV129;
                break;
            case 132:
                //devtoolsV128;
                break;
            default:
                Assertions.fail(devtoolsVersion + " devtools version hatalı");
        }
    }

    public void stopClientCoverage(Integer devtoolsVersion){

        switch (devtoolsVersion) {
            case 134:
                devtoolsV134.stopClientCoverage();
                break;
            case 133:
                // devtoolsV129;
                break;
            case 132:
                //devtoolsV128;
                break;
            default:
                Assertions.fail(devtoolsVersion + " devtools version hatalı");
        }
    }

}
