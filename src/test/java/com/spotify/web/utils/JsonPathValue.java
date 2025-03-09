package com.spotify.web.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JsonPathValue {

    private String key;
    private Object value;
    private String type;
    private String id;
    private String valueControlType;
    private Boolean OrActive;
}