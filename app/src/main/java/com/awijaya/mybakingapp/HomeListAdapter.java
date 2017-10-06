package com.awijaya.mybakingapp;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.awijaya.mybakingapp.Model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by awijaya on 10/5/17.
 */

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.HomeListViewHolder> {

    private ArrayList<Recipe> tempDataSource;
    private Context mContext;

    final private HomeListAdapterOnClickHandler mOnClickHandler;

    interface HomeListAdapterOnClickHandler {
        void onItemClick(Recipe item);
    }
    public HomeListAdapter(Context context, ArrayList<Recipe> dataSource, HomeListAdapterOnClickHandler handler){
        mOnClickHandler = handler;
        this.tempDataSource = dataSource;
        this.mContext = context;
    }

    class HomeListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.image_view_recipe)
        ImageView mImageView;

        @BindView(R.id.text_view_recipe_title)
        TextView mRecipeTitle;

        @BindView(R.id.text_view_steps_count)
        TextView mStepsCount;

        @BindView(R.id.text_view_estimated_time)
        TextView mEstimatedTime;

        @BindView(R.id.btn_add_favourite)
        Button mFaveButton;

        public HomeListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            mOnClickHandler.onItemClick(tempDataSource.get(getAdapterPosition()));
        }
    }

    @Override
    public HomeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForMovieItem = R.layout.recipe_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        Boolean shouldAttachToParent = false;

        View view = inflater.inflate(layoutIdForMovieItem, parent, shouldAttachToParent);
        return new HomeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeListViewHolder viewHolder, int position) {
        final Recipe mRecipe = tempDataSource.get(position);
        int stepCounter = mRecipe.recipeSteps.size();
        viewHolder.mRecipeTitle.setText(tempDataSource.get(position).recipeName);
        viewHolder.mStepsCount.setText(stepCounter+ " Steps");
        viewHolder.mEstimatedTime.setText(stepCounter*5 + " Minutes");
        if (mRecipe.recipeImage != null && !mRecipe.equals("")) {
            viewHolder.mImageView.setVisibility(View.VISIBLE);
            Picasso.with(mContext)
                    .load(Uri.parse(mRecipe.recipeImage))
                    .placeholder(R.drawable.exoplayer_artwork)
                    .into(viewHolder.mImageView);
        }
        viewHolder.mFaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return tempDataSource.size();
    }
}
