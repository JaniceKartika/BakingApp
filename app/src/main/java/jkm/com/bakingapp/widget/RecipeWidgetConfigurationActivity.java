package jkm.com.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jkm.com.bakingapp.R;
import jkm.com.bakingapp.adapter.RecipeAdapter;
import jkm.com.bakingapp.data.RecipeColumns;
import jkm.com.bakingapp.data.RecipeProvider;
import jkm.com.bakingapp.model.RecipeModel;
import jkm.com.bakingapp.util.GridSpacingItemDecoration;

public class RecipeWidgetConfigurationActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        RecipeAdapter.OnItemClickListener {
    private static final String PREFS_NAME = "RecipeWidgetPrefs";
    private static final String PREF_ID_PREFIX_KEY = "recipeId";
    private static final String PREF_NAME_PREFIX_KEY = "recipeName";

    private static final int ID_RECIPE_LOADER = 10;
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

    @BindView(R.id.rv_recipe_widget_configuration)
    RecyclerView recipeWidgetConfigurationRecyclerView;

    private int mRecipeWidgetId;

    private RecipeAdapter mAdapter;
    private ArrayList<RecipeModel> mRecipeModels = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);

        setContentView(R.layout.activity_recipe_widget_configuration);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mRecipeWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        if (mRecipeWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            configureRecyclerView(recipeWidgetConfigurationRecyclerView, 2);
        } else {
            configureRecyclerView(recipeWidgetConfigurationRecyclerView, 1);
        }
        mAdapter = new RecipeAdapter(this, mRecipeModels, this);
        recipeWidgetConfigurationRecyclerView.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(ID_RECIPE_LOADER, null, this);
    }

    private void configureRecyclerView(RecyclerView recyclerView, int spanCount) {
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, 0, true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void createWidget(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RecipeWidgetProvider.updateAppWidget(context, appWidgetManager, mRecipeWidgetId);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mRecipeWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    @Override
    public void setOnItemClickListener(View view, int position) {
        RecipeModel recipeModel = mRecipeModels.get(position);
        saveWidgetPreferences(this, mRecipeWidgetId, recipeModel.getId(), recipeModel.getName());
        createWidget(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.widget_config_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_cancel) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_RECIPE_LOADER:
                Uri recipeQueryUri = RecipeProvider.Recipes.CONTENT_URI;
                String recipeSortOrder = RecipeColumns.ID + " ASC";
                return new CursorLoader(this, recipeQueryUri, RECIPE_PROJECTION, null, null, recipeSortOrder);
            default:
                throw new RuntimeException(getString(R.string.loader_not_implemented) + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() == 0) {
            Toast.makeText(this, getString(R.string.recipe_not_available), Toast.LENGTH_LONG).show();
            finish();
        } else {
            convertToRecipeModel(data);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mRecipeModels.clear();
        mAdapter.notifyDataSetChanged();
    }

    private void convertToRecipeModel(Cursor data) {
        mRecipeModels.clear();
        for (int i = 0; i < data.getCount(); i++) {
            data.moveToPosition(i);
            RecipeModel model = new RecipeModel();
            model.setId(data.getInt(INDEX_RECIPE_ID));
            model.setName(data.getString(INDEX_NAME));
            model.setServings(data.getInt(INDEX_SERVINGS));
            model.setImage(data.getString(INDEX_IMAGE));
            mRecipeModels.add(model);
        }
    }

    private static void saveWidgetPreferences(Context context, int appWidgetId, int recipeId, String recipeName) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt(PREF_ID_PREFIX_KEY + appWidgetId, recipeId);
        editor.putString(PREF_NAME_PREFIX_KEY + appWidgetId, recipeName);
        editor.apply();
    }

    public static int loadRecipeIdPreference(Context context, int appWidgetId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getInt(PREF_ID_PREFIX_KEY + appWidgetId, 0);
    }

    public static String loadRecipeNamePreference(Context context, int appWidgetId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(PREF_NAME_PREFIX_KEY + appWidgetId, "");
    }

    public static void deleteWidgetPreferences(Context context, int appWidgetId) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.remove(PREF_ID_PREFIX_KEY + appWidgetId);
        editor.remove(PREF_NAME_PREFIX_KEY + appWidgetId);
        editor.apply();
    }
}
