package com.spotify.web.methods.devtools.utils.debuggerScriptParsed;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter @Setter @AllArgsConstructor
public class DebuggerScriptParsed {
    
    private String scriptId;
    private String url;
    private Integer startLine;
    private Integer startColumn;
    private Integer endLine;
    private Integer endColumn;
    private String executionContextId;
    private Map<String, Object> executionContextAuxData;
    private Boolean isLiveEdit;
    private String sourceMapURL;
    private Boolean isModule;
    private Integer length;
    private Integer codeOffset;
    private String scriptLanguage;
    private String debugSymbols;
    private String embedderName;
}
