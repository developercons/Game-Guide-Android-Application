package com.example.martha.gameguide.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.martha.gameguide.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Martha on 4/7/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Animation animBlink;
    protected DrawerLayout mDrawerLayout;
    protected ListView mDrawerList;
    protected List<String> mItemTitles = new ArrayList<>();
    protected long lastSelectedItemId = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initBlinkAnimation();
    }

    protected void initDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item_example, mItemTitles));
    }


    private void initBlinkAnimation() {
        animBlink = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.blink);
        animBlink.setBackgroundColor(Color.RED);
    }

    protected void placeFragment(int containerViewId, Fragment fragment, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        String fragmentTag = fragment.getClass().getSimpleName();
        fragmentTransaction.replace(containerViewId, fragment, fragmentTag);
        if(addToBackStack){
            fragmentTransaction.addToBackStack(fragmentTag);
        }
        fragmentTransaction.commit();
    }
    protected void triggerBackButton() {
        getSupportFragmentManager().popBackStack();
    }


    public void clearFragmentBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0, size = fm.getBackStackEntryCount(); i < size; ++i) {
            fm.popBackStackImmediate();
        }
    }

    public void manageKeyPadActions(View rootView, final View removable1, final View removable2, final View removable3) {
        final View activityRootView = rootView;
        if (activityRootView != null) {
            activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if(removable1 != null) {
                        int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                        if (heightDiff > 80) {
                            removable1.setVisibility(View.GONE);
                        } else {
                            removable1.setVisibility(View.VISIBLE);
                        }
                    }
                    if(removable2 != null) {
                        int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                        if (heightDiff > 80) {
                            removable2.setVisibility(View.GONE);
                        } else {
                            removable2.setVisibility(View.VISIBLE);
                        }
                    }
                    if(removable3 != null) {
                        int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                        if (heightDiff > 80) {
                            removable3.setVisibility(View.GONE);
                        } else {
                            removable3.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
        }
    }

    public Toolbar makeToolbar(View toolbarView, @Nullable String text, @Nullable Integer leftButtonResId,
                               @Nullable Integer rightButtonResId, @Nullable Integer logoResId){
        Toolbar toolbar = (Toolbar) toolbarView;
        toolbar.dismissPopupMenus();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;

        TextView toolbarTitle = (TextView)toolbar.findViewById(R.id.toolbar_title);
        ImageView toolbarLogo = (ImageView)toolbar.findViewById(R.id.toolbar_logo);
        ImageView toolbarLeftButton = (ImageView)toolbar.findViewById(R.id.toolbar_leftButton);
        ImageView toolbarRightButton = (ImageView)toolbar.findViewById(R.id.toolbar_rightButton);
        if(text != null){
            toolbarTitle.setText(text);
            toolbarLogo.setVisibility(View.INVISIBLE);
        } else if (logoResId != null){
            toolbarTitle.setVisibility(View.INVISIBLE);
            toolbarLogo.setImageResource(logoResId);
        }
        if (leftButtonResId != null) toolbarLeftButton.setImageResource(leftButtonResId);
        else toolbarLeftButton.setVisibility(View.INVISIBLE);
        if (rightButtonResId != null) toolbarRightButton.setImageResource(rightButtonResId);
        else toolbarRightButton.setVisibility(View.INVISIBLE);
        return toolbar;
    }


    public Animation getAnimBlink() {
        return animBlink;
    }
}