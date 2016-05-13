package com.example.martha.gameguide.maneger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.martha.gameguide.R;
import com.example.martha.gameguide.model.UserModel;
import com.example.martha.gameguide.util.Util;

import java.io.ByteArrayOutputStream;

/**
 * Created by Martha on 4/17/2016.
 */
public class UserManager {

    public static final String USER_CACHE_KEY = "user";

    private static UserManager instance = new UserManager();

    private UserModel currentUser;
    private byte [] defaultProfPicBytes;

    public static UserManager instance() {
        return instance;
    }

    private UserManager() {
        currentUser = new UserModel();
    }




    public void initUserModel(Context context) {
        readUserModel(context);
        initDefaultProfPic(context);
        if (currentUser.getAvatarBytes() == null || currentUser.getAvatarBytes().length != 0) {
            setDefaultProfPic();
        }
    }

    public void setDefaultProfPic(){
        currentUser.setAvatarBytes(defaultProfPicBytes);
    }

    public void resetCurrentUser(){
        currentUser.deleteData();
        setDefaultProfPic();
    }

    public void initDefaultProfPic(Context context){
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.prof_pic);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        defaultProfPicBytes = stream.toByteArray();
    }
    public void readUserModel(Context context) {
        currentUser.updateData((UserModel) Util.readCachedObject(context, USER_CACHE_KEY));
    }
    public void updateAndCacheUserModel(UserModel userModel, Context context) {
        currentUser.updateData(userModel);
        if (currentUser.getAvatarBytes() == null && userModel.getAvatarBytes() == null) {
            setDefaultProfPic();
        }
        cacheUserData(context);
    }
    public void cacheUserData(Context context) {
        Util.cacheObject(context, USER_CACHE_KEY, currentUser);
    }

    public boolean isLoggedIn(){
        return currentUser.getToken() != null;
    }


    public void deleteUserData(Context context){
        Util.cacheObject(context, USER_CACHE_KEY, null);
    }
    public UserModel getCurrentUser() {
        return currentUser;
    }



    public void setUdid(String udid){
        currentUser.setUdid(udid);
    }

    public byte[] getAvatarBytes() {
        return currentUser.getAvatarBytes();
    }

}