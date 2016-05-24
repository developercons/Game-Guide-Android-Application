package com.example.martha.gameguide.viewholder;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.martha.gameguide.R;
import com.example.martha.gameguide.listener.DecodeBitmapListener;
import com.example.martha.gameguide.maneger.GameManager;
import com.example.martha.gameguide.model.GameModel;
import com.example.martha.gameguide.util.Util;

import java.util.ArrayList;

/**
 * Created by Martha on 4/25/2016.
 */
public class GameCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private ImageView gamePic;
    private TextView gameName;
    private TextView category;
    private TextView expertDefinition;
    private ArrayList<ImageView> stars;
    private TextView reviews;
    private TextView ageRange;
    private ProgressBar progressBar;

    public GameCardViewHolder(View itemView) {
        super(itemView);
        stars = new ArrayList<>();
        gamePic = (ImageView)itemView.findViewById(R.id.game_pic);
        gameName = (TextView)itemView.findViewById(R.id.game_name);
        category = (TextView)itemView.findViewById(R.id.category);
        expertDefinition = (TextView)itemView.findViewById(R.id.expert_definition);
        stars.add((ImageView) itemView.findViewById(R.id.star1));
        stars.add((ImageView) itemView.findViewById(R.id.star2));
        stars.add((ImageView) itemView.findViewById(R.id.star3));
        stars.add((ImageView) itemView.findViewById(R.id.star4));
        stars.add((ImageView) itemView.findViewById(R.id.star5));
        reviews = (TextView)itemView.findViewById(R.id.reviews_quantity);
        ageRange = (TextView)itemView.findViewById(R.id.age_range);
        progressBar = (ProgressBar)itemView.findViewById(R.id.card_progress_bar);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
    }

    public void initialise(GameModel gameModel) {
        gameName.setText(gameModel.getGameName());
        category.setText(gameModel.getCategory());
        expertDefinition.setText(gameModel.getExpertDefinition());
        reviews.setText(Util.random.nextInt(12) + " reviews"); // TODO change random to actual review count.
        ageRange.setText(gameModel.getAgeRange());
        for (int i = 0, size = stars.size(); i < size; i++) {
            ImageView currentStar = stars.get(i);
            if (i < gameModel.getExpertMark()) currentStar.setImageResource(R.mipmap.starfull);
            else currentStar.setImageResource(R.mipmap.star);
        }
        Util.decodeByteArrayForSizeAsync(gameModel.getGamePicture(), this.gamePic.getWidth(), this.gamePic.getHeight(), new DecodeBitmapListener() {
            @Override
            public void onComplete(Bitmap bitmap) {
                gamePic.setImageBitmap(bitmap);
                progressBar.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onFail() {
                System.out.println("FAILED TO DECODE IMAGE ON ASYNC TASK!");
            }
        });
    }


    public void reset() {
        GameManager.instance().removeRequestCall(this);
        gameName.setText("");
        category.setText("");
        expertDefinition.setText("");
        reviews.setText("");
        ageRange.setText("");
        for (ImageView v : stars) {
            v.setImageBitmap(null);
        }
        gamePic.setImageBitmap(null);
        progressBar.setVisibility(View.VISIBLE);
    }



    public ImageView getGamePic() {
        return gamePic;
    }

    public void setGamePic(ImageView gamePic) {
        this.gamePic = gamePic;
    }

    public TextView getGameName() {
        return gameName;
    }

    public void setGameName(TextView gameName) {
        this.gameName = gameName;
    }

    public TextView getCategory() {
        return category;
    }

    public void setCategory(TextView category) {
        this.category = category;
    }

    public TextView getExpertDefinition() {
        return expertDefinition;
    }

    public void setExpertDefinition(TextView expertDefinition) {
        this.expertDefinition = expertDefinition;
    }

    public TextView getReviews() {
        return reviews;
    }

    public void setReviews(TextView reviews) {
        this.reviews = reviews;
    }

    public TextView getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(TextView ageRange) {
        this.ageRange = ageRange;
    }
}
