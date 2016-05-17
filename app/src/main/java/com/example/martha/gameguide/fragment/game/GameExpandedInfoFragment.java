package com.example.martha.gameguide.fragment.game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.martha.gameguide.R;


/**
 * Created by Martha on 5/10/2016.
 */
public class GameExpandedInfoFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.game_expanded_view2, container, false);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Game Guide");

        ImageView gamePic = (ImageView) view.findViewById(R.id.game_pic);
        TextView category = (TextView) view.findViewById(R.id.category);
        TextView expertDefinition = (TextView) view.findViewById(R.id.expert_definition);
        ImageView star1 = (ImageView) view.findViewById(R.id.star1);
        ImageView star2 = (ImageView) view.findViewById(R.id.star2);
        ImageView star3 = (ImageView) view.findViewById(R.id.star3);
        ImageView star4 = (ImageView) view.findViewById(R.id.star4);
        ImageView star5 = (ImageView) view.findViewById(R.id.star5);
        TextView reviews = (TextView) view.findViewById(R.id.reviews_quantity);
        TextView ageRange = (TextView) view.findViewById(R.id.age_range);

        ListView listView = (ListView) view.findViewById(R.id.list_view);

        return view;
    }
}
