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

    public static final String ACTION_REGISTERED = "action_registered";
    public static final String ACTION_SIGN_UP_BACK = "action_sign_up_back";

    // region Instance fields
    private AuthActivity hostActivity;
    private FragmentActionListener actionListener;
    EditText firstName;
    EditText lastName;
    EditText password;
    EditText email;
    EditText repeatEmail;
    TextView error;
    SpannableStringBuilder builder;
    String simple = "Field is empty ";
    String colored = "*";

    // endregion

    // region Constructors
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
        Button signUpButton = (Button)view.findViewById(R.id.sign_up_button);
        firstName = (EditText) view.findViewById(R.id.sign_up_view_user_name);
        lastName = (EditText) view.findViewById(R.id.sign_up_view_surname);
        password = (EditText) view.findViewById(R.id.sign_up_password);
        email = (EditText) view.findViewById(R.id.sign_up_email);
        repeatEmail = (EditText) view.findViewById(R.id.sign_up_repeat_email);
        error = (TextView)view.findViewById(R.id.sign_toast);

        // endregion
        builder = new SpannableStringBuilder();
        builder.append(simple);
        int start = builder.length();
        builder.append(colored);
        int end = builder.length();
        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        initSingUpBtnListener(signUpButton);
        hostActivity.manageKeyPadActions(view, null, null, null);
        initToolbar(view);
        initListeners();

        return view;
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
    private void initToolbar(View rootView) {
        Toolbar toolbar = hostActivity.makeToolbar(rootView.findViewById(R.id.sign_up_toolbar), getString(R.string.sign_up_toolbar_title),
                R.drawable.back, null, null);
        ImageView left = (ImageView)toolbar.findViewById(R.id.toolbar_leftButton);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.actionComplete(ACTION_SIGN_UP_BACK);
            }
        });
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

            String firstNameText = firstName.getText().toString();
            String lastNameText = lastName.getText().toString();
            String passwordText = password.getText().toString();
            String emailText = email.getText().toString();
            String repeatEmailText = repeatEmail.getText().toString();


            if (firstNameText.isEmpty()) {
                firstName.setText(builder);
//                firstName.setHint(R.string.empty_field);
//                Util.markEmptyField(firstName, hostActivity.getAnimBlink());
                errorExist = false;
            }
            if (lastNameText.isEmpty()) {
                lastName.setText(builder);
//                lastName.setHint(R.string.empty_field);
//                Util.markEmptyField(lastName, hostActivity.getAnimBlink());
                errorExist = false;
            }
            if (passwordText.isEmpty()) {
                password.setHint(R.string.empty_field);
                Util.markEmptyField(password, hostActivity.getAnimBlink());
                errorExist = false;
            }
            if (emailText.isEmpty()) {
                email.setText(builder);
//                email.setHint(R.string.empty_field);
//                Util.markEmptyField(email, hostActivity.getAnimBlink());
                errorExist = false;
            }
            if (!repeatEmailText.equals(emailText)) {
                repeatEmail.setText(builder);
//                repeatEmail.setHint("Mail does not match");
                errorExist = false;
            }

            if(!errorExist){
                return false;
            }

            boolean errorCheck = true;
            if(!Util.isEmailValid(emailText) && !Util.isPasswordValid(passwordText)){
                error.setText(R.string.invalid_password_mail);
                error.setVisibility(View.VISIBLE);
//            Util.markInvalidField(email, hostActivity.getAnimBlink());
                errorCheck = false;
            }
            if(!Util.isEmailValid(emailText) && Util.isPasswordValid(passwordText)){
                error.setText(R.string.invalid_mail);
                error.setVisibility(View.VISIBLE);
//            Util.markInvalidField(email, hostActivity.getAnimBlink());
                errorCheck = false;
            }
            if(Util.isEmailValid(emailText) && !Util.isPasswordValid(passwordText)){
                error.setText(R.string.invalid_password);
                error.setVisibility(View.VISIBLE);
//            Util.markInvalidField(email, hostActivity.getAnimBlink());
                errorCheck = false;
            }

            if(!errorCheck){
                return false;
            }



            //TODO Make a request and write data on response. (put a prog bar)

            UserModel userModel = new UserModel();
            userModel.setFirst_name(firstNameText);
            userModel.setLast_name(lastNameText);
            userModel.setPassword(passwordText);
            userModel.setEmail(emailText);

            ServiceManager.instance().signUp(userModel, getContext(), new RequestListener() {
                @Override
                public void onComplete() {
                    actionListener.actionComplete(ACTION_REGISTERED);
                }
            });


        }
        return true;
    }
}