package com.example.martha.gameguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.martha.gameguide.R;
import com.example.martha.gameguide.adapter.GameListAdapter;
import com.example.martha.gameguide.animation.FlipHorizontal;
import com.example.martha.gameguide.fragment.SplashFragment;
import com.example.martha.gameguide.fragment.game.GameRecyclerListFragment;
import com.example.martha.gameguide.listener.FragmentActionListener;
import com.example.martha.gameguide.listener.RequestListener;
import com.example.martha.gameguide.maneger.GameManager;
import com.example.martha.gameguide.maneger.ServiceManager;
import com.example.martha.gameguide.maneger.UserManager;
import com.example.martha.gameguide.model.NavDrawerItemModel;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements FragmentActionListener{

    //region Instance Fields
    private GameListAdapter adapter;
    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_logged);

//        GameExpandedInfoFragment gameExpandedInfoFragment = new GameExpandedInfoFragment();

        placeFragment(R.id.content_frame, new SplashFragment(), false);
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

    private void checkIfLogged(){
        if(!UserManager.instance().isLoggedIn()){
            startActivity(new Intent(HomeActivity.this, AuthActivity.class));
        }else {
            ServiceManager.instance().getUser(UserManager.instance().getCurrentUser().getToken(),
                    this, null);
        }
    }

    private void loadGames(){
        GameManager.instance().loadCategoryList(this, new RequestListener() {
            @Override
            public void onComplete() {
                List<NavDrawerItemModel> currentList = new ArrayList<>();
                List<String> titlesList = GameManager.instance().getCategoryList();
                for (int i = 0; i < titlesList.size(); i++) {
                    currentList.add(new NavDrawerItemModel(R.mipmap.logo_botton, titlesList.get(i)));
                }
                drawerAdapter.setDrawerItems(currentList);
                mDrawerList.setAdapter(drawerAdapter);
                Toast.makeText(HomeActivity.this, "MAIN LOAD REQUEST COMPLETE!", Toast.LENGTH_SHORT).show();
                mDrawerList.setItemChecked(0, true);
                drawerItemClicked(0);
            }
            @Override
            public void onFailure() {
                Toast.makeText(HomeActivity.this, "MAIN LOAD REQUEST FAILED!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void drawerItemClicked(long id) {
        if (id != lastSelectedItemId) {
            lastSelectedItemId = id;
            String category = GameManager.instance().getCategoryList().get((int)id);
            adapter.setGameList(GameManager.instance().getGameIdMap().get(category), category);
            mDrawerLayout.closeDrawers();
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
            case FlipHorizontal.ACTION_FLIP_ANIMATION_COMPLETE:
                init();
                checkIfLogged();
                loadGames();
                placeFragment(R.id.content_frame, new GameRecyclerListFragment(), false);
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