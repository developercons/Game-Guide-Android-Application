package com.example.martha.gameguide.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Martha on 4/20/2016.
 */
public class ErrorModel {

    @SerializedName("code")
    private String code;
    @SerializedName("message")
    private String message;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
