package com.example.martha.gameguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.martha.gameguide.R;
import com.example.martha.gameguide.adapter.GameListAdapter;
import com.example.martha.gameguide.fragment.game.GameRecyclerListFragment;
import com.example.martha.gameguide.listener.FragmentActionListener;
import com.example.martha.gameguide.listener.RequestListener;
import com.example.martha.gameguide.maneger.GameManager;
import com.example.martha.gameguide.maneger.ServiceManager;
import com.example.martha.gameguide.maneger.UserManager;

public class HomeActivity extends BaseActivity implements FragmentActionListener{

    GameListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_logged);


//        GameExpandedInfoFragment gameExpandedInfoFragment = new GameExpandedInfoFragment();

        init();
        placeFragment(R.id.content_frame, new GameRecyclerListFragment(), false);
        if(!UserManager.instance().isLoggedIn()){
            startActivity(new Intent(HomeActivity.this, AuthActivity.class));
        }else {
            ServiceManager.instance().getUser(UserManager.instance().getCurrentUser().getToken(),
                    this, null);
        }


        GameManager.instance().loadCategoryList(this, new RequestListener() {
            @Override
            public void onComplete() {
                mItemTitles.clear();
                mItemTitles.addAll(GameManager.instance().getCategoryList());
                Toast.makeText(HomeActivity.this, "MAIN LOAD REQUEST COMPLETE!", Toast.LENGTH_SHORT);
                mDrawerList.setItemChecked(0, true);
                drawerItemClicked(0);
            }
        });

    }


    private void init() {
        adapter = new GameListAdapter(this);
        UserManager.instance().initUserModel(this);
        initDrawer();
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                drawerItemClicked(id);
            }
        });
    }
    private void drawerItemClicked(long id) {
        if (id != lastSelectedItemId) {
            lastSelectedItemId = id;
            String category = GameManager.instance().getCategoryList().get((int)id);
            adapter.setGameList(GameManager.instance().getGameIdMap().get(category), category);
        }
    }



    @Override
    public void actionComplete(String actionType) {
        switch (actionType){
            case GameRecyclerListFragment.TOOLBAR_RIGHT_BUTTON_CLICKED:
                if(UserManager.instance().isLoggedIn()){
                    startActivity(new Intent(this, ProfileActivity.class));
                } else {
                    startActivity(new Intent(this, AuthActivity.class));
                }
                break;
            case GameRecyclerListFragment.TOOLBAR_LEFT_BUTTON_CLICKED:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        GameManager.instance().resetParams();
    }

    public GameListAdapter getAdapter() {
        return adapter;
    }
}