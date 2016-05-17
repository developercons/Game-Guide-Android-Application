package com.example.martha.gameguide.fragment.profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.martha.gameguide.R;
import com.example.martha.gameguide.activity.ProfileActivity;
import com.example.martha.gameguide.listener.FragmentActionListener;
import com.example.martha.gameguide.listener.RequestListener;
import com.example.martha.gameguide.maneger.ServiceManager;
import com.example.martha.gameguide.maneger.UserManager;
import com.example.martha.gameguide.model.UserModel;
import com.example.martha.gameguide.util.Util;

import java.io.ByteArrayOutputStream;

/**
 * Created by Martha on 4/10/2016.
 */

public class ProfileEditFragment extends Fragment {

    public static final String ACTION_CLICK_CHANGE_PROF_PIC = "action_click_change_prof_pic";
    public static final String ACTION_CLICK_TOOLBAR_RIGHT_BTN = "action_click_toolbar_right_btn";
    public static final String ACTION_CLICK_CHANGE_PASSWORD = "action_click_change_password";
    public static final String ACTION_SAVE_PROFILE_COMPLETE = "action_save_profile_complete";
    public static final String ACTION_PROFILE_EDIT_BACK = "action_profile_edit_back";

    private ProfileActivity hostActivity;
    private FragmentActionListener actionListener;

    private ImageView profilePicView;
    private TextView changeProfilePicButton;
    private Button saveButton;

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText phoneNumber;
    private TextView changePassword;

    private ImageView toolbarLeftButton;
    private ImageView toolbarRightButton;


    public ProfileEditFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        hostActivity = (ProfileActivity)context;
        actionListener = hostActivity;

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // region Views
        View view = inflater.inflate(R.layout.edit_account_view, container, false);
        firstName = (EditText)view.findViewById(R.id.edit_account_view_name);
        lastName = (EditText)view.findViewById(R.id.edit_account_view_surname);
        email = (EditText)view.findViewById(R.id.edit_account_view_email);
        phoneNumber = (EditText)view.findViewById(R.id.edit_account_view_phone);
        changePassword = (TextView)view.findViewById(R.id.edit_account_view_change_password);
        profilePicView = (ImageView)view.findViewById(R.id.edit_account_view_profile_picture);
        saveButton = (Button)view.findViewById(R.id.edit_account_view_save_button);
        changeProfilePicButton = (TextView)view.findViewById(R.id.change_photo);

        if (hostActivity.getTempProfPic() != null) {
            profilePicView.setImageBitmap(hostActivity.getTempProfPic());
        } else {
            profilePicView.setImageBitmap(hostActivity.getCurrentProfPic());
        }


        UserModel user = UserManager.instance().getCurrentUser();

        if (user.getFirst_name() != null && !user.getFirst_name().isEmpty()) firstName.setHint(user.getFirst_name());
        if (user.getLast_name() != null && !user.getLast_name().isEmpty()) lastName.setHint(user.getLast_name());
        if (user.getEmail() != null && !user.getEmail().isEmpty()) email.setHint(user.getEmail());
        if (user.getPhone() != null && !user.getPhone().isEmpty()) phoneNumber.setHint(user.getPhone());

        initButtonListeners();
        initToolbar(view);
        hostActivity.manageKeyPadActions(view, profilePicView, changeProfilePicButton, null);


        return view;
    }



    private void initToolbar(View rootView) {
        Toolbar toolbar = hostActivity.makeToolbar(rootView.findViewById(R.id.edit_account_toolbar), getString(R.string.edit_account_toolbar_title),
                R.drawable.back, R.drawable.list, null);
        ImageView left = (ImageView)toolbar.findViewById(R.id.toolbar_leftButton);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.actionComplete(ACTION_PROFILE_EDIT_BACK);
            }
        });
        ImageView right = (ImageView)toolbar.findViewById(R.id.toolbar_rightButton);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.actionComplete(ACTION_CLICK_TOOLBAR_RIGHT_BTN);
            }
        });
        TextView toolbarTitle = (TextView)toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setTextColor(ContextCompat.getColor(toolbarTitle.getContext(), R.color.white));
        toolbarLeftButton = left;
        toolbarRightButton = right;
    }
    private void initButtonListeners() {
        changeProfilePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.actionComplete(ACTION_CLICK_CHANGE_PROF_PIC);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ServiceManager.isConnectedToInternet(getContext())) {
                    Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                String emailText = email.getText().toString();
                String phoneText = phoneNumber.getText().toString();
                String firstNameText = firstName.getText().toString();
                String lastNameText = lastName.getText().toString();

                if (!emailText.isEmpty() && !Util.isEmailValid(emailText)) {
                    Util.markInvalidField(email, hostActivity.getAnimBlink());
                    Toast.makeText(getActivity(), "Email is not valid", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!phoneText.isEmpty() && !Util.isPhoneValid(phoneText)) {
                    Util.markInvalidField(phoneNumber, hostActivity.getAnimBlink());
                    Toast.makeText(getActivity(), "Phone number is not valid", Toast.LENGTH_SHORT).show();
                    return;
                }

                final UserModel currentUser = UserManager.instance().getCurrentUser();

                final UserModel updatedUserModel = new UserModel();
                if (!firstNameText.isEmpty()) updatedUserModel.setFirst_name(firstNameText);
                    else{updatedUserModel.setFirst_name(currentUser.getFirst_name());}
                if (!lastNameText.isEmpty()) updatedUserModel.setLast_name(lastNameText);
                    else{updatedUserModel.setLast_name(currentUser.getLast_name());}
                if (!emailText.isEmpty()) updatedUserModel.setEmail(emailText);
                    else{updatedUserModel.setEmail(currentUser.getEmail());}
                if (!phoneText.isEmpty()) updatedUserModel.setPhone(phoneText);
                    else{updatedUserModel.setPhone(currentUser.getPhone());}

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (hostActivity.getTempProfPic() != null) {
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            hostActivity.getTempProfPic().compress(Bitmap.CompressFormat.PNG, 10, stream);
                            updatedUserModel.setAvatarBytes(stream.toByteArray());
                        }
                        System.out.println("***************************BITMAP COMPRESSED*************");
                        hostActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                enableAllButtons(false);
                            }
                        });

                        ServiceManager.instance().updateUser(updatedUserModel, currentUser.getToken(),
                                getContext(), new RequestListener() {
                                    @Override
                                    public void onComplete() {
                                        UserManager.instance().updateAndCacheUserModel(updatedUserModel, getContext());
                                        hostActivity.runOnUiThread(new Runnable() {
                                            public void run() {
                                                enableAllButtons(true);
                                            }
                                        });
                                        actionListener.actionComplete(ACTION_SAVE_PROFILE_COMPLETE);
                                    }
                                });
                    }
                });
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("change password", "Change password pressed");
                actionListener.actionComplete(ACTION_CLICK_CHANGE_PASSWORD);
            }
        });

    }

    private void enableAllButtons(boolean index){
        changeProfilePicButton.setEnabled(index);
        saveButton.setEnabled(index);
        firstName.setEnabled(index);
        lastName.setEnabled(index);
        email.setEnabled(index);
        phoneNumber.setEnabled(index);
        changePassword.setEnabled(index);
        toolbarLeftButton.setEnabled(index);
        toolbarRightButton.setEnabled(index);
    }
}