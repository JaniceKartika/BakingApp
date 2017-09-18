package jkm.com.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.DecimalFormat;

import jkm.com.bakingapp.R;
import jkm.com.bakingapp.data.IngredientColumns;
import jkm.com.bakingapp.data.RecipeProvider;

public class RecipeWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class RecipeRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private Cursor mCursor;

    private int recipeId;

    RecipeRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        recipeId = intent.getIntExtra(RecipeWidgetProvider.RECIPE_ID, 0);
    }

    @Override
    public void onCreate() {
        // must-implement method
    }

    @Override
    public void onDataSetChanged() {
        Uri ingredientQueryUri = RecipeProvider.Ingredients.fromRecipe(recipeId);
        if (mCursor != null) mCursor.close();
        mCursor = mContext.getContentResolver().query(ingredientQueryUri, null, null, null, null);
    }

    @Override
    public void onDestroy() {
        mCursor.close();
    }

    @Override
    public int getCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mCursor == null || mCursor.getCount() == 0) return null;

        mCursor.moveToPosition(position);
        int quantityIndex = mCursor.getColumnIndex(IngredientColumns.QUANTITY);
        int measureIndex = mCursor.getColumnIndex(IngredientColumns.MEASURE);
        int ingredientIndex = mCursor.getColumnIndex(IngredientColumns.INGREDIENT);

        double quantity = mCursor.getDouble(quantityIndex);
        String measure = mCursor.getString(measureIndex);
        String ingredient = mCursor.getString(ingredientIndex);

        String formattedQuantity = new DecimalFormat("#.#").format(quantity);
        String formattedIngredient = formattedQuantity + " " + measure + " " + ingredient;

        RemoteViews ingredientView = new RemoteViews(mContext.getPackageName(), R.layout.item_ingredient);
        ingredientView.setTextViewText(R.id.tv_ingredient, formattedIngredient);

        return ingredientView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
