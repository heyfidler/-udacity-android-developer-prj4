package com.fidflop.baketime.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.fidflop.baketime.R;
import com.fidflop.baketime.data.Recipe;

public class RecipeStepActivity extends AppCompatActivity {

    private static final String TAG = RecipeStepActivity.class.getSimpleName();
    private Recipe recipe;
    private RecipeStepFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);
        int step = 0;

        if (savedInstanceState != null) {
            this.recipe = savedInstanceState.getParcelable("recipe");
            step = savedInstanceState.getInt("step");
        } else if (getIntent().getExtras() != null) {
            this.recipe = getIntent().getExtras().getParcelable("recipe");
            step = getIntent().getExtras().getInt("step");
        }

        boolean isLandscape = false;
        if (Configuration.ORIENTATION_LANDSCAPE == getResources().getConfiguration().orientation) {
            isLandscape = true;
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (isLandscape) {
                actionBar.hide();
            } else {
                actionBar.show();
                actionBar.setTitle(recipe.getName());
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        this.fragment = (RecipeStepFragment) getSupportFragmentManager()
                .findFragmentById(R.id.recipe_step_fragment);



        if (fragment != null) {
            fragment.setLandscape(isLandscape);
            fragment.setStep(step, recipe);
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("recipe", recipe);
        savedInstanceState.putInt("step", fragment.getStep());
    }

}
