package com.example.martha.gameguide.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.martha.gameguide.R;
import com.example.martha.gameguide.listener.RequestListener;
import com.example.martha.gameguide.maneger.GameManager;
import com.example.martha.gameguide.model.GameModel;
import com.example.martha.gameguide.viewholder.GameCardViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martha on 4/25/2016.
 */
public class GameListAdapter extends RecyclerView.Adapter<GameCardViewHolder>{
    private Context context;
    private List<GameModel> gameList;

    public GameListAdapter(Context context) {
        this.context = context;
        gameList = new ArrayList<>();
    }

    public void setGameList(List<Long> gameIdList, String category) {
        this.gameList.clear();
        for (Long id : gameIdList) {
            gameList.add(new GameModel(category, id));
        }
        notifyDataSetChanged();
    }


    @Override
    public GameCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new GameCardViewHolder(cardView);
    }

    @Override
    public void onViewRecycled(GameCardViewHolder holder) {
        holder.reset();
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(GameCardViewHolder cardViewHolder, int position) {
        final GameCardViewHolder viewHolder = cardViewHolder;
        final int pos = cardViewHolder.getAdapterPosition();
        GameManager.instance().loadGameData(gameList.get(position), cardViewHolder, context, new RequestListener() {
            @Override
            public void onComplete() {
                viewHolder.initialise(gameList.get(pos));
            }
        });
    }
    @Override
    public int getItemCount() {
        if(gameList != null){
        return gameList.size();}
        return 0;
    }
}
