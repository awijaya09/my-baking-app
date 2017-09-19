package com.awijaya.mybakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.awijaya.mybakingapp.Model.Recipe;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by awijaya on 9/18/17.
 */

public class RecipeDetailActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;
    private Bundle mBundle;
    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Intent intent = getIntent();
        mBundle = intent.getBundleExtra(MainActivity.RECIPE_INTENT_KEY);
        mRecipe = mBundle.getParcelable(MainActivity.RECIPE_BUNDLE_KEY);
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setRecipeItem(mRecipe);

        mFragmentManager = getSupportFragmentManager();

        mFragmentManager.beginTransaction()
                .replace(R.id.recipe_detail_container, recipeDetailFragment)
                .commit();

    }



    private void getIntentData(){

    }

}
