package com.example.martha.gameguide.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;

import com.example.martha.gameguide.listener.FragmentActionListener;

/**
 * Created by Martha on 5/22/2016.
 */
public class FlipHorizontal extends Animation{
    public static final int PIVOT_CENTER = 0, PIVOT_LEFT = 1, PIVOT_RIGHT = 2;
    public static final String ACTION_FLIP_ANIMATION_COMPLETE = "action_flip_animation_complete";


    public AnimatorSet getAnimation(View rootView, long duration, float degrees, int pivot, final FragmentActionListener listener){
        float pivotX, pivotY, viewWidth = rootView.getWidth(), viewHeight = rootView.getHeight();
        switch (pivot) {
            case PIVOT_LEFT:
                pivotX = 0f;
                pivotY = viewHeight / 2;
                break;
            case PIVOT_RIGHT:
                pivotX = viewWidth;
                pivotY = viewHeight / 2;
                break;
            case PIVOT_CENTER:
                pivotX = viewWidth / 2;
                pivotY = viewHeight / 2;
                break;
            default:
                pivotX = viewWidth / 2;
                pivotY = viewHeight / 2;
                break;
        }
        rootView.setPivotX(pivotX);
        rootView.setPivotY(pivotY);
        AnimatorSet flipSet = new AnimatorSet();
        flipSet.play(ObjectAnimator.ofFloat(rootView, View.ROTATION_Y, rootView.getRotationY() + degrees));
        flipSet.setInterpolator(new AccelerateDecelerateInterpolator());
        flipSet.setDuration(duration);
        flipSet.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                if (listener != null) {
                    listener.actionComplete(ACTION_FLIP_ANIMATION_COMPLETE);
                }
            }
        });
        return flipSet;
    }

}
