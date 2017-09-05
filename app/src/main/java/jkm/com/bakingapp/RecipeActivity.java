package jkm.com.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import jkm.com.bakingapp.model.RecipeModel;

public class RecipeActivity extends AppCompatActivity implements RecipeFragment.OnRecipeClickListener,
        RecipeFragment.OnRecipeFetchFinished {

    private ArrayList<RecipeModel> recipeModels;
    private boolean isFetchFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
    }

    @Override
    public void setOnRecipeSelected(RecipeModel recipeModel) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(getString(R.string.recipes_key), recipeModel);
        startActivity(intent);
    }

    @Override
    public void setOnRecipeFetchFinished(ArrayList<RecipeModel> recipeModels) {
        setRecipeModels(recipeModels);
        setFetchFinished(true);
    }

    public boolean isFetchFinished() {
        return isFetchFinished;
    }

    public void setFetchFinished(boolean fetchFinished) {
        isFetchFinished = fetchFinished;
    }

    public ArrayList<RecipeModel> getRecipeModels() {
        return recipeModels;
    }

    public void setRecipeModels(ArrayList<RecipeModel> recipeModels) {
        this.recipeModels = recipeModels;
    }
}
