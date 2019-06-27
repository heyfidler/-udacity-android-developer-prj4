package com.fidflop.baketime.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class RecipeStep implements Parcelable {
    private int id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    private static final String SHORT_DESCRIPTION = "shortDescription";
    private static final String DESCRIPTION = "description";
    private static final String VIDEO_URL = "videoURL";
    private static final String THUMBNAIL_URL = "thumbnailURL";
    private static final String ID = "id";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public static List<RecipeStep> parseRecipeStep(JSONArray jsonRecipeSteps) {
        List<RecipeStep> recipeSteps = new ArrayList<>();

        try {

            if (jsonRecipeSteps == null) {
                return recipeSteps;
            }

            for (int i = 0; i < jsonRecipeSteps.length(); i++) {
                RecipeStep recipeStep = new RecipeStep();
                recipeStep.setShortDescription(jsonRecipeSteps.getJSONObject(i).getString(SHORT_DESCRIPTION));
                recipeStep.setDescription(jsonRecipeSteps.getJSONObject(i).getString(DESCRIPTION));
                recipeStep.setThumbnailURL(jsonRecipeSteps.getJSONObject(i).getString(THUMBNAIL_URL));
                recipeStep.setVideoURL(jsonRecipeSteps.getJSONObject(i).getString(VIDEO_URL));
                recipeStep.setId(jsonRecipeSteps.getJSONObject(i).getInt(ID));
                recipeSteps.add(recipeStep);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipeSteps;
    }

    private RecipeStep() {
    }

    protected RecipeStep(Parcel in) {
        id = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<RecipeStep> CREATOR = new Parcelable.Creator<RecipeStep>() {
        @Override
        public RecipeStep createFromParcel(Parcel in) {
            return new RecipeStep(in);
        }

        @Override
        public RecipeStep[] newArray(int size) {
            return new RecipeStep[size];
        }
    };
}