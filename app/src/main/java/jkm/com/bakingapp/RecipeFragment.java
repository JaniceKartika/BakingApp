package jkm.com.bakingapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeFragment extends Fragment {
    private static final String TAG = RecipeFragment.class.getSimpleName();

    @BindView(R.id.rv_recipe_list)
    RecyclerView recipeRecyclerView;
    @BindView(R.id.tv_recipe_list_empty)
    TextView recipeEmptyTextView;
    @BindView(R.id.pb_recipe_list)
    ProgressBar recipeProgressBar;

    private RecipeAdapter mAdapter;
    private ArrayList<RecipeModel> mRecipeModels = new ArrayList<>();

    public RecipeFragment() {
        // Constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        ButterKnife.bind(this, view);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            configureRecyclerView(recipeRecyclerView, 2);
        } else {
            configureRecyclerView(recipeRecyclerView, 1);
        }

        mAdapter = new RecipeAdapter(getActivity(), mRecipeModels, (RecipeActivity) getActivity());
        recipeRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private void configureRecyclerView(RecyclerView recyclerView, int spanCount) {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, 0, true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void showLoading() {
        recipeRecyclerView.setVisibility(View.INVISIBLE);
        recipeEmptyTextView.setVisibility(View.GONE);
        recipeProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        recipeRecyclerView.setVisibility(View.VISIBLE);
        recipeEmptyTextView.setVisibility(View.GONE);
        recipeProgressBar.setVisibility(View.GONE);
    }

    private void showEmptyMessage() {
        recipeRecyclerView.setVisibility(View.INVISIBLE);
        recipeEmptyTextView.setVisibility(View.VISIBLE);
        recipeProgressBar.setVisibility(View.GONE);
    }
}
