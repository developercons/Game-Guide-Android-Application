package com.example.martha.gameguide.model;

/**
 * Created by Martha on 5/9/2016.
 */
public class NavDrawerItemModel {
    private int icon;
    private String title;

    public NavDrawerItemModel(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public NavDrawerItemModel() {
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
