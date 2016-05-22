package com.example.martha.gameguide.maneger;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.example.martha.gameguide.anotation.Request;
import com.example.martha.gameguide.listener.RequestListener;
import com.example.martha.gameguide.model.GameModel;
import com.example.martha.gameguide.model.UserModel;
import com.example.martha.gameguide.service.GameService;
import com.example.martha.gameguide.service.ResponseBundle;
import com.example.martha.gameguide.service.UserService;
import com.example.martha.gameguide.viewholder.GameCardViewHolder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Martha on 4/17/2016.
 */
public class ServiceManager {

    public static final String SERVICE_SIGN_UP = "sign_up";
    public static final String SERVICE_LOGIN = "login";
    public static final String SERVICE_UPDATE = "update";
    public static final String SERVICE_GAME = "game_request";



    public static final String SERVICE_CHANGE_PASSWORD = "change_password";
    public static final String SERVICE_RECOVER_PASSWORD = "recover_password";
    public static final String SERVICE_PROFILE_GET = "profile_get";
    public static final String SERVICE_PROFILE_PUT = "profile_put";
    public static final int CODE_SUCCESS = 200;
    public static final int CODE_WRONG_EMAIL_PASSWORD = 400;

    private static ServiceManager instance = new ServiceManager();
    public static ServiceManager instance() {
        return instance;
    }


    private HashMap<String, Object> requestData;
    public Gson gson = new GsonBuilder().create();
    public final String BASE_URL = "http://gameguideserver-margin.rhcloud.com/webapi/";
    public Retrofit retrofit;


    private UserService userService;
    private GameService gameService;

