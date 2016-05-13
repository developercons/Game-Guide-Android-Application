package com.example.martha.gameguide.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.martha.gameguide.R;
import com.example.martha.gameguide.activity.ProfileActivity;
import com.example.martha.gameguide.listener.CropFragmentActionListener;
import com.example.martha.gameguide.util.Util;
import com.isseiaoki.simplecropview.CropImageView;

/**
 * Created by Martha on 4/11/2016.
 */
public class CropImageFragment extends Fragment {

    public static final String ACTION_CROP_PROCESS_COMPLETE = "action_crop_process_complete";
    public static final String ACTION_CROP_PROCESS_CANCELED = "action_crop_process_canceled";

    private ProfileActivity hostActivity;
    private CropFragmentActionListener actionListener;

    private final int imageRequestCode = 1;
    private CropImageView cropImageView;

    public CropImageFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        hostActivity = (ProfileActivity)context;
        actionListener = hostActivity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cropper, container, false);
        cropImageView = (CropImageView)view.findViewById(R.id.cropImageView);
        cropImageView.setCropMode(CropImageView.CropMode.RATIO_FREE);
        Button cropButton = (Button)view.findViewById(R.id.crop_button);

        initCropBtnListener(cropButton);
        chooseImage();

        return view;
    }

    private void initCropBtnListener(Button cropButton) {
        cropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO recycle old source bitmap.
                actionListener.onCropComplete(cropImageView.getCroppedBitmap());
                actionListener.actionComplete(ACTION_CROP_PROCESS_COMPLETE);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == imageRequestCode) {
            if (data == null) {
                actionListener.actionComplete(ACTION_CROP_PROCESS_CANCELED);
                return;
            }
            String path = Util.getImageFilePath(data, hostActivity.getContentResolver());
            Bitmap picture = Util.decodeSampledBitmapFromFile(path, cropImageView.getWidth() / 2, cropImageView.getHeight() / 2);
            cropImageView.setImageBitmap(picture);
        }
    }

    private void chooseImage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, imageRequestCode);
    }


}