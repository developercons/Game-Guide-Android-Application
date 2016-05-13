package com.example.martha.gameguide.listener;

import android.graphics.Bitmap;

/**
 * Created by Lion on 5/12/16.
 */
public interface DecodeBitmapListener {

    void onComplete(Bitmap bitmap);
    void onFail();

}
