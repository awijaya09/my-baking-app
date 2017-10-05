package com.awijaya.mybakingapp;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.awijaya.mybakingapp.Model.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by awijaya on 10/5/17.
 */

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.HomeListViewHolder> {

    private ArrayList<Recipe> tempDataSource;

    final private HomeListAdapterOnClickHandler mOnClickHandler;

    interface HomeListAdapterOnClickHandler {
        void onItemClick(Recipe item);
    }
    public HomeListAdapter(ArrayList<Recipe> dataSource, HomeListAdapterOnClickHandler handler){
        mOnClickHandler = handler;
        this.tempDataSource = dataSource;
    }

    class HomeListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.text_view_recipe_title)
        TextView mRecipeTitle;

        @BindView(R.id.text_view_steps_count)
        TextView mStepsCount;

        @BindView(R.id.text_view_estimated_time)
        TextView mEstimatedTime;

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
        int stepCounter = tempDataSource.get(position).recipeSteps.size();
        viewHolder.mRecipeTitle.setText(tempDataSource.get(position).recipeName);
        viewHolder.mStepsCount.setText(stepCounter+ " Steps");
        viewHolder.mEstimatedTime.setText(stepCounter*5 + " Minutes");
    }

    @Override
    public int getItemCount() {
        return tempDataSource.size();
    }
}
