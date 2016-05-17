package com.example.martha.gameguide.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import com.example.martha.gameguide.R;
import com.example.martha.gameguide.fragment.CropImageFragment;
import com.example.martha.gameguide.fragment.profile.ChangePasswordFragment;
import com.example.martha.gameguide.fragment.profile.ProfileEditFragment;
import com.example.martha.gameguide.fragment.profile.ProfileInfoFragment;
import com.example.martha.gameguide.listener.CropFragmentActionListener;
import com.example.martha.gameguide.maneger.UserManager;


/**
 * Created by Martha on 4/14/2016.
 */
public class ProfileActivity extends BaseActivity implements CropFragmentActionListener {

    private Bitmap currentProfPic;
    private Bitmap tempProfPic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_logged);
        initDrawer();

        ProfileInfoFragment profileInfoFragment = new ProfileInfoFragment();

        loadCurrentProfPic();
        placeFragment(R.id.content_frame, profileInfoFragment, false);
    }
    private void loadCurrentProfPic() {
        if (currentProfPic == null) {
            byte[] avatarBytes = UserManager.instance().getAvatarBytes();
            currentProfPic = BitmapFactory.decodeByteArray(avatarBytes, 0, avatarBytes.length);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (tempProfPic != null) {
            tempProfPic.recycle();
            tempProfPic = null;
        }
    }

    @Override
    public void actionComplete(String actionType) {
        switch (actionType) {
            case ProfileInfoFragment.ACTION_CLICK_EDIT_PROF_BTN:
                placeFragment(R.id.content_frame, new ProfileEditFragment(), true);
                break;
            case ProfileInfoFragment.ACTION_CLICK_FACEBOOK_BTN:
                //TODO Add fb button functionality.
                break;
            case ProfileInfoFragment.ACTION_LOG_OUT_COMPLETE:
                UserManager.instance().deleteUserData(this);
                startActivity(new Intent(this, AuthActivity.class));
                finish();
                // logout
                break;
            case ProfileInfoFragment.ACTION_CLICK_TOOLBAR_BACK:
                finish();
                break;
            case ProfileEditFragment.ACTION_PROFILE_EDIT_BACK:
                triggerBackButton();
                break;
            case ProfileEditFragment.ACTION_CLICK_CHANGE_PROF_PIC:
                placeFragment(R.id.content_frame, new CropImageFragment(), true);
                break;
            case ProfileEditFragment.ACTION_SAVE_PROFILE_COMPLETE:
                if (tempProfPic != null) {
                    currentProfPic.recycle();
                    currentProfPic = tempProfPic;
                    tempProfPic = null;
                }
                triggerBackButton();
                break;
            case ProfileEditFragment.ACTION_CLICK_CHANGE_PASSWORD:
                //placeFragment(R.id.content_frame, new ChangePasswordFragment(), true);
                Toast.makeText(this, "Change password is under construction!", Toast.LENGTH_SHORT).show();
                break;
            case ProfileEditFragment.ACTION_CLICK_TOOLBAR_RIGHT_BTN:
                finish();
                break;
            case CropImageFragment.ACTION_CROP_PROCESS_COMPLETE:
                triggerBackButton();
                break;
            case CropImageFragment.ACTION_CROP_PROCESS_CANCELED:
                triggerBackButton();
                break;
            case ChangePasswordFragment.ACTION_PASSWORD_CHANGE_BACK:
                triggerBackButton();
                break;
        }
    }
    @Override
    public void onCropComplete(Bitmap bitmap) {
        tempProfPic = bitmap;
    }

    public void destroyTempBitmap() {
        tempProfPic.recycle();
        tempProfPic = null;
    }
    public Bitmap getTempProfPic() {
        return tempProfPic;
    }
    public Bitmap getCurrentProfPic() {
        return currentProfPic;
    }
}
