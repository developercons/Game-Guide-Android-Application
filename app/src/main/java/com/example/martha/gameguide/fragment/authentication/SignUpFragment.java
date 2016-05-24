package com.example.martha.gameguide.fragment.authentication;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
public class SignUpFragment extends Fragment {

    // region Static fileds
    public static final String ACTION_REGISTERED = "action_registered";
    public static final String ACTION_SIGN_UP_BACK = "action_sign_up_back";
    // endregion

    // region Instance fields
    private AuthActivity hostActivity;
    private FragmentActionListener actionListener;
    private EditText firstName;
    private EditText lastName;
    private EditText password;
    private EditText email;
    private EditText repeatEmail;
    private Button signUpButton;
    private TextView error;
    private ImageView toolbarBackButton;
    private SpannableStringBuilder builder;
    private ProgressBar progressBar;
    private String simple = "Field is empty ";
    private String colored = "*";
    // endregion

    // region ctor
    public SignUpFragment() {
    }
    // endregion

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        hostActivity = (AuthActivity)context;
        actionListener = (hostActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // region View
        View view = inflater.inflate(R.layout.sign_up_view, container, false);
        signUpButton = (Button)view.findViewById(R.id.sign_up_button);
        firstName = (EditText) view.findViewById(R.id.sign_up_view_user_name);
        lastName = (EditText) view.findViewById(R.id.sign_up_view_surname);
        password = (EditText) view.findViewById(R.id.sign_up_password);
        email = (EditText) view.findViewById(R.id.sign_up_email);
        repeatEmail = (EditText) view.findViewById(R.id.sign_up_repeat_email);
        error = (TextView)view.findViewById(R.id.sign_toast);
        progressBar = (ProgressBar)view.findViewById(R.id.sign_up_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
        // endregion

        // region Attributes
        initSingUpBtnListener(signUpButton);
        buildSpan();
        hostActivity.manageKeyPadActions(view, null, null, null);
        toolbarBackButton = initToolbar(view);
        initListeners();
        // endregion

        return view;
    }

    private void buildSpan(){
        builder = new SpannableStringBuilder();
        builder.append(simple);
        int start = builder.length();
        builder.append(colored);
        int end = builder.length();
        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void initSingUpBtnListener(Button signUpButton) {
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ServiceManager.isConnectedToInternet(getContext())) {
                    Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT);
                    return;
                }
                handleSignUpRequest();
            }
        });
    }

    private ImageView initToolbar(View rootView) {
        Toolbar toolbar = hostActivity.makeToolbar(rootView.findViewById(R.id.sign_up_toolbar), getString(R.string.sign_up_toolbar_title),
                R.drawable.back, null, null);
        ImageView left = (ImageView)toolbar.findViewById(R.id.toolbar_leftButton);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.actionComplete(ACTION_SIGN_UP_BACK);
            }
        });
        return left;
    }

    private void initListeners(){
        firstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                error.setVisibility(View.INVISIBLE);
                firstName.setTextColor(ContextCompat.getColor(getContext(), R.color.auth_view_texts_color));
                if (firstName.getAnimation() != null) firstName.clearAnimation();
                firstName.setHint(R.string.hint_name);
            }
        });
        firstName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error.setVisibility(View.INVISIBLE);
                firstName.setHint(R.string.hint_name);
            }
        });
        lastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                builder.clearSpans();
                error.setVisibility(View.INVISIBLE);
                lastName.setTextColor(ContextCompat.getColor(getContext(), R.color.auth_view_texts_color));
                if (lastName.getAnimation() != null) lastName.clearAnimation();
                lastName.setHint(R.string.hint_surname);
            }
        });
        lastName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.removeSpan(lastName);
                error.setVisibility(View.INVISIBLE);
                lastName.setHint(R.string.hint_surname);
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                builder.clear();
                error.setVisibility(View.INVISIBLE);
                email.setTextColor(ContextCompat.getColor(getContext(), R.color.auth_view_texts_color));
                if (email.getAnimation() != null) email.clearAnimation();
                email.setHint(R.string.hint_email);
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.clear();
                error.setVisibility(View.INVISIBLE);
                email.setHint(R.string.hint_email);
            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                builder.clear();
                error.setVisibility(View.INVISIBLE);
                password.setTextColor(ContextCompat.getColor(getContext(), R.color.auth_view_texts_color));
                if (password.getAnimation() != null) password.clearAnimation();
                password.setHint(R.string.hint_password);
            }
        });
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.clear();
                error.setVisibility(View.INVISIBLE);
                password.setHint(R.string.hint_password);
            }
        });
    }

    public boolean handleSignUpRequest() {
        View tempView = getView();
        boolean errorExist = true;
        if (tempView != null) {

            // region Input fields check
            String firstNameText = firstName.getText().toString();
            String lastNameText = lastName.getText().toString();
            String passwordText = password.getText().toString();
            String emailText = email.getText().toString();
            String repeatEmailText = repeatEmail.getText().toString();

            if (firstNameText.isEmpty()) {
                firstName.setText(builder);
                errorExist = false;
            }
            if (lastNameText.isEmpty()) {
                lastName.setText(builder);
                errorExist = false;
            }
            if (passwordText.isEmpty()) {
                password.setHint(R.string.empty_field);
                Util.markEmptyField(password, hostActivity.getAnimBlink());
                errorExist = false;
            }
            if (emailText.isEmpty()) {
                email.setText(builder);
                errorExist = false;
            }
            if (!repeatEmailText.equals(emailText)) {
                repeatEmail.setText(builder);
                errorExist = false;
            }

            if(!errorExist){
                return false;
            }

            boolean errorCheck = true;
            if(!Util.isEmailValid(emailText) && !Util.isPasswordValid(passwordText)){
                error.setText(R.string.invalid_password_mail);
                error.setVisibility(View.VISIBLE);
                errorCheck = false;
            }
            if(!Util.isEmailValid(emailText) && Util.isPasswordValid(passwordText)){
                error.setText(R.string.invalid_mail);
                error.setVisibility(View.VISIBLE);
                errorCheck = false;
            }
            if(Util.isEmailValid(emailText) && !Util.isPasswordValid(passwordText)){
                error.setText(R.string.invalid_password);
                error.setVisibility(View.VISIBLE);
                errorCheck = false;
            }

            if(!errorCheck){
                return false;
            }
            // endregion

            // region Build Request
            UserModel userModel = new UserModel();
            userModel.setFirst_name(firstNameText);
            userModel.setLast_name(lastNameText);
            userModel.setPassword(passwordText);
            userModel.setEmail(emailText);
            if (!ServiceManager.isConnectedToInternet(hostActivity)) {
                Toast.makeText(getActivity(), "Please connect to Internet to login", Toast.LENGTH_SHORT).show();
                return false;
            }
            enableAllButtons(false);
            ServiceManager.instance().signUp(userModel, getContext(), new RequestListener() {
                @Override
                public void onComplete() {
                    enableAllButtons(true);
                    actionListener.actionComplete(ACTION_REGISTERED);
                }

                @Override
                public void onFailure() {
                    enableAllButtons(true);
                }
            });
            // endregion

        }
        return true;
    }

    public void enableAllButtons(boolean index){
            firstName.setEnabled(index);
            lastName.setEnabled(index);
            email.setEnabled(index);
            password.setEnabled(index);
            signUpButton.setEnabled(index);
            toolbarBackButton.setEnabled(index);
        if(!index){
            progressBar = new ProgressBar(getContext());
            progressBar.setVisibility(View.VISIBLE);
        } else progressBar.setVisibility(View.INVISIBLE);
    }
}