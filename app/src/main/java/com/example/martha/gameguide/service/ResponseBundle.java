package com.example.martha.gameguide.service;

import com.example.martha.gameguide.model.ErrorModel;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Martha on 4/19/2016.
 */
public class ResponseBundle<T> {

    @SerializedName("result")
    private T result;
    @SerializedName("error")
    private ErrorModel error;


    public T getResult() {
        return result;
    }
    public ErrorModel getError(){
        return error;
    }
}