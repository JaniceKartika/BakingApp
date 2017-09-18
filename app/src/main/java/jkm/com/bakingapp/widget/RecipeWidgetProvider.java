package jkm.com.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import jkm.com.bakingapp.R;

public class RecipeWidgetProvider extends AppWidgetProvider {
    public static final String RECIPE_ID = "recipeId";

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipe);

        CharSequence recipeName = RecipeWidgetConfigurationActivity.loadRecipeNamePreference(context, appWidgetId);
        views.setTextViewText(R.id.recipe_name_widget_text_view, recipeName);

        int recipeId = RecipeWidgetConfigurationActivity.loadRecipeIdPreference(context, appWidgetId);
        Intent intent = new Intent(context, RecipeWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.putExtra(RECIPE_ID, recipeId);
        views.setRemoteAdapter(R.id.recipe_ingredients_widget_list_view, intent);

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
}