    private ServiceManager() {
        init();
    }
    private void init() {
        requestData = new HashMap<>();
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        userService = retrofit.create(UserService.class);
        gameService = retrofit.create(GameService.class);
    }
    private HashMap<String, Object> buildRequest(Object object, String requestType) {
        requestData.clear();
        try {
            Field fields[] = object.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; ++i) {
                Request requestAnnotation = fields[i].getAnnotation(Request.class);
                if (requestAnnotation != null) {
                    Method requestMethod = requestAnnotation.getClass().getMethod(requestType);
                    if ((Boolean) (requestMethod.invoke(requestAnnotation, null))) {
                        fields[i].setAccessible(true);
                        Object value = fields[i].get(object);
                        String name = fields[i].getName();
                        SerializedName serializedName = fields[i].getAnnotation(SerializedName.class);
                        if (serializedName != null) name = serializedName.value();
                        requestData.put(name, value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestData;
    }

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }




    public void signUp(final UserModel userModel, final Context context, final RequestListener listener) {
        Call<ResponseBundle<UserModel>> call = userService.sign_up(buildRequest(userModel, SERVICE_SIGN_UP));
        call.enqueue(new Callback<ResponseBundle<UserModel>>() {
            @Override
            public void onResponse(Call<ResponseBundle<UserModel>> call, Response<ResponseBundle<UserModel>> response) {
                ResponseBundle<UserModel> bundle = response.body();
                if (bundle.getResult() != null) {
                    Log.d("MyLogCat", "RESPONSE: " + response.body().getResult().toString());
                    UserModel responseModel = response.body().getResult();
                    UserManager.instance().updateAndCacheUserModel(responseModel, context);
                    Log.d("MyLogCat", "UPDATED USER DATA" + UserManager.instance().getCurrentUser());
                    if (listener != null) listener.onComplete();
                } else {
                    Toast.makeText(context, bundle.getError().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBundle<UserModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void login(final UserModel userModel, final Context context, final RequestListener requestListener){
        Call<ResponseBundle<UserModel>> call = userService.login(buildRequest(userModel, SERVICE_LOGIN));
        call.enqueue(new Callback<ResponseBundle<UserModel>>() {
            @Override
            public void onResponse(Call<ResponseBundle<UserModel>> call, Response<ResponseBundle<UserModel>> response) {
                ResponseBundle<UserModel> bundle = response.body();
                if (bundle.getResult() != null) {
                    final UserModel currentUser = response.body().getResult();
                    downloadProfilePicture(currentUser, context, new RequestListener() {
                        @Override
                        public void onComplete() {
                            UserManager.instance().updateAndCacheUserModel(currentUser, context);
                            if (requestListener != null) {
                                requestListener.onComplete();
                            }
                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(context, "Something went wrong, try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(context, bundle.getError().getMessage(), Toast.LENGTH_SHORT).show();
                    if (requestListener != null) {
                        requestListener.onFailure();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBundle<UserModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void updateUser(final UserModel userModel, final String token, final Context context, final RequestListener requestListener){
        HashMap<String, Object> hashMap = buildRequest(userModel, SERVICE_UPDATE);
        RequestBody userRequest = RequestBody.create(MediaType.parse("application/json"), gson.toJson(hashMap));
        MultipartBody.Part user = MultipartBody.Part.createFormData("user", "userInfo", userRequest);

        MultipartBody.Part profPic = null;
        if (userModel.getAvatarBytes() != null) {
            RequestBody profPicRequest = RequestBody.create(MediaType.parse("image/png"), userModel.getAvatarBytes());
            profPic = MultipartBody.Part.createFormData("picture", "userPicture", profPicRequest);
        }

        Call<ResponseBundle<String>> call = userService.updateUser(token, user, profPic);
        call.enqueue(new Callback<ResponseBundle<String>>() {
            @Override
            public void onResponse(Call<ResponseBundle<String>> call, Response<ResponseBundle<String>> response) {
                if(response.body().getResult() != null){
                    Toast.makeText(context, response.body().getResult(), Toast.LENGTH_SHORT).show();
                    if(requestListener != null){
                        requestListener.onComplete();
                    }
                } else {
                    Toast.makeText(context, response.body().getError().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBundle<String>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void logout(final String token, final Context context, final RequestListener requestListener){
        Call<ResponseBundle<String>> call = userService.logout(token);
        call.enqueue(new Callback<ResponseBundle<String>>() {
            @Override
            public void onResponse(Call<ResponseBundle<String>> call, Response<ResponseBundle<String>> response) {
                ResponseBundle<String> responseBundle = response.body();
                if(responseBundle.getResult() != null){
                    UserManager.instance().resetCurrentUser();
                    if(requestListener != null) {
                        requestListener.onComplete();
                    }
                } else {
                    Toast.makeText(context, responseBundle.getError().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBundle<String>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void getUser(final String token, final Context context, final RequestListener requestListener){
        Call<ResponseBundle<UserModel>> call = userService.getUser(token);
        call.enqueue(new Callback<ResponseBundle<UserModel>>() {
            @Override
            public void onResponse(Call<ResponseBundle<UserModel>> call, Response<ResponseBundle<UserModel>> response) {
                ResponseBundle<UserModel> bundle = response.body();
                if (bundle.getResult() != null) {
                    final UserModel currentUser = response.body().getResult();
                    downloadProfilePicture(currentUser, context, new RequestListener() {
                        @Override
                        public void onComplete() {
                            UserManager.instance().updateAndCacheUserModel(currentUser, context);
                            if (requestListener != null) {
                                requestListener.onComplete();
                            }
                        }
                        @Override
                        public void onFailure() {
                            requestListener.onFailure();
                        }
                    });
                } else {
                    Toast.makeText(context, bundle.getError().getMessage(), Toast.LENGTH_SHORT).show();
                    requestListener.onFailure();
                }
            }
            @Override
            public void onFailure(Call<ResponseBundle<UserModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private void downloadProfilePicture(final UserModel userModel, final Context context, final RequestListener requestListener){
        Call<ResponseBody> call = userService.downloadProfilePicture(userModel.getToken());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody mresponse = response.body();
                if(mresponse != null){
                    byte[] profPic = null;
                    try{
                        profPic = mresponse.bytes();
                    } catch(IOException e){
                        e.printStackTrace();
                    }
                    if(profPic != null && profPic.length != 0) userModel.setAvatarBytes(profPic);
                    if(requestListener != null){
                        requestListener.onComplete();
                    }
                } else {
                    if(requestListener != null){
                        requestListener.onFailure();
                    }

                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void getGameCategoryList(final List<String> source, final Context context, final RequestListener requestListener){
        Call<ResponseBundle<Set<String>>> call = gameService.getCategoryList();
        call.enqueue(new Callback<ResponseBundle<Set<String>>>() {
            @Override
            public void onResponse(Call<ResponseBundle<Set<String>>> call, Response<ResponseBundle<Set<String>>> response) {
                ResponseBundle<Set<String>> responseBundle = response.body();
                Set<String> result = responseBundle.getResult();
                if(result != null){
                    for (String cat : result) {
                        source.add(cat);
                    }
                    if(requestListener != null) {
                        requestListener.onComplete();
                    }
                } else {
                    Toast.makeText(context, responseBundle.getError().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBundle<Set<String>>> call, Throwable t) {

            }
        });
    }
    public void getGameIdList(final GameModel gameRequestModel, final List<Long> sourceIdList, final Context context, final RequestListener listener) {
        Call<ResponseBundle<Set<Long>>> call = gameService.getGameIdList(buildRequest(gameRequestModel, SERVICE_GAME));
        call.enqueue(new Callback<ResponseBundle<Set<Long>>>() {
            @Override
            public void onResponse(Call<ResponseBundle<Set<Long>>> call, Response<ResponseBundle<Set<Long>>> response) {
                ResponseBundle<Set<Long>> responseBundle = response.body();
                Set<Long> result = responseBundle.getResult();
                if(result != null) {
                    sourceIdList.addAll(result);
                    if(listener != null) listener.onComplete();
                } else {
                    Toast.makeText(context, responseBundle.getError().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBundle<Set<Long>>> call, Throwable t) {
                Toast.makeText(context, "getGameIdList onFailure()", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
    public Call getAGame(final GameModel gameRequestModel, final GameCardViewHolder viewHolder, final Context context, final RequestListener listener) {
        Call<ResponseBundle<GameModel>> call = gameService.getAGame(buildRequest(gameRequestModel, SERVICE_GAME));
        call.enqueue(new Callback<ResponseBundle<GameModel>>() {
            @Override
            public void onResponse(Call<ResponseBundle<GameModel>> call, Response<ResponseBundle<GameModel>> response) {
                ResponseBundle<GameModel> bundle = response.body();
                if (bundle.getResult() != null) {
                    final GameModel currentGame = response.body().getResult();
                    gameRequestModel.updateData(currentGame);
                    Call downloadCall = downloadGamePicture(gameRequestModel, context, new RequestListener() {
                        @Override
                        public void onComplete() {
                            if (listener != null) listener.onComplete();
                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(context, "No response body exist", Toast.LENGTH_SHORT).show();
                        }
                    });
                    GameManager.instance().replaceRequestCall(viewHolder, downloadCall);
                } else {
                    Toast.makeText(context, "Error occurred and game is not Loaded", Toast.LENGTH_SHORT).show();
                    if (listener != null) listener.onFailure();
                }
            }

            @Override
            public void onFailure(Call<ResponseBundle<GameModel>> call, Throwable t) {
                if (!call.isCanceled()) {
                    t.printStackTrace();
                    Toast.makeText(context, "getAGame() onFailure!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return call;
    }
    private Call downloadGamePicture(final GameModel gameRequestModel, final Context context, final RequestListener listener) {
        Call<ResponseBody> call = gameService.downloadGamePicture(buildRequest(gameRequestModel, SERVICE_GAME));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody responseBody = response.body();
                if(responseBody != null) {
                    byte[] gamePicture = null;
                    try{
                        gamePicture = responseBody.bytes();
                    } catch(IOException e){
                        e.printStackTrace();
                    }
                    if(gamePicture != null && gamePicture.length != 0) gameRequestModel.setGamePicture(gamePicture);
                    if(listener != null) listener.onComplete();
                } else {
                    if(listener != null) listener.onFailure();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (!call.isCanceled()) {
                    t.printStackTrace();
                    Toast.makeText(context, "downloadGamePic: onFailure.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return call;
    }



}
