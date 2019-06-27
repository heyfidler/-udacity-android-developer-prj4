package com.fidflop.baketime;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RemoteViews;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.fidflop.baketime.data.Ingredient;
import com.fidflop.baketime.data.Recipe;
import com.fidflop.baketime.util.NetworkHelper;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class BakeTimeWidget extends AppWidgetProvider {

    private static final String TAG = BakeTimeWidget.class.getSimpleName();

    private static ArrayList<Recipe> recipes;
    private static final String RECIPE = "recipe";
    private static final String WIDGET_ID = "WIDGET_ID";

    private AppWidgetManager appWidgetManager;


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.app_name);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bake_time_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        for(Recipe recipe:recipes) {
            RemoteViews buttonRV = new RemoteViews(context.getPackageName(), R.layout.widget_button);
            views.addView(R.id.buttonLinearLayout,buttonRV);

            buttonRV.setTextViewText(R.id.button_for_widget,recipe.getName());
            buttonRV.setOnClickPendingIntent(R.id.button_for_widget, getPendingSelfIntent(context, recipe, appWidgetId));
        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        this.appWidgetManager = appWidgetManager;

        JsonArrayRequest jsonObjectRequest;
        jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET,
                NetworkHelper.RECIPE_URL,
                null,
                response -> {
                    this.recipes = Recipe.parseRecipes(response);

                    for (int appWidgetId : appWidgetIds) {
                        updateAppWidget(context, appWidgetManager, appWidgetId);
                    }
                },
                Throwable::printStackTrace
        );
        NetworkHelper.getInstance().getRequestQueue(context).add(jsonObjectRequest);
    }

    private static PendingIntent getPendingSelfIntent(Context context, Recipe recipe, int appWidgetId) {
        Intent intent = new Intent(context, BakeTimeWidget.class);
        intent.putExtra(RECIPE, recipe);
        intent.putExtra(WIDGET_ID, appWidgetId);
        return PendingIntent.getBroadcast(context, recipe.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static String buildRecipeString(Recipe recipe) {
        StringBuilder sb = new StringBuilder();
        for (Ingredient ingredient : recipe.getIngredients()) {
            sb.append("\t\t");
            sb.append(ingredient.getIngredient());
            sb.append("\t");
            sb.append(Integer.toString(ingredient.getQuantity()));
            sb.append(" x ");
            sb.append(ingredient.getMeasure());
            sb.append("\n");
        }

        return sb.toString();
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Recipe recipe = intent.getParcelableExtra(RECIPE);
        if (recipe != null) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bake_time_widget);
            views.setTextViewText(R.id.appwidget_text, buildRecipeString(recipe));
            views.setTextViewTextSize(R.id.appwidget_text, TypedValue.COMPLEX_UNIT_SP, Float.parseFloat("14"));
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            appWidgetManager.updateAppWidget(intent.getIntExtra(WIDGET_ID, 0), views);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

