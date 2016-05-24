package com.example.martha.gameguide.fragment.game;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.martha.gameguide.R;
import com.example.martha.gameguide.activity.HomeActivity;
import com.example.martha.gameguide.adapter.GameListAdapter;
import com.example.martha.gameguide.listener.FragmentActionListener;
import com.example.martha.gameguide.maneger.UserManager;

/**
 * Created by Martha on 4/25/2016.
 */
public class GameRecyclerListFragment extends Fragment{

    // region Static fields
    public static final String TOOLBAR_RIGHT_BUTTON_CLICKED = "toolbar_right_button_clicked";
    public static final String TOOLBAR_LEFT_BUTTON_CLICKED = "toolbar_left_button_clicked";
    // endregion

    // region Instance fields
    private HomeActivity hostActivity;
    private FragmentActionListener fragmentActionListener;
    private Toolbar toolBar;
    private GameListAdapter adapter;
    private TextView toolbarTitle;
    // endregion

    // region ctor
    public GameRecyclerListFragment() {
    }
    // endregion

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        hostActivity = (HomeActivity)context;
        fragmentActionListener = hostActivity;
        adapter = hostActivity.getAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // region View
        View view = inflater.inflate(R.layout.recycle_view, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        // endregion

        // region Attributes
        toolBar = initToolbar(view);
        updateToolbar();
        // endregion

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (toolBar != null) updateToolbar();
    }

    private Toolbar initToolbar(View rootView) {
        Toolbar toolbar = hostActivity.makeToolbar(rootView.findViewById(R.id.recycler_toolbar), getString(R.string.recycler_toolbar_title),
                R.drawable.open_drawer, null, null);
        ImageView right = (ImageView)toolbar.findViewById(R.id.toolbar_rightButton);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentActionListener.actionComplete(TOOLBAR_RIGHT_BUTTON_CLICKED);
            }
        });
        ImageView left = (ImageView)toolbar.findViewById(R.id.toolbar_leftButton);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentActionListener.actionComplete(TOOLBAR_LEFT_BUTTON_CLICKED);
            }
        });
        toolbar.setBackgroundResource(R.color.white);
        toolbarTitle = (TextView)toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setTextColor(ContextCompat.getColor(toolbarTitle.getContext(), R.color.toolbar_title_color));
        return toolbar;
    }

    private void updateToolbar() {
        ImageView rightButton = (ImageView)toolBar.findViewById(R.id.toolbar_rightButton);
        int rightIcon;
        if(UserManager.instance().isLoggedIn()) rightIcon = R.drawable.loggedin;
        else rightIcon = R.drawable.login;
        rightButton.setImageResource(rightIcon);
        rightButton.setVisibility(View.VISIBLE);
    }

    public void refreshToolbarTitle(String text){
        toolbarTitle.setText(text);
    }
}
