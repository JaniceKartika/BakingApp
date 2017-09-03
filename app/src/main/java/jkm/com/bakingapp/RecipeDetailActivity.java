package jkm.com.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity {

    @BindView(R.id.collapsing_toolbar_recipe_detail)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.iv_recipe_detail)
    ImageView recipeDetailImageView;
    @BindView(R.id.toolbar_recipe_detail)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        int orientation = getResources().getConfiguration().orientation;
        setToolbarViewParams(orientation);

        Intent intent = getIntent();
        if (intent.hasExtra(getString(R.string.recipes_key))) {
            RecipeModel recipeModel = intent.getParcelableExtra(getString(R.string.recipes_key));

            collapsingToolbar.setTitle(recipeModel.getName());
            collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
            collapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);

            String imagePath = recipeModel.getImage() == null ? "" : recipeModel.getImage();
            if (imagePath.isEmpty()) {
                recipeDetailImageView.setImageResource(RecipeAssets.getRecipeImageID(recipeModel.getName()));
            } else {
                Picasso.with(this)
                        .load(imagePath)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(recipeDetailImageView);
            }

            if (savedInstanceState == null) {
                RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
                recipeDetailFragment.setRecipeModel(recipeModel);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.recipe_detail_container, recipeDetailFragment)
                        .commit();
            }
        } else {
            Toast.makeText(this, getString(R.string.failed_show_recipe_detail), Toast.LENGTH_LONG).show();
        }
    }

    private void setToolbarViewParams(int orientation) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;

        int imageHeight;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            imageHeight = (width * 2) / 3;
        } else {
            imageHeight = (height * 4 / 9);
        }

        CollapsingToolbarLayout.LayoutParams layoutParamsForImageView = new CollapsingToolbarLayout
                .LayoutParams(CollapsingToolbarLayout.LayoutParams.MATCH_PARENT, imageHeight);
        recipeDetailImageView.setLayoutParams(layoutParamsForImageView);
    }
}
