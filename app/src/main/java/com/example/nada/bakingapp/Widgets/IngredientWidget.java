package com.example.nada.bakingapp.Widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import com.example.nada.bakingapp.Activities.MainActivity;
import com.example.nada.bakingapp.Activities.RecipeDetailsActivity;
import com.example.nada.bakingapp.Models.Recipe;
import com.example.nada.bakingapp.R;
import com.google.gson.Gson;

import static com.example.nada.bakingapp.Utils.Constants.RECIPE_KEY;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientWidget extends AppWidgetProvider {

    /*static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget);
       // views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }*/

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        RemoteViews remoteView = getViewWidget(context, options);
        appWidgetManager.updateAppWidget(appWidgetId, remoteView);

    }

    private static RemoteViews getViewWidget(Context context, Bundle options) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget);

        Recipe sharedPref_recipe = MainActivity.getDataFromSharedPrefs(context);

        views.setTextViewText(R.id.widget_recipeName, sharedPref_recipe.getName());

        Gson gson = new Gson();
        String recipe = gson.toJson(sharedPref_recipe);

        Intent recipe_intent = new Intent(context, RecipeDetailsActivity.class);
        recipe_intent.putExtra(RECIPE_KEY, recipe);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, recipe_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_recipeName, pendingIntent);

        Intent ingredientIntent = new Intent(context, ListViewWidgetService.class);
        views.setRemoteAdapter(R.id.widget_ingredientList, ingredientIntent);

        return views;
    }

    public static void updateAllAppWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        WidgetUpdateService.startActionUpdateAppWidgets(context, true);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        WidgetUpdateService.startActionUpdateAppWidgets(context, true);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }
}

