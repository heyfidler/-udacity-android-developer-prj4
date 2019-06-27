package com.fidflop.baketime.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fidflop.baketime.R;
import com.fidflop.baketime.data.Ingredient;
import com.fidflop.baketime.data.Recipe;
import com.fidflop.baketime.data.RecipeStep;
public class RecipeDetailFragment extends Fragment {

    private static final String TAG = RecipeDetailFragment.class.getSimpleName();

    private OnStepSelectedListener listener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe_detail,
                container, false);
    }

    public void setRecipe(Recipe recipe) {
        setupIngredients(recipe);
        setupRecipeSteps(recipe);
    }

    public interface OnStepSelectedListener {
        void onStepSelected(int step);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStepSelectedListener) {
            listener = (OnStepSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement RecipeDetailFragment.OnStepSelectedListener");
        }
    }

    private void setupRecipeSteps(Recipe recipe) {
        final LinearLayout layout = getView().findViewById(R.id.recipe_detail_steps_layout);

        LayoutInflater inflater = LayoutInflater.from(getContext());

        if (recipe == null || recipe.getRecipeSteps() == null) {
            return;
        }

        for (final RecipeStep recipeStep:recipe.getRecipeSteps()) {
            Button b = (Button)inflater.inflate(R.layout.step, layout, false);
            b.setText(recipeStep.getShortDescription());

            b.setOnClickListener((View v) -> updateStep(recipeStep.getId()));

            layout.addView(b);
        }
    }

    public void updateStep(int step) {
        listener.onStepSelected(step);
    }

    private void setupIngredients(Recipe recipe) {
        TextView view = getView().findViewById(R.id.recipe_detail_ingredients);

        view.setText(Html.fromHtml("<h2>" + recipe.getName() + " ingredients:</h2>"));

        if (recipe == null || recipe.getIngredients() == null) {
            return;
        }

        for (Ingredient ingredient : recipe.getIngredients()) {
            view.append("\t\t");
            view.append(ingredient.getIngredient());
            view.append("\t");
            view.append(Integer.toString(ingredient.getQuantity()));
            view.append(" x ");
            view.append(ingredient.getMeasure());
            view.append("\n");
        }
    }
}

