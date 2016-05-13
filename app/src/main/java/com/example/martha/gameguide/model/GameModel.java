package com.example.martha.gameguide.model;

import com.example.martha.gameguide.anotation.Request;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Martha on 4/20/2016.
 */
public class GameModel implements Serializable {

    @SerializedName("avatar")
    private byte[] gamePicture;

    @SerializedName("id")
    @Request(game_request = true)
    private long id;

    @SerializedName("name")
    private String gameName;

    @SerializedName("category")
    @Request(game_request = true)
    private String category;

    @SerializedName("expert_definition")
    private String expertDefinition;

    @SerializedName("age_range")
    private String ageRange;

    @SerializedName("expert_mark")
    private int expertMark;

    @SerializedName("expert_review")
    private String expertReview;

    private ArrayList<ReviewModel> reviews = new ArrayList<>();

    @Override
    public String toString() {
        return
        "gamePicture" + gamePicture +
        "\n" + "game_name" + gameName +
        "\n" + "category" + category +
        "\n" + "expert_definition" + expertDefinition +
        "\n" + "age_range" + ageRange +
        "\n" + "expert_mark" + expertMark +
        "\n" + "reviews" + reviews;}

    public GameModel(String gameName, String category,
                     String expertDefinition,
                     String ageRange, int expertMark) {
        this.gameName = gameName;
        this.category = category;
        this.expertDefinition = expertDefinition;
        this.ageRange = ageRange;
        this.expertMark = expertMark;
    }
    public GameModel() {
    }
    public GameModel(String category) {
        this.category = category;
    }
    public GameModel(String category, long id) {
        this.category = category;
        this.id = id;
    }
    public GameModel(long id) {
        this.id = id;
    }

    public void updateData(GameModel dataUpdate){
        if(dataUpdate != null) {
            try {
                Field[] feilds = dataUpdate.getClass().getDeclaredFields();
                for (int i = 0, length = feilds.length; i < length; i++) {
                    feilds[i].setAccessible(true);
                    if (feilds[i].get(dataUpdate) != null) {
                        feilds[i].set(this, feilds[i].get(dataUpdate));
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public byte[] getGamePicture() {
        return gamePicture;
    }

    public void setGamePicture(byte[] gamePicture) {
        this.gamePicture = gamePicture;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getExpertDefinition() {
        return expertDefinition;
    }

    public void setExpertDefinition(String expertDefinition) {
        this.expertDefinition = expertDefinition;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public int getExpertMark() {
        return expertMark;
    }

    public void setExpertMark(int expertMark) {
        this.expertMark = expertMark;
    }

    public String getExpertReview() {
        return expertReview;
    }

    public void setExpertReview(String expertReview) {
        this.expertReview = expertReview;
    }

    public ArrayList<ReviewModel> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<ReviewModel> reviews) {
        this.reviews = reviews;
    }
}
