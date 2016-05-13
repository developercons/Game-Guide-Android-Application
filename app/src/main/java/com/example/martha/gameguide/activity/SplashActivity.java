package com.example.martha.gameguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.martha.gameguide.R;
import com.example.martha.gameguide.maneger.UserManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Martha on 4/7/2016.
 */
public class SplashActivity extends BaseActivity {

    Animation animFadeIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        init();
        startSplashAnimation();
        startTerminationCountDown();
    }

    private void init(){
        UserManager.instance().setUdid(((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId());
    }

    private void startSplashAnimation() {
        ImageView splashImage = (ImageView)findViewById(R.id.splash_image);
        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        assert splashImage != null;
        splashImage.setAnimation(animFadeIn);
    }

    private void startTerminationCountDown(){
        Timer timer = new Timer();
        long animationDuration = 2000;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent homeActivityIntent = new Intent(SplashActivity.this, HomeActivity.class);
                homeActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                homeActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                homeActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(homeActivityIntent);
            }
        }, animationDuration);
    }
}