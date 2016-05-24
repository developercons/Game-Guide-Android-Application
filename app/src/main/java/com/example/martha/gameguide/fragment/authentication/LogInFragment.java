package com.example.martha.gameguide.fragment.authentication;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.martha.gameguide.R;
import com.example.martha.gameguide.activity.AuthActivity;
import com.example.martha.gameguide.listener.FragmentActionListener;
import com.example.martha.gameguide.listener.RequestListener;
import com.example.martha.gameguide.maneger.ServiceManager;
import com.example.martha.gameguide.model.UserModel;
import com.example.martha.gameguide.util.Util;


/**
 * Created by Martha on 4/13/2016.
 */
public class LogInFragment extends Fragment {

    // Static fileds
    public static final String ACTION_LOG_IN_COMPLETE = "action_log_in_complete";
    public static final String ACTION_CLICK_SIGN_UP = "action_click_sign_up";
    public static final String ACTION_CLICK_PASS_RECOVERY = "action_click_pass_recovery";
    public static final String ACTION_CLICK_CONTINUE_WITHOUT_LOG_IN = "action_click_continue_without_log_in";
    // endregion

    // region Instance fields
    private AuthActivity hostActivity;
    private FragmentActionListener actionListener;
    private EditText email;
    private EditText password;
    private TextView forgotPassword;
    private Button logIn;
    private TextView signUp;
    private LinearLayout continueWithoutLoginBtn;
    private TextView error;
    private ProgressBar progressBar;
    // endregion

    // region ctor
    public LogInFragment() {
    }
    // endregion

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        hostActivity = (AuthActivity) context;
        actionListener = hostActivity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // region Views
        View view = inflater.inflate(R.layout.login_view, container, false);
        forgotPassword = (TextView)view.findViewById(R.id.login_view_forgot);
        logIn = (Button)view.findViewById(R.id.login_button);
        signUp = (TextView)view.findViewById(R.id.login_view_sign_up_suggestion);
        continueWithoutLoginBtn = (LinearLayout)view.findViewById(R.id.login_view_facebook_button);
        email = (EditText)view.findViewById(R.id.login_view_email_input);
        password = (EditText) view.findViewById(R.id.login_view_password);
        error = (TextView)view.findViewById(R.id.login_error);
        progressBar = (ProgressBar)view.findViewById(R.id.login_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
        // endregion

        // region Attributes
        initListeners();
        hostActivity.manageKeyPadActions(view, continueWithoutLoginBtn, null, null);
        // endregion

        return view;
    }

    private void initListeners() {
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.actionComplete(ACTION_CLICK_PASS_RECOVERY);
            }
        });
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ServiceManager.isConnectedToInternet(hostActivity)) {
                    Toast.makeText(getActivity(), "Please connect to Internet to login", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isInputDataValid()) {
                    enableAllButtons(false);
                    UserModel loginModel = new UserModel();
                    loginModel.setEmail(email.getText().toString());
                    loginModel.setPassword(password.getText().toString());
                    ServiceManager.instance().login(loginModel, getContext(), new RequestListener() {
                        @Override
                            public void onComplete() {
                            enableAllButtons(true);
                            actionListener.actionComplete(ACTION_LOG_IN_COMPLETE);
                        }
                        @Override
                        public void onFailure() {
                            enableAllButtons(true);
                        }
                    });
                }
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.actionComplete(ACTION_CLICK_SIGN_UP);
            }
        });
        continueWithoutLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Not logged in", Toast.LENGTH_SHORT).show();
                actionListener.actionComplete(ACTION_CLICK_CONTINUE_WITHOUT_LOG_IN);
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                error.setVisibility(View.INVISIBLE);
                email.setTextColor(ContextCompat.getColor(getContext(), R.color.auth_view_texts_color));
                if (email.getAnimation() != null) email.clearAnimation();
                email.setHint(R.string.hint_email);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error.setVisibility(View.INVISIBLE);
                email.setHint(R.string.hint_email);
            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                error.setVisibility(View.INVISIBLE);
                password.setTextColor(ContextCompat.getColor(getContext(), R.color.auth_view_texts_color));
                if (password.getAnimation() != null) password.clearAnimation();
            }
        });
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error.setVisibility(View.INVISIBLE);
                password.setHint(R.string.hint_password);
            }
        });
    }

    public void enableAllButtons(boolean index){
            logIn.setEnabled(index);
            signUp.setEnabled(index);
            forgotPassword.setEnabled(index);
            email.setEnabled(index);
            password.setEnabled(index);
            continueWithoutLoginBtn.setEnabled(index);
        if(!index){
            progressBar = new ProgressBar(getContext());
            progressBar.setVisibility(View.VISIBLE);
        } else progressBar.setVisibility(View.INVISIBLE);
    }

    public boolean isInputDataValid() {
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();

        if (emailText.isEmpty()) {
            email.setHint(R.string.login_empty_mail_hint_text);
            Util.markEmptyField(email, hostActivity.getAnimBlink());
            if (passwordText.isEmpty()) {
                password.setHint(R.string.login_empty_password_hint_text);
            Util.markEmptyField(password, hostActivity.getAnimBlink());
                return false;
            }
            return false;
        }

        if(!Util.isEmailValid(emailText) && !Util.isPasswordValid(passwordText)){

            error.setText(R.string.invalid_password_mail);
            error.setVisibility(View.VISIBLE);
            return false;
        }
        if(!Util.isEmailValid(emailText) && Util.isPasswordValid(passwordText)){
            error.setText(R.string.invalid_mail);
            error.setVisibility(View.VISIBLE);
            return false;
        }
        if(Util.isEmailValid(emailText) && !Util.isPasswordValid(passwordText)){
            error.setText(R.string.invalid_password);
            error.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }
}