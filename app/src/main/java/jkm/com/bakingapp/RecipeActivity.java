package jkm.com.bakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class RecipeActivity extends AppCompatActivity implements RecipeFragment.OnRecipeClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
    }

    @Override
    public void onRecipeSelected(RecipeModel recipeModel) {
        // TODO: goto next activity
    }
}
