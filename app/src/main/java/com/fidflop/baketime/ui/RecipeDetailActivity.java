package com.fidflop.baketime.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fidflop.baketime.R;
import com.fidflop.baketime.data.Recipe;

import java.util.Objects;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailFragment.OnStepSelectedListener {

    private static final String TAG = RecipeDetailActivity.class.getSimpleName();
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        if (savedInstanceState != null) {
            this.recipe =savedInstanceState.getParcelable("recipe");
        } else {
            this.recipe = Objects.requireNonNull(getIntent().getExtras()).getParcelable("recipe");
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(recipe.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        RecipeDetailFragment fragment = (RecipeDetailFragment) getSupportFragmentManager()
            .findFragmentById(R.id.recipe_detail_fragment);
        if (fragment != null) {
            fragment.setRecipe(recipe);
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onStepSelected(int step) {
        RecipeStepFragment fragment = (RecipeStepFragment) getSupportFragmentManager()
                .findFragmentById(R.id.recipe_step_fragment);

        if (fragment == null || ! fragment.isInLayout()) {
            Intent intent = new Intent(this, RecipeStepActivity.class);
            intent.putExtra("recipe", recipe);
            intent.putExtra("step", step);
            this.startActivity(intent);
        }
        else {
            fragment.setStep(step, this.recipe);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("recipe", recipe);
    }
}
