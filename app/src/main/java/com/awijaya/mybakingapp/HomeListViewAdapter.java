package com.awijaya.mybakingapp;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.awijaya.mybakingapp.Model.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.http.HEAD;

/**
 * Created by awijaya on 9/17/17.
 */

public class HomeListViewAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<Recipe> tempDataSource;

    public HomeListViewAdapter(Context context, ArrayList<Recipe> dataSource){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        tempDataSource = dataSource;
    }
    @Override
    public int getCount() {
        return tempDataSource.size();
    }

    @Override
    public Object getItem(int i) {
        return tempDataSource.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

            ViewHolder viewHolder;

            if (view != null){
                viewHolder = (ViewHolder) view.getTag();
            } else {
                view = inflater.inflate(R.layout.recipe_list_item, viewGroup, false);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            }

            viewHolder.mRecipeTitle.setText(tempDataSource.get(position).recipeName);
            viewHolder.mStepsCount.setText(tempDataSource.get(position).recipeSteps.size() + " Steps");

            return view;

    }

    static class ViewHolder {
        @BindView(R.id.card_view_recipe)
        CardView mCardView;

        @BindView(R.id.text_view_recipe_title)
        TextView mRecipeTitle;

        @BindView(R.id.text_view_steps_count)
        TextView mStepsCount;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }

}
