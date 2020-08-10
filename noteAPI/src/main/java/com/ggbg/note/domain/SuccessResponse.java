package com.ggbg.note.domain;

import java.util.Map;

import io.swagger.annotations.ApiModelProperty;

public class SuccessResponse {
    @ApiModelProperty(value = "status", position = 1)
    public boolean status;
    @ApiModelProperty(value = "result", position = 2)
    public String result;
    @ApiModelProperty(value = "map", position = 3)
    public Map<String, Object> map;
}
