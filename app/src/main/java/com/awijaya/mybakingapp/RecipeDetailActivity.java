package com.awijaya.mybakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.awijaya.mybakingapp.Model.Ingredient;
import com.awijaya.mybakingapp.Model.Recipe;
import com.awijaya.mybakingapp.Model.Step;
import com.awijaya.mybakingapp.Networking.SharedNetworking;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by awijaya on 9/18/17.
 */

public class RecipeDetailActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;
    private Bundle mBundle;
    private Recipe mRecipe;
    private ArrayList<Ingredient> mIngredients = new ArrayList<Ingredient>();
    private ArrayList<Step> mSteps;
    private ArrayList<String> mIngredientString = new ArrayList<String>();
    private static final String ING_KEY = "ingKey";

    private static final String TAG = "Recipe Tracking";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);


        if (savedInstanceState == null){
            Intent intent = getIntent();
            mBundle = intent.getBundleExtra(MainActivity.RECIPE_INTENT_KEY);
            mRecipe = mBundle.getParcelable(MainActivity.RECIPE_BUNDLE_KEY);

            mIngredients = mBundle.getParcelableArrayList(MainActivity.INGREDIENTS_BUNDLE_KEY);
            mSteps = mBundle.getParcelableArrayList(MainActivity.STEPS_BUNDLE_KEY);

            mRecipe.recipeIngredients = mIngredients;
            mRecipe.recipeSteps = mSteps;

            mIngredientString = SharedNetworking.addIngredientStrings(mIngredients);

            setTitle(mRecipe.recipeName);

            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setRecipeItem(mRecipe);

            mFragmentManager = getSupportFragmentManager();

            mFragmentManager.beginTransaction()
                    .replace(R.id.recipe_detail_fragment, recipeDetailFragment)
                    .commit();
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ingredient, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ingredient_list){
            new MaterialDialog.Builder(this)
                    .title(R.string.ingredient_dialog_title)
                    .titleColorRes(R.color.colorPrimary)
                    .dividerColorRes(R.color.colorAccent)
                    .items(mIngredientString)
                    .show();
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(ING_KEY, mIngredientString);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mIngredientString = savedInstanceState.getStringArrayList(ING_KEY);
    }
}
