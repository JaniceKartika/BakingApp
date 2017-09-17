package jkm.com.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.widget.RemoteViews;

import java.util.ArrayList;

import jkm.com.bakingapp.R;
import jkm.com.bakingapp.data.IngredientColumns;
import jkm.com.bakingapp.model.IngredientModel;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ID_INGREDIENT_LOADER = 20;
    private static final String[] INGREDIENT_PROJECTION = {
            IngredientColumns.QUANTITY,
            IngredientColumns.MEASURE,
            IngredientColumns.INGREDIENT,
            IngredientColumns.RECIPE_ID
    };
    public static final int INDEX_QUANTITY = 0;
    public static final int INDEX_MEASURE = 1;
    public static final int INDEX_INGREDIENT = 2;
    public static final int INDEX_INGREDIENT_RECIPE_ID = 3;

    private ArrayList<IngredientModel> mIngredientModels = new ArrayList<>();

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        CharSequence recipeName = RecipeWidgetConfigurationActivity.loadRecipeNamePreference(context, appWidgetId);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipe);
        views.setTextViewText(R.id.recipe_name_widget_text_view, recipeName);

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
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RecipeWidgetConfigurationActivity.deleteWidgetPreferences(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
