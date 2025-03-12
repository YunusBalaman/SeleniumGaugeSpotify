package com.spotify.web.methods.devtools.versions;

import com.spotify.web.driver.SeleniumDevtools;
import com.spotify.web.methods.MethodsUtil;
import com.spotify.web.methods.devtools.utils.networkRequestWillBeSent.RequestWillBeSentData;
import com.spotify.web.methods.devtools.ResponseBodyData;
import com.spotify.web.methods.devtools.ResponseReceivedData;
import com.spotify.web.methods.devtools.utils.ProfilerCoverageRange;
import com.spotify.web.methods.devtools.utils.ProfilerFunctionCoverage;
import com.spotify.web.methods.devtools.utils.ProfilerPreciseCoverage;
import com.spotify.web.methods.devtools.utils.ProfilerScriptCoverage;
import com.spotify.web.methods.devtools.utils.debuggerScriptParsed.DebuggerScriptParsed;
import com.spotify.web.methods.devtools.utils.debuggerScriptSource.DebuggerScriptSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.devtools.v133.debugger.Debugger;
import org.openqa.selenium.devtools.v133.network.Network;
import org.openqa.selenium.devtools.v133.network.model.*;
import org.openqa.selenium.devtools.v133.emulation.Emulation;
import org.openqa.selenium.devtools.v133.page.Page;
import org.openqa.selenium.devtools.v133.profiler.Profiler;
import org.openqa.selenium.devtools.v133.profiler.model.CoverageRange;
import org.openqa.selenium.devtools.v133.profiler.model.FunctionCoverage;
import org.openqa.selenium.devtools.v133.profiler.model.ScriptCoverage;
import org.openqa.selenium.devtools.v133.runtime.Runtime;
import org.openqa.selenium.devtools.v133.runtime.model.ScriptId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Devtoolsv133 {

    private static final Logger logger = LogManager.getLogger(Devtoolsv133.class);
    MethodsUtil methodsUtil;

    public Devtoolsv133(){

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
            //logger.warn("Hatalı requestId: " + requestId);
        }
        return requestPostData;
    }

    public void setGeolocationOverride(Number latitude, Number longitude, Number accuracy){

        SeleniumDevtools.devTools.send(Emulation.setGeolocationOverride(Optional.of(latitude), Optional.of(longitude), Optional.of(accuracy)));
    }

    public void clientCoverageDemo(){

        SeleniumDevtools.devTools.send(Runtime.enable());
       // SeleniumDevtools.devTools.send(DOM.enable(Optional.of(DOM.EnableIncludeWhitespace.ALL))); //CSS
        SeleniumDevtools.devTools.send(Profiler.enable());
       // SeleniumDevtools.devTools.send(CSS.enable()); //CSS
       // SeleniumDevtools.devTools.send(CSS.startRuleUsageTracking()); //CSS
        SeleniumDevtools.devTools.send(Page.enable());
        SeleniumDevtools.devTools.send(Profiler.startPreciseCoverage(Optional.of(true), Optional.of(true), Optional.of(true)));
        SeleniumDevtools.devTools.send(Debugger.enable(Optional.empty()));
        SeleniumDevtools.devTools.send(Debugger.setSkipAllPauses(true));
    }

    public List<ProfilerScriptCoverage> takeClientCoverageDemo(){

        Profiler.TakePreciseCoverageResponse takePreciseCoverageResponse = SeleniumDevtools.devTools.send(Profiler.takePreciseCoverage());
        return getProfilerScriptCoverage(takePreciseCoverageResponse.getResult());
    }

    public List<ProfilerScriptCoverage> getProfilerScriptCoverage(List<ScriptCoverage> scriptCoverageList){

        List<ProfilerScriptCoverage> profilerScriptCoverageList = new ArrayList<>();
        scriptCoverageList.forEach(scriptCoverage -> profilerScriptCoverageList.add(new ProfilerScriptCoverage(scriptCoverage.getScriptId().toString()
                , scriptCoverage.getUrl(), getProfilerFunctionList(scriptCoverage.getFunctions()))));
        return profilerScriptCoverageList;
    }

    private List<ProfilerFunctionCoverage> getProfilerFunctionList(List<FunctionCoverage> list){

        List<ProfilerFunctionCoverage> profilerFunctionCoverageList = new ArrayList<>();
        list.forEach(functionCoverage -> profilerFunctionCoverageList.add(new ProfilerFunctionCoverage(functionCoverage.getFunctionName()
                , functionCoverage.getIsBlockCoverage(), getProfilerCoverageRange(functionCoverage.getRanges()))));
        return profilerFunctionCoverageList;
    }

    private List<ProfilerCoverageRange> getProfilerCoverageRange(List<CoverageRange> list){

        List<ProfilerCoverageRange> profilerCoverageRangeList = new ArrayList<>();
        list.forEach(coverageRange -> profilerCoverageRangeList.add(new ProfilerCoverageRange(coverageRange.getCount()
                , coverageRange.getStartOffset(), coverageRange.getEndOffset())));
        return profilerCoverageRangeList;
    }

    public void listenPreciseCoverageDeltaUpdate(){

        SeleniumDevtools.devTools.addListener(Profiler.preciseCoverageDeltaUpdate(), preciseCoverageDeltaUpdate -> SeleniumDevtools
                .clientCoverageList.add(new ProfilerPreciseCoverage(preciseCoverageDeltaUpdate.getTimestamp()
                        , preciseCoverageDeltaUpdate.getOccasion(), getProfilerScriptCoverage(preciseCoverageDeltaUpdate.getResult()))));
    }

    public void listenDebuggerScriptParsed(){

        SeleniumDevtools.devTools.addListener(Debugger.scriptParsed(), scriptParsed -> SeleniumDevtools
                .debuggerScriptParsedList.add(new DebuggerScriptParsed(scriptParsed.getScriptId().toString()
                , scriptParsed.getUrl(), scriptParsed.getStartLine(), scriptParsed.getStartColumn(), scriptParsed.getEndLine()
                , scriptParsed.getEndColumn(), scriptParsed.getExecutionContextId().toString()
                        , scriptParsed.getExecutionContextAuxData().isPresent() ? scriptParsed.getExecutionContextAuxData().orElse(null) : null
                        , scriptParsed.getIsLiveEdit().isPresent() ? scriptParsed.getIsLiveEdit().orElse(null) : null
                        , scriptParsed.getSourceMapURL().isPresent() ? scriptParsed.getSourceMapURL().orElse("") : ""
                        , scriptParsed.getIsModule().isPresent() ? scriptParsed.getIsModule().orElse(null) : null
                        , scriptParsed.getLength().isPresent() ? scriptParsed.getLength().orElse(null) : null
                        , scriptParsed.getCodeOffset().isPresent() ? scriptParsed.getCodeOffset().orElse(null) : null
                        , scriptParsed.getScriptLanguage().isPresent() ? scriptParsed.getScriptLanguage().get().toString() : ""
                        , scriptParsed.getDebugSymbols().isPresent() ? scriptParsed.getDebugSymbols().get().toString() : null
                        , scriptParsed.getEmbedderName().isPresent() ? scriptParsed.getEmbedderName().get() : "" )));
    }

    public void listenExecutionContextsCleared(){

        SeleniumDevtools.devTools.addListener(Runtime.executionContextsCleared(), executionContextsCleared -> SeleniumDevtools.executionContextsClearedList.add(executionContextsCleared.toString()));
    }

    public DebuggerScriptSource getScriptSource(String scriptId){

        DebuggerScriptSource debuggerScriptSource = null;
        Debugger.GetScriptSourceResponse scriptSourceResponse = null;
        try {
            scriptSourceResponse = SeleniumDevtools.devTools.send(Debugger.getScriptSource(new ScriptId(scriptId)));
            debuggerScriptSource = new DebuggerScriptSource(scriptSourceResponse.getScriptSource(), scriptSourceResponse.getBytecode().isPresent() ? scriptSourceResponse.getBytecode().get() : "");
        } catch (Throwable e) {
            logger.warn("Hatalı requestId: " + scriptId);
        }
        return debuggerScriptSource;
    }

    public void stopClientCoverage(){

        SeleniumDevtools.devTools.send(Profiler.stopPreciseCoverage());
        SeleniumDevtools.devTools.send(Profiler.disable());
        SeleniumDevtools.devTools.send(Debugger.disable());
       // SeleniumDevtools.devTools.send(CSS.stopRuleUsageTracking()); //CSS
        //SeleniumDevtools.devTools.send(CSS.disable()); //CSS
        SeleniumDevtools.devTools.send(Page.disable());
       // SeleniumDevtools.devTools.send(DOM.disable()); //CSS
        SeleniumDevtools.devTools.send(Runtime.disable());

        //  chromeOptions.setExperimentalOption("debuggerAddress","127.0.0.1:9111");
    }

    /**
     Css

     clientEmitter.on('CSS.styleSheetAdded', this.#onStyleSheet.bind(this));
     clientEmitter.on(
     'Runtime.executionContextsCleared',
     this.#onExecutionContextsCleared.bind(this),
     );

     const response = await this.#client.send('CSS.getStyleSheetText', {
     styleSheetId: header.styleSheetId,
     });
     */
}
