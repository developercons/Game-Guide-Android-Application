package com.example.martha.gameguide.fragment;


import android.animation.AnimatorSet;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.martha.gameguide.R;
import com.example.martha.gameguide.activity.HomeActivity;
import com.example.martha.gameguide.animation.FlipHorizontal;
import com.example.martha.gameguide.listener.FragmentActionListener;

/**
 * Created by Martha on 5/22/2016.
 */
public class SplashFragment extends Fragment {

    private HomeActivity hostActivity;
    private FragmentActionListener actionListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        hostActivity = (HomeActivity)context;
        actionListener = (hostActivity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.splash, container, false);
        ImageView logo = (ImageView)view.findViewById(R.id.splash_logo);
        FlipHorizontal flipAnimation = new FlipHorizontal();
        AnimatorSet animatorSet = flipAnimation.getAnimation(logo, 5000, (5*360), 5, actionListener);
        animatorSet.start();

        return view;
    }
}
