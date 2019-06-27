package com.fidflop.baketime.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;


import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fidflop.baketime.R;
import com.fidflop.baketime.data.Recipe;
import com.fidflop.baketime.data.RecipeStep;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.Objects;

public class RecipeStepFragment extends Fragment implements Player.EventListener {

    private static final String TAG = RecipeStepFragment.class.getSimpleName();
    private int step;
    private View view;
    private Recipe recipe;
    private RecipeStep recipeStep;
    private DataSource.Factory dataSourceFactory;
    private SimpleExoPlayer player;
    private PlayerView playerView;
    private boolean isLandscape = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_recipe_step,
                container, false);

        return view;
    }

    public void setStep(int step, Recipe recipe) {
        this.step = step;
        this.recipe = recipe;
        this.recipeStep = recipe.getRecipeSteps().get(step);

        TextView view = getView().findViewById(R.id.recipe_step_text_view);

        view.setText(recipeStep.getDescription());

        setupButtons();
        initializePlayer();
    }

    private void setupButtons() {
        Button prevButton = view.findViewById(R.id.prev_button);
        Button nextButton = view.findViewById(R.id.next_button);

        if (step < 1) {
            prevButton.setVisibility(View.INVISIBLE);
        } else {
            prevButton.setVisibility(View.VISIBLE);
            prevButton.setOnClickListener((View v) -> setStep(--step, recipe));
        }

        if (step >= recipe.getRecipeSteps().size() - 1) {
            nextButton.setVisibility(View.INVISIBLE);
        } else {
            nextButton.setVisibility(View.VISIBLE);
            nextButton.setOnClickListener((View v) -> setStep(++step, recipe));
        }
    }

    private void initializePlayer() {
        if (playerView == null) {
            this.playerView = view.findViewById(R.id.exoplayer);
        }

        if (recipeStep.getVideoURL() == null || TextUtils.isEmpty(recipeStep.getVideoURL())) {
            playerView.setVisibility(View.INVISIBLE);
            return;
        }

        playerView.setVisibility(View.VISIBLE);
        if (isLandscape) {
            ViewGroup.LayoutParams params = playerView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = params.MATCH_PARENT;
            playerView.setLayoutParams(params);
        }

        //Initialize the player
        if (player == null) {
            this.player = ExoPlayerFactory.newSimpleInstance(getContext());
            player.addListener(this);
            playerView.setPlayer(player);
        }

        // Produces DataSource instances through which media data is loaded.
        if (this.dataSourceFactory == null) {
            this.dataSourceFactory =
                    new DefaultDataSourceFactory(Objects.requireNonNull(getActivity()), Util.getUserAgent(getContext(), "BakeTime"));
        }

        MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .setExtractorsFactory(new DefaultExtractorsFactory())
                .createMediaSource(Uri.parse(recipeStep.getVideoURL()));

        player.prepare(mediaSource);

    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    public int getStep() {
        return step;
    }

    public void setLandscape(boolean landscape) {
        isLandscape = landscape;
    }


}

