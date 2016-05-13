package com.example.martha.gameguide.listener;

import android.graphics.Bitmap;

/**
 * Created by Martha on 4/19/2016.
 */
public interface CropFragmentActionListener extends FragmentActionListener {

    void onCropComplete(Bitmap bitmap);

}