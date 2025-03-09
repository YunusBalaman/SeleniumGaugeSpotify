package com.spotify.web.methods.devtools;

import com.spotify.web.driver.SeleniumDevtools;
import com.spotify.web.methods.MethodsUtil;
import com.spotify.web.methods.devtools.utils.ProfilerScriptCoverage;
import com.spotify.web.methods.devtools.versions.Devtoolsv133;
import com.spotify.web.methods.devtools.versions.DevtoolsV132;
import com.spotify.web.methods.devtools.versions.DevtoolsV131;
import com.spotify.web.methods.devtools.versions.DevtoolsV85;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MethodsDevTools {

    private static final Logger logger = LogManager.getLogger(MethodsDevTools.class);

    MethodsUtil methodsUtil;
    Devtoolsv133 devtoolsV133;
    DevtoolsV132 devtoolsV132;
    DevtoolsV131 devtoolsV131;
    DevtoolsV85 devtoolsV85;

    public MethodsDevTools(){

        methodsUtil = new MethodsUtil();
        devtoolsV133 = new Devtoolsv133();
        devtoolsV132 = new DevtoolsV132();
        devtoolsV131 = new DevtoolsV131();
        devtoolsV85 = new DevtoolsV85();
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
            case 133:
                devtoolsV133.enableNetwork();
                break;
            case 132:
                devtoolsV132.enableNetwork();
                break;
            case 131:
                devtoolsV131.enableNetwork();
                break;
            case 85:
                devtoolsV85.enableNetwork();
                break;
            default:
                Assertions.fail(devtoolsVersion + " devtools version hatalı");
        }
    }

    public void disableNetwork(Integer devtoolsVersion){

        switch (devtoolsVersion) {
            case 133:
                devtoolsV133.disableNetwork();
                break;
            case 132:
                devtoolsV132.disableNetwork();
                break;
            case 131:
                devtoolsV131.disableNetwork();
                break;
            case 85:
                devtoolsV85.disableNetwork();
                break;
            default:
                Assertions.fail(devtoolsVersion + " devtools version hatalı");
        }
    }

    public void listenNetworkApiRequest(Integer devtoolsVersion){

        switch (devtoolsVersion){
            case 133:
                devtoolsV133.listenNetworkApiRequest();
                break;
            case 132:
                devtoolsV132.listenNetworkApiRequest();
                break;
            case 131:
                devtoolsV131.listenNetworkApiRequest();
                break;
            case 85:
                devtoolsV85.listenNetworkApiRequest();
                break;
            default:
                fail(devtoolsVersion + " devtools version hatalı");
        }
    }

    public ResponseBodyData getResponseBody(Integer devtoolsVersion, String requestId){

        ResponseBodyData responseBodyData = null;
        switch (devtoolsVersion){
            case 133:
                responseBodyData = devtoolsV133.getResponseBody(requestId);
                break;
            case 132:
                responseBodyData = devtoolsV132.getResponseBody(requestId);
                break;
            case 131:
                responseBodyData = devtoolsV131.getResponseBody(requestId);
                break;
            case 85:
                responseBodyData = devtoolsV85.getResponseBody(requestId);
                break;
            default:
                fail(devtoolsVersion + " devtools version hatalı");
        }
        return responseBodyData;
    }

    public String getRequestPostData(Integer devtoolsVersion, String requestId){

        String requestPostData = null;
        switch (devtoolsVersion){
            case 133:
                requestPostData = devtoolsV133.getRequestPostData(requestId);
                break;
            case 132:
                requestPostData = devtoolsV132.getRequestPostData(requestId);
                break;
            case 131:
                requestPostData = devtoolsV131.getRequestPostData(requestId);
                break;
            case 85:
                requestPostData = devtoolsV85.getRequestPostData(requestId);
                break;
            default:
                fail(devtoolsVersion + " devtools version hatalı");
        }
        return requestPostData;
    }

    public void setGeolocationOverride(Integer devtoolsVersion, Number latitude, Number longitude, Number accuracy){

        switch (devtoolsVersion) {
            case 133:
                devtoolsV133.setGeolocationOverride(latitude, longitude, accuracy);
                break;
            case 132:
                devtoolsV132.setGeolocationOverride(latitude, longitude, accuracy);
                break;
            case 131:
                devtoolsV131.setGeolocationOverride(latitude, longitude, accuracy);
                break;
            case 85:
                devtoolsV85.setGeolocationOverride(latitude, longitude, accuracy);
                break;
            default:
                Assertions.fail(devtoolsVersion + " devtools version hatalı");
        }
    }

    public void clientCoverageDemo(Integer devtoolsVersion){

        switch (devtoolsVersion) {
            case 133:
                devtoolsV133.clientCoverageDemo();
                break;
            case 132:
               // devtoolsV129;
                break;
            case 131:
                //devtoolsV128;
                break;
            case 85:
                //devtoolsV85;
                break;
            default:
                Assertions.fail(devtoolsVersion + " devtools version hatalı");
        }
    }

    public  List<ProfilerScriptCoverage> takeClientCoverageDemo(Integer devtoolsVersion){

        List<ProfilerScriptCoverage> profilerScriptCoverageList = null;
        switch (devtoolsVersion) {
            case 133:
                profilerScriptCoverageList = devtoolsV133.takeClientCoverageDemo();
                break;
            case 132:
                // devtoolsV129;
                break;
            case 131:
                //devtoolsV128;
                break;
            case 85:
                //devtoolsV85;
                break;
            default:
                Assertions.fail(devtoolsVersion + " devtools version hatalı");
        }
        return profilerScriptCoverageList;
    }

    public void listenPreciseCoverageDeltaUpdate(Integer devtoolsVersion){

        switch (devtoolsVersion) {
            case 133:
                devtoolsV133.listenPreciseCoverageDeltaUpdate();
                break;
            case 132:
                // devtoolsV129;
                break;
            case 131:
                //devtoolsV128;
                break;
            case 85:
                //devtoolsV85;
                break;
            default:
                Assertions.fail(devtoolsVersion + " devtools version hatalı");
        }
    }

    public void listenDebuggerScriptParsed(Integer devtoolsVersion){

        switch (devtoolsVersion) {
            case 133:
                devtoolsV133.listenDebuggerScriptParsed();
                break;
            case 132:
                // devtoolsV129;
                break;
            case 131:
                //devtoolsV128;
                break;
            case 85:
                //devtoolsV85;
                break;
            default:
                Assertions.fail(devtoolsVersion + " devtools version hatalı");
        }
    }

    public void stopClientCoverage(Integer devtoolsVersion){

        switch (devtoolsVersion) {
            case 133:
                devtoolsV133.stopClientCoverage();
                break;
            case 132:
                // devtoolsV129;
                break;
            case 131:
                //devtoolsV128;
                break;
            case 85:
                //devtoolsV85;
                break;
            default:
                Assertions.fail(devtoolsVersion + " devtools version hatalı");
        }
    }

}
