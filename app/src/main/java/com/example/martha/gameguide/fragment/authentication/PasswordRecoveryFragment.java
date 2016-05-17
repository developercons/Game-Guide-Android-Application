package com.example.martha.gameguide.fragment.authentication;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.martha.gameguide.R;
import com.example.martha.gameguide.activity.AuthActivity;
import com.example.martha.gameguide.listener.FragmentActionListener;
import com.example.martha.gameguide.util.Util;


/**
 * Created by Martha on 4/11/2016.
 */
public class PasswordRecoveryFragment extends Fragment {


    public static final String ACTION_PASS_RECOVERED = "action_pass_recover_complete";
    public static final String ACTION_CLICK_PASS_RECOVER_BACK = "action_click_pass_recover_back";

    private AuthActivity hostActivity;
    private FragmentActionListener actionListener;

    private EditText email;


    public PasswordRecoveryFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        hostActivity = (AuthActivity) context;
        actionListener = hostActivity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.password_recovery_view, container, false);
        email = (EditText)view.findViewById(R.id.password_rec_view_email);
        Button recoveryButton = (Button) view.findViewById(R.id.password_rec_view_recover_button);

        hostActivity.manageKeyPadActions(view, null, null, null);
        initRecButtonListener(recoveryButton);
        initToolbar(view);

        return view;
    }

    private void initRecButtonListener(Button recoveryButton) {
        recoveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Check if email field is empty highlight it
                final String emailText = email.getText().toString();
                if(Util.isEmailValid(emailText)){
                    actionListener.actionComplete(ACTION_PASS_RECOVERED);
                }

            }
        });
    }

    private void initToolbar(View rootView) {
        Toolbar toolbar = hostActivity.makeToolbar(rootView.findViewById(R.id.password_recovery_toolbar), getString(R.string.password_recovery_toolbar_title),
                R.drawable.back, null, null);
        ImageView left = (ImageView) toolbar.findViewById(R.id.toolbar_leftButton);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.actionComplete(ACTION_CLICK_PASS_RECOVER_BACK);
            }
        });
    }
}