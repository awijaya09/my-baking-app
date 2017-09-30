package com.awijaya.mybakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.awijaya.mybakingapp.Model.Ingredient;
import com.awijaya.mybakingapp.Model.Recipe;
import com.awijaya.mybakingapp.Model.Step;
import com.awijaya.mybakingapp.Networking.SharedNetworking;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecipeListFragment.OnItemClickListener {

    public static final String RECIPE_BUNDLE_KEY = "recipe_item";
    public static final String RECIPE_INTENT_KEY = "recipe_key";
    public static final String INGREDIENTS_BUNDLE_KEY = "ingredient_item";
    public static final String STEPS_BUNDLE_KEY = "step_item";
    private static final String TAG = "Main Activity";

    private boolean mTwoPane = false;
    private FragmentManager mFragmentManager;
    private ArrayList<Recipe> mRecipeList = new ArrayList<Recipe>();

    @BindView(R.id.recipe_list_fragment_frame)
    FrameLayout mRecipeListFrame;

    @BindView(R.id.recipe_detail_fragment_frame)
    FrameLayout mRecipeDetailFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (findViewById(R.id.recipe_list_fragment_frame) != null) {
            mTwoPane = true;
            ButterKnife.bind(this);

            if (savedInstanceState == null) {
                mFragmentManager = getSupportFragmentManager();
                final RecipeListFragment recipeListFragment = new RecipeListFragment();

                SharedNetworking.downloadRcipeList(new Callback<ArrayList<Recipe>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                        ArrayList<Recipe> recipeList = response.body();
                        for (Recipe recipeItem : recipeList) {
                            mRecipeList.add(recipeItem);

                            ArrayList<Ingredient> ingredients = recipeItem.recipeIngredients;
                            ArrayList<Step> steps = recipeItem.recipeSteps;
                        }

                        recipeListFragment.setRecipeList(mRecipeList);

                        mFragmentManager.beginTransaction()
                                .replace(R.id.recipe_list_fragment_frame, recipeListFragment)
                                .commit();


                        if (!mRecipeList.isEmpty()) {
                            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
                            recipeDetailFragment.setRecipeItem(mRecipeList.get(0));

                            mFragmentManager.beginTransaction()
                                    .replace(R.id.recipe_detail_fragment_frame, recipeDetailFragment)
                                    .commit();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                        Log.e(TAG, "onFailure: Unable to get data from JSON" + t.getMessage());
                        call.cancel();
                    }
                });

            }
        } else {
            mTwoPane = false;
        }

    }


    @Override
    public void onItemSelected(Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(RECIPE_BUNDLE_KEY, recipe);

        ArrayList<Ingredient> mIngredient = recipe.recipeIngredients;
        bundle.putParcelableArrayList(INGREDIENTS_BUNDLE_KEY, mIngredient);

        ArrayList<Step> mSteps = recipe.recipeSteps;
        bundle.putParcelableArrayList(STEPS_BUNDLE_KEY, mSteps);

        final Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(RECIPE_INTENT_KEY, bundle);

        startActivity(intent);
    }

}
