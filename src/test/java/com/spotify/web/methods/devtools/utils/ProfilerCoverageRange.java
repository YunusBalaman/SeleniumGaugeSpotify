package com.spotify.web.methods.devtools.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter @Setter @AllArgsConstructor
@Builder
public class ProfilerCoverageRange implements Serializable {

    private Integer count;
    private Integer startOffset;
    private Integer endOffset;

}
