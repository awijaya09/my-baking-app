package com.awijaya.mybakingapp;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.awijaya.mybakingapp.Model.Recipe;
import com.awijaya.mybakingapp.Model.Step;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by awijaya on 9/18/17.
 */

public class RecipeDetailListViewAdapter extends BaseAdapter {

    private ArrayList<Step> steps;
    private Recipe recipe;

    @Override
    public int getCount() {
        return steps.size()+2;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        return null;
    }
}
