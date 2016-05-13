package com.example.martha.gameguide.service;


import com.example.martha.gameguide.model.GameModel;

import java.util.HashMap;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Martha on 4/17/2016.
 */
public interface GameService {
    @GET("games/category_list")
    Call<ResponseBundle<Set<String>>> getCategoryList();

    @POST("games/game_ids")
    Call<ResponseBundle<Set<Long>>> getGameIdList(@Body HashMap<String, Object> gameRequestData);

    @POST("games/game")
    Call<ResponseBundle<GameModel>> getAGame(@Body HashMap<String, Object> gameRequestData);

    @POST("games/game_picture")
    Call<ResponseBody> downloadGamePicture(@Body HashMap<String, Object> gameRequestData);
}
