package com.ggbg.note.bean;

import io.swagger.annotations.ApiModelProperty;

public class ExceptionResponse {

    @ApiModelProperty(value = "message", position = 1)
    public String message;
    @ApiModelProperty(value = "status", position = 2)
    public int status;
    @ApiModelProperty(value = "code", position = 3)
    public String code;

}
