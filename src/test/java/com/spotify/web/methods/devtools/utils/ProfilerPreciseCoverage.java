package com.spotify.web.methods.devtools.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter @AllArgsConstructor
public class ProfilerPreciseCoverage {

    private Number timestamp;
    private String occasion;
    private List<ProfilerScriptCoverage> result;
}
