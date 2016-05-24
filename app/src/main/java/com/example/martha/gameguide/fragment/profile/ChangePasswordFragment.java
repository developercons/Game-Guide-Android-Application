package com.example.martha.gameguide.fragment.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.martha.gameguide.R;
import com.example.martha.gameguide.activity.ProfileActivity;
import com.example.martha.gameguide.listener.FragmentActionListener;


/**
 * Created by Martha on 4/14/2016.
 */
public class ChangePasswordFragment extends Fragment {

    // region Static field
    public static final String ACTION_PASSWORD_CHANGE_BACK = "action_password_change_back";
    // endregion

    // region Instance fields
    private ProfileActivity hostActivity;
    private FragmentActionListener actionListener;
    private EditText oldPassword;
    private EditText newPassword;
    private EditText repeatNewPassword;
    // endregion

    // region ctor
    public ChangePasswordFragment() {
    }
    // endregion

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        hostActivity = (ProfileActivity)context;
        actionListener = hostActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // region Views
        View view = inflater.inflate(R.layout.change_password_view, container, false);
        Button changePasswordButton = (Button) view.findViewById(R.id.change_password_view_change_button);
        // endregion

        // region Attributes
        initToolbar(view);
        // endregion

        return view;
    }

    private void initButtonsListeners(){

    }

    private void initToolbar(View rootView) {
        Toolbar toolbar = hostActivity.makeToolbar(rootView.findViewById(R.id.edit_account_toolbar), getString(R.string.edit_account_toolbar_title),
                R.drawable.back, R.drawable.loggedin, null);
        ImageView left = (ImageView)toolbar.findViewById(R.id.toolbar_leftButton);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.actionComplete(ACTION_PASSWORD_CHANGE_BACK);
            }
        });
        toolbar.setBackgroundResource(R.color.white);
        TextView toolbarTitle = (TextView)toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setTextColor(ContextCompat.getColor(toolbarTitle.getContext(), R.color.toolbar_title_color));
    }

    public void handlePasswordChanging(){
        View tempView = getView();
        if(tempView != null) {
            oldPassword = (EditText) tempView.findViewById(R.id.change_password_view_old_password);
            newPassword = (EditText) tempView.findViewById(R.id.change_password_view_new_password);
            repeatNewPassword = (EditText) tempView.findViewById(R.id.change_password_view_repeat_new_password);

            String oldPasswordText = oldPassword.getText().toString();
            String newPasswordText = newPassword.getText().toString();
            String repeatNewPasswordText = repeatNewPassword.getText().toString();
        }
    }

}