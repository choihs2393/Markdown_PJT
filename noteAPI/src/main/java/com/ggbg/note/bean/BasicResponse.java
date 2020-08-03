package com.ggbg.note.bean;

import io.swagger.annotations.ApiModelProperty;

public class BasicResponse {
    @ApiModelProperty(value = "status", position = 1)
    public boolean status;
    @ApiModelProperty(value = "result", position = 2)
    public String result;
    @ApiModelProperty(value = "object", position = 3)
    public Object object;
}
