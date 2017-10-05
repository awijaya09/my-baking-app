package com.awijaya.mybakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.awijaya.mybakingapp.Model.Ingredient;
import com.awijaya.mybakingapp.Model.Recipe;
import com.awijaya.mybakingapp.Model.Step;
import com.awijaya.mybakingapp.Networking.SharedNetworking;

import android.support.test.espresso.idling.CountingIdlingResource;
import android.widget.ProgressBar;
import android.widget.Toast;

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
    private Recipe curRecipe;
    private Context mContext;
    private HomeListAdapter adapter;
    private ArrayList<Recipe> mRecipeList = new ArrayList<Recipe>();
    private ArrayList<Ingredient> mIngredients = new ArrayList<Ingredient>();
    private ArrayList<String> mIngredientString = new ArrayList<String>();
    CountingIdlingResource countingIdlingResource = new CountingIdlingResource("Retrofit_call");

    @Nullable @BindView(R.id.pb_main_activity)
    ProgressBar mProgressbar;

    @BindView(R.id.recipe_list_fragment_frame)
    FrameLayout mRecipeListFrame;

    @Nullable @BindView(R.id.recipe_detail_fragment_frame)
    FrameLayout mRecipeDetailFrame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mFragmentManager = getSupportFragmentManager();

        if (findViewById(R.id.recipe_detail_fragment_frame) != null) {
            mTwoPane = true;
            ButterKnife.bind(this);
            mProgressbar.animate();
            if (savedInstanceState == null) {
                countingIdlingResource.increment();
                SharedNetworking.downloadRcipeList(new Callback<ArrayList<Recipe>>() {

                    @Override
                    public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                        ArrayList<Recipe> recipeList = response.body();
                        for (Recipe recipeItem : recipeList) {
                            mRecipeList.add(recipeItem);

                        }

                        placeRecipeListFragment(mRecipeList);

                        if (!mRecipeList.isEmpty()) {
                            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
                            recipeDetailFragment.setRecipeItem(mRecipeList.get(0));
                            curRecipe = mRecipeList.get(0);
                            mFragmentManager.beginTransaction()
                                    .replace(R.id.recipe_detail_fragment_frame, recipeDetailFragment)
                                    .commit();
                        }

                    }

                    @Override
                    public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                        failedInGettingData(call, t);
                    }
                });

            }
        } else {
            mTwoPane = false;

            if (savedInstanceState == null) {
                countingIdlingResource.increment();
                ButterKnife.bind(this);

                SharedNetworking.downloadRcipeList(new Callback<ArrayList<Recipe>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                        ArrayList<Recipe> recipeList = response.body();
                        for (Recipe recipeItem : recipeList) {
                            mRecipeList.add(recipeItem);

                        }

                        placeRecipeListFragment(mRecipeList);
                        countingIdlingResource.decrement();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                        failedInGettingData(call, t);
                    }
                });
            }

        }

    }

    private void placeRecipeListFragment(ArrayList<Recipe> items) {
        final RecipeListFragment recipeListFragment = new RecipeListFragment();
        recipeListFragment.setRecipeList(mRecipeList);
        mProgressbar.setVisibility(View.GONE);
        mFragmentManager.beginTransaction()
                .replace(R.id.recipe_list_fragment_frame, recipeListFragment)
                .commit();
    }

    private void failedInGettingData(Call<ArrayList<Recipe>> call, Throwable t){
        Log.e(TAG, "onFailure: Unable to get data from JSON" + t.getMessage());
        Toast.makeText(mContext, "Sorry, something wrong with the connection!", Toast.LENGTH_SHORT).show();
        call.cancel();
        countingIdlingResource.decrement();
    }

    public CountingIdlingResource getEspressoIdlingResourceForMainActivity() {
        return countingIdlingResource;
    }

    private void setupIngredientMenu() {
        mIngredients = curRecipe.recipeIngredients;
        mIngredientString = SharedNetworking.addIngredientStrings(mIngredients);
    }


    @Override
    public void onItemSelected(Recipe recipe) {

        if (mTwoPane) {
            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setRecipeItem(recipe);
            curRecipe = recipe;

            setupIngredientMenu();

            mFragmentManager.beginTransaction()
                    .replace(R.id.recipe_detail_fragment_frame, recipeDetailFragment)
                    .commit();
        } else {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mTwoPane) {

            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.ingredient, menu);

            return true;
        }

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ingredient_list) {
            if (mIngredientString.isEmpty()) {
                setupIngredientMenu();
            }
            new MaterialDialog.Builder(this)
                    .title(R.string.ingredient_dialog_title)
                    .titleColorRes(R.color.colorPrimary)
                    .dividerColorRes(R.color.colorAccent)
                    .items(mIngredientString)
                    .show();
        }

        return true;
    }
}
