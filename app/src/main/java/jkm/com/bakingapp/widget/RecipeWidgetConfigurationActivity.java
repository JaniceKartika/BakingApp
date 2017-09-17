package jkm.com.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import jkm.com.bakingapp.R;
import jkm.com.bakingapp.data.IngredientColumns;
import jkm.com.bakingapp.data.RecipeColumns;
import jkm.com.bakingapp.data.RecipeProvider;

public class RecipeWidgetConfigurationActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final int ID_RECIPE_LOADER = 10;
    private static final int ID_INGREDIENT_LOADER = 20;

    private static final String[] RECIPE_PROJECTION = {
            RecipeColumns.RECIPE_ID,
            RecipeColumns.NAME,
            RecipeColumns.SERVINGS,
            RecipeColumns.IMAGE
    };
    public static final int INDEX_RECIPE_ID = 0;
    public static final int INDEX_NAME = 1;
    public static final int INDEX_SERVINGS = 2;
    public static final int INDEX_IMAGE = 3;

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

    @BindView(R.id.tv_recipe_widget_configuration)
    TextView recipeWidgetTextView;

    private int mAppWidgetId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);

        setContentView(R.layout.activity_recipe_widget_configuration);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        recipeWidgetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createWidget(RecipeWidgetConfigurationActivity.this);
            }
        });

        getSupportLoaderManager().initLoader(ID_RECIPE_LOADER, null, this);
        getSupportLoaderManager().initLoader(ID_INGREDIENT_LOADER, null, this);
    }

    private void createWidget(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipe);
        appWidgetManager.updateAppWidget(mAppWidgetId, views);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_RECIPE_LOADER:
                Uri recipeQueryUri = RecipeProvider.Recipes.CONTENT_URI;
                String recipeSortOrder = RecipeColumns.ID + " ASC";
                return new CursorLoader(this, recipeQueryUri, RECIPE_PROJECTION, null, null, recipeSortOrder);
            case ID_INGREDIENT_LOADER:
                Uri ingredientQueryUri = RecipeProvider.Ingredients.CONTENT_URI;
                String ingredientSortOrder = IngredientColumns.ID + " ASC";
                return new CursorLoader(this, ingredientQueryUri, INGREDIENT_PROJECTION, null, null, ingredientSortOrder);
            default:
                throw new RuntimeException(getString(R.string.loader_not_implemented) + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case ID_RECIPE_LOADER:
                for (int i = 0; i < data.getCount(); i++) {
                    data.moveToPosition(i);
                    String print = data.getInt(INDEX_RECIPE_ID) + "   " +
                            data.getString(INDEX_NAME) + "   " +
                            data.getInt(INDEX_SERVINGS) + "   " +
                            data.getString(INDEX_IMAGE);
                    Log.d("test", print);
                }
                break;
            case ID_INGREDIENT_LOADER:
                for (int i = 0; i < data.getCount(); i++) {
                    data.moveToPosition(i);
                    String print = data.getDouble(INDEX_QUANTITY) + "   " +
                            data.getString(INDEX_MEASURE) + "   " +
                            data.getString(INDEX_INGREDIENT) + "   " +
                            data.getInt(INDEX_INGREDIENT_RECIPE_ID);
                    Log.d("test", print);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
