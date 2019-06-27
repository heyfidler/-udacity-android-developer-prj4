package com.fidflop.baketime.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.fidflop.baketime.R;
import com.fidflop.baketime.data.Recipe;
import com.fidflop.baketime.util.NetworkHelper;

import java.util.ArrayList;
import java.util.List;

public class RecipeListActivity extends AppCompatActivity {

    private static final String TAG = RecipeListActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private ArrayList<Recipe> recipes;
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        final int columns = getResources().getInteger(R.integer.list_columns);
        mRecyclerView = findViewById(R.id.idRecyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, columns));

        if (savedInstanceState != null) {
            this.getRecipes(savedInstanceState.getParcelableArrayList("recipes"));
        } else {
            this.getRecipes(null);
        }
    }

    private void populateCards(List<Recipe> recipes) {
        RecipeListAdapter recipeListAdapter = new RecipeListAdapter(recipes,this);
        mRecyclerView.setAdapter(recipeListAdapter);

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(true);
        }
    }


    private void getRecipes(ArrayList<Recipe> recipes) {
        if (recipes != null) {
            this.recipes = recipes;
            populateCards(recipes);
            return;
        }

        final RecipeListActivity activity = this;
        JsonArrayRequest jsonObjectRequest;
        jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET,
                NetworkHelper.RECIPE_URL,
                null,
                response -> {
                    this.recipes = Recipe.parseRecipes(response);

                    activity.populateCards(this.recipes);
                },
                Throwable::printStackTrace
        );
        NetworkHelper.getInstance().getRequestQueue(this.getApplicationContext()).add(jsonObjectRequest);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList("recipes", recipes);
    }



    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
