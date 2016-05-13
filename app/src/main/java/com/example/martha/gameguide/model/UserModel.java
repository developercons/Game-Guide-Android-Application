package com.example.martha.gameguide.model;

import com.example.martha.gameguide.anotation.Request;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Created by Martha on 4/17/2016.
 */
public class UserModel implements Serializable {

    @SerializedName("id")
    private long id;

    @SerializedName("first_name")
    @Request(sign_up = true, update = true)
    private String first_name;

    @SerializedName("last_name")
    @Request (sign_up = true, update = true)
    private String last_name;

    @SerializedName("email")
    @Request (sign_up = true, login = true, recover_password = true, update = true)
    private String email;

    @SerializedName("password")
    @Request (sign_up = true, login = true)
    private String password;

    @SerializedName("old_password")
    @Request (change_password = true)
    private String current_password;

    @SerializedName("new_password")
    @Request (change_password = true)
    private String new_password;

    @SerializedName("phone")
    @Request (update = true)
    private String phone;

    private byte[] avatar;

    @SerializedName("udid")
    @Request (sign_up = true, login = true)
    private String udid;

    @SerializedName("token")
    private String token;


    @Override
    public String toString() {
        return "id: " + id + " " +
                "first_name: " + first_name + " " +
                "last_name: " + last_name + " " +
                "email: " + email + " " +
                "password: " + password + " " +
                "current_password: " + current_password + " " +
                "new_password: " + new_password + " " +
                "phone: " + phone + " " +
                "avatar: " + avatar + " " +
                "udid: " + udid + " " +
                "token: " + token + " ";
    }

    public void updateData(UserModel dataUpdate){
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


    public void deleteData(){
        first_name = null;
        last_name = null;
        email = null;
        password = null;
        current_password = null;
        new_password = null;
        phone = null;
        avatar = null;
        token = null;
    }

    public long getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getCurrent_password() {
        return current_password;
    }

    public String getNew_password() {
        return new_password;
    }

    public String getPhone() {
        return phone;
    }

    public byte[] getAvatarBytes() {
        return avatar;
    }

    public String getUdid() {
        return udid;
    }

    public String getToken() {
        return token;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCurrent_password(String current_password) {
        this.current_password = current_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAvatarBytes(byte[] avatar) {
        this.avatar = avatar;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public void setToken(String token) {
        this.token = token;
    }
}