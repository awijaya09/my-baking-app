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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        mFragmentManager = getSupportFragmentManager();

        getIntentData();
    }

    private void getIntentData(){
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(MainActivity.RECIPE_INTENT_KEY);
        Recipe recipe = bundle.getParcelable(MainActivity.RECIPE_BUNDLE_KEY);

        RecipeDetailFragment fragObject = new RecipeDetailFragment();
        fragObject.setArguments(bundle);
        mFragmentManager.beginTransaction().replace(R.id.recipe_detail_fragment, fragObject).commit();

    }
}
