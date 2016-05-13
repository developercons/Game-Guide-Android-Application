package com.example.martha.gameguide.model;

import com.example.martha.gameguide.anotation.Request;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Martha on 5/9/2016.
 */
public class ReviewModel implements Serializable {

    @SerializedName("author")
    private String author;

    @SerializedName("author_prof_pic")
    private byte[] profPic;

    @SerializedName("author_review")
    private String review;
}
