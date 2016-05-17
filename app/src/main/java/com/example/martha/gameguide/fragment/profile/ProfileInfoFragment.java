package com.example.martha.gameguide.fragment.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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


/**
 * Created by Martha on 4/10/2016.
 */

public class ProfileInfoFragment extends Fragment {

    public static final String ACTION_CLICK_EDIT_PROF_BTN = "action_click_edit_prof_btn";
    public static final String ACTION_CLICK_FACEBOOK_BTN = "action_click_facebook_btn";
    public static final String ACTION_CLICK_TOOLBAR_BACK = "action_click_toolbar_back";
    public static final String ACTION_LOG_OUT_COMPLETE = "action_log_out_complete";

    private int tollbarLeftButton;
    private int tollbarRightButton;


    private ProfileActivity hostActivity;
    private FragmentActionListener actionListener;

    private TextView editProfile;
    private Button facebookButton;
    private Button logoutButton;
    TextView firstLastNames;

    public ProfileInfoFragment() {
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
        // region View
        View view = inflater.inflate(R.layout.profile_view, container, false);
        ImageView profPicView = (ImageView)view.findViewById(R.id.profile_view_prof_pic);
        firstLastNames = (TextView)view.findViewById(R.id.profile_view_name_surname);
        editProfile = (TextView)view.findViewById(R.id.profile_view_edit_account);
        facebookButton = (Button)view.findViewById(R.id.profile_view_disconnect_with_fb);
        logoutButton = (Button)view.findViewById(R.id.profile_view_logout_button);

        profPicView.setImageBitmap(hostActivity.getCurrentProfPic());
        UserModel currentUser = UserManager.instance().getCurrentUser();
        if(currentUser != null){
            firstLastNames.setText(currentUser.getFirst_name() + " " + currentUser.getLast_name());
            Toast.makeText(getActivity(), "User info updated", Toast.LENGTH_SHORT).show();
        }
        // endregion

        initToolbar(view);
        initButtonListeners();

        return view;
    }

    private void initButtonListeners() {
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.actionComplete(ACTION_CLICK_EDIT_PROF_BTN);
            }
        });
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.actionComplete(ACTION_CLICK_FACEBOOK_BTN);
            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(UserManager.instance().getCurrentUser().getToken(), new RequestListener() {
                    @Override
                    public void onComplete() {
                        actionListener.actionComplete(ACTION_LOG_OUT_COMPLETE);
                    }
                });
            }
        });
    }

    private void logout(String token, RequestListener requestListener){
        ServiceManager.instance().logout(token, getContext(), requestListener);
    }

    private void initToolbar(View rootView) {
        Toolbar toolbar = hostActivity.makeToolbar(rootView.findViewById(R.id.profile_toolbar), getString(R.string.profile_toolbar_title),
                R.drawable.back, null, null);
        TextView toolbarTitle = (TextView)toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setTextColor(ContextCompat.getColor(toolbarTitle.getContext(), R.color.white));

        ImageView backButton = (ImageView) toolbar.findViewById(R.id.toolbar_leftButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.actionComplete(ACTION_CLICK_TOOLBAR_BACK);
            }
        });
    }
}