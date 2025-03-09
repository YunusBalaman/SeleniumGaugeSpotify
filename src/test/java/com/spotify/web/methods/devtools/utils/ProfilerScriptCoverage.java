package com.spotify.web.methods.devtools.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter @AllArgsConstructor @Builder
public class ProfilerScriptCoverage {

    private String scriptId;
    private String url;
    private List<ProfilerFunctionCoverage> functionCoverageList;
}
