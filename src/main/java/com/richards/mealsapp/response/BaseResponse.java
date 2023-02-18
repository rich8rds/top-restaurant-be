package com.richards.mealsapp.response;

import com.richards.mealsapp.enums.ResponseCodeEnum;
import lombok.*;

@Data
public class BaseResponse<T> {
    private int code;
    private String description;
    private T payload;

     public BaseResponse() {
        this(ResponseCodeEnum.ERROR); // default value
    }

    public BaseResponse(ResponseCodeEnum responseCode) {
        this.code = responseCode.getCode();
        this.description = responseCode.getDescription();
    }

    public BaseResponse(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public BaseResponse(ResponseCodeEnum responseCode, String description) {
        this.code = responseCode.getCode();
        this.description = description;
    }


    public BaseResponse(int code, String description, T payload) {
        this.code = code;
        this.description = description;
        this.payload = payload;
    }

    public BaseResponse(ResponseCodeEnum responseCode, T payload) {
        this.code = responseCode.getCode();
        this.description = responseCode.getDescription();
        this.payload  = payload;
    }
}
