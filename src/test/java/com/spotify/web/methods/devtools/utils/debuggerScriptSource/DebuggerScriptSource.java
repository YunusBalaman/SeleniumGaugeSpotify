package com.spotify.web.methods.devtools.utils.debuggerScriptSource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class DebuggerScriptSource {

    private String scriptSource;
    private String bytecode;
}
