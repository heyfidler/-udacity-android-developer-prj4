package com.fidflop.baketime.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {
    private String name;
    private int id;

    private List<Ingredient> ingredients;
    private List<RecipeStep> recipeSteps;

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String INGREDIENTS = "ingredients";
    private static final String STEPS = "steps";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    private void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<RecipeStep> getRecipeSteps() {
        return recipeSteps;
    }

    private void setRecipeSteps(List<RecipeStep> recipeSteps) {
        this.recipeSteps = recipeSteps;
    }

    public static ArrayList<Recipe> parseRecipes(JSONArray json) {
        ArrayList<Recipe> recipes = new ArrayList<>();

        if (json == null) {
            return recipes;
        }

        try {
            for (int i = 0; i < json.length(); i++) {
                Recipe recipe = new Recipe();
                recipe.setName(json.getJSONObject(i).getString(NAME));
                recipe.setId(json.getJSONObject(i).getInt(ID));
                recipes.add(recipe);
                recipe.setIngredients(Ingredient.parseIngredient(json.getJSONObject(i).getJSONArray(INGREDIENTS)));
                recipe.setRecipeSteps(RecipeStep.parseRecipeStep(json.getJSONObject(i).getJSONArray(STEPS)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipes;
    }

    public Recipe() {
    }

    private Recipe(Parcel in) {
        name = in.readString();
        id = in.readInt();
        if (in.readByte() == 0x01) {
            ingredients = new ArrayList<>();
            in.readList(ingredients, Ingredient.class.getClassLoader());
        } else {
            ingredients = null;
        }
        if (in.readByte() == 0x01) {
            recipeSteps = new ArrayList<>();
            in.readList(recipeSteps, RecipeStep.class.getClassLoader());
        } else {
            recipeSteps = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(id);
        if (ingredients == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(ingredients);
        }
        if (recipeSteps == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(recipeSteps);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}