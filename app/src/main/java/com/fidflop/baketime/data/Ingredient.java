package com.fidflop.baketime.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Ingredient implements Parcelable {
    private int quantity;
    private String measure;
    private String ingredient;

    private static final String QUANTITY = "quantity";
    private static final String MEASURE = "measure";
    private static final String INGREDIENT = "ingredient";


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient() {
        this.ingredient = ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public static List<Ingredient> parseIngredient(JSONArray jsonIngredients) {
        List<Ingredient> ingredients = new ArrayList<>();

        try {

            if (jsonIngredients == null) {
                return ingredients;
            }

            for (int i = 0; i < jsonIngredients.length(); i++) {
                Ingredient ingredient = new Ingredient();
                ingredient.setQuantity(jsonIngredients.getJSONObject(i).getInt(QUANTITY));
                ingredient.setMeasure(jsonIngredients.getJSONObject(i).getString(MEASURE));
                ingredient.setIngredient(jsonIngredients.getJSONObject(i).getString(INGREDIENT));
                ingredients.add(ingredient);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ingredients;
    }

    private Ingredient() {
    }

    protected Ingredient(Parcel in) {
        quantity = in.readInt();
        measure = in.readString();
        ingredient = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}