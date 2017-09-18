package com.awijaya.mybakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.awijaya.mybakingapp.Model.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipeListFragment.OnItemClickListener{

    public static final String RECIPE_BUNDLE_KEY = "recipe_item";
    public static final String RECIPE_INTENT_KEY = "recipe_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onItemSelected(Recipe recipe) {
        Toast.makeText(this, "Recipe Selected for item: " + recipe.recipeName, Toast.LENGTH_SHORT).show();

        Bundle bundle = new Bundle();
        bundle.putParcelable(RECIPE_BUNDLE_KEY, recipe);

        final Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(RECIPE_INTENT_KEY, bundle);

        startActivity(intent);
    }

}
