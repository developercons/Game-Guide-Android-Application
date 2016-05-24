package com.example.martha.gameguide.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.example.martha.gameguide.R;
import com.example.martha.gameguide.fragment.authentication.LogInFragment;
import com.example.martha.gameguide.fragment.authentication.PasswordRecoveryFragment;
import com.example.martha.gameguide.fragment.authentication.SignUpFragment;
import com.example.martha.gameguide.listener.FragmentActionListener;


/**
 * Created by Martha on 4/7/2016.
 */
public class AuthActivity extends BaseActivity implements FragmentActionListener {

    // region Instance fields
    FragmentManager fragmentManager;
    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_unlogged);

        placeFragment(R.id.home_unlogged, new LogInFragment(), false);
        fragmentManager = getSupportFragmentManager();
    }

    @Override
    public void actionComplete(String actionType) {
        switch (actionType) {
            case LogInFragment.ACTION_CLICK_SIGN_UP:
                Toast.makeText(this, "Sign Up button pressed", Toast.LENGTH_SHORT).show();
                placeFragment(R.id.home_unlogged, new SignUpFragment(), true);
                break;
            case LogInFragment.ACTION_LOG_IN_COMPLETE:
                finish();
                break;
            case LogInFragment.ACTION_CLICK_PASS_RECOVERY:
                Toast.makeText(this, "Password recovery button pressed", Toast.LENGTH_SHORT).show();
                placeFragment(R.id.home_unlogged, new PasswordRecoveryFragment(), true);
                break;
            case LogInFragment.ACTION_CLICK_CONTINUE_WITHOUT_LOG_IN:
                finish();
                break;
            case PasswordRecoveryFragment.ACTION_PASS_RECOVERED:
                Toast.makeText(this, "Password successfully recovered", Toast.LENGTH_SHORT).show();
                placeFragment(R.id.home_unlogged, new LogInFragment(), true);
                break;
            case PasswordRecoveryFragment.ACTION_CLICK_PASS_RECOVER_BACK:
                Toast.makeText(this, "Navigated Back", Toast.LENGTH_SHORT).show();
                triggerBackButton();
                break;
            case SignUpFragment.ACTION_SIGN_UP_BACK:
                Toast.makeText(this, "Navigated Back", Toast.LENGTH_SHORT).show();
                triggerBackButton();
                break;
            case SignUpFragment.ACTION_REGISTERED:
                finish();
                break;
        }
    }
}

