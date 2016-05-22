package com.example.martha.gameguide.maneger;

import android.content.Context;
import android.widget.Toast;

import com.example.martha.gameguide.listener.RequestListener;
import com.example.martha.gameguide.model.GameModel;
import com.example.martha.gameguide.viewholder.GameCardViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

/**
 * Created by Martha on 4/20/2016.
 */
public class GameManager {

    private List<String> categoryList;
    private HashMap<String, List<Long>> gameIdMap;
    private volatile int queue = 0;
    private boolean isLoadRequestMade;
    private HashMap<GameCardViewHolder, Call> enquedGameCallMap;
    private Call tempCall;


    private static GameManager instance = new GameManager();

    public static GameManager instance() {
        return instance;
    }

    private GameManager() {
        init();
    }

    private void init(){
        categoryList = new ArrayList<>();
        gameIdMap = new HashMap<>();
        enquedGameCallMap = new HashMap<>();
    }

    public void loadCategoryList(final Context context, final RequestListener requestListener){
        if (isLoadRequestMade) throw new RuntimeException("loadCategoryList must be called only once for the application");
        isLoadRequestMade = true;
        ServiceManager.instance().getGameCategoryList(categoryList, context, new RequestListener() {
            @Override
            public void onComplete() {
                queue = categoryList.size();
                for (String category : categoryList) {
                    List<Long> sourceIdList = new ArrayList<>();
                    gameIdMap.put(category, sourceIdList);
                    ServiceManager.instance().getGameIdList(new GameModel(category), sourceIdList, context, new RequestListener() {
                        @Override
                        public void onComplete() {
                            --queue;
                            if (queue == 0) requestListener.onComplete();
                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(context, "Something went wrong while loading game list", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure() {
                Toast.makeText(context, "Something went wrong while loading category list", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadGameData(GameModel gameModel, final GameCardViewHolder viewHolder, Context context, final RequestListener requestListener) {
        if (enquedGameCallMap.get(viewHolder) != null) throw new RuntimeException("A view holder already has an unfinished and not canceled call!");
        tempCall = ServiceManager.instance().getAGame(gameModel, viewHolder, context, new RequestListener() {
            @Override
            public void onComplete() {
                if (requestListener != null) requestListener.onComplete();
                enquedGameCallMap.remove(viewHolder);
            }

            @Override
            public void onFailure() {
                if (requestListener != null) requestListener.onFailure();
            }
        });
        enquedGameCallMap.put(viewHolder, tempCall);
        System.out.println("********************CALL IS REGISTERED " + enquedGameCallMap.size() + "!********************");
    }
    public void removeRequestCall(GameCardViewHolder viewHolder) {
        Call call = enquedGameCallMap.get(viewHolder);
        if (call != null) {
            call.cancel();
            enquedGameCallMap.remove(viewHolder);
            System.out.println("********************CALL IS REMOVED!********************");
        }
    }
    public void replaceRequestCall(GameCardViewHolder viewHolder, Call newCall) {
        Call callToBeReplaced = enquedGameCallMap.get(viewHolder);
        if (callToBeReplaced != null) {
            callToBeReplaced.cancel();
            enquedGameCallMap.put(viewHolder, newCall);
            System.out.println("********************CALL IS REPLACED!********************");
            return;
        }
        System.out.println("********************ERROR, NO CALL TO REPLACE WITH!********************");
    }

    // Must be called on homeActivity onDestroy().
    public void resetParams() {
        isLoadRequestMade = false;
        categoryList.clear();
        gameIdMap.clear();
        enquedGameCallMap.clear();
        tempCall = null;
        queue = 0;
    }


    public List<String> getCategoryList() {
        return categoryList;
    }

    public HashMap<String, List<Long>> getGameIdMap() {
        return gameIdMap;
    }
}
