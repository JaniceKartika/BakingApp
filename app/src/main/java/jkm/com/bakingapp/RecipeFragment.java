package jkm.com.bakingapp;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jkm.com.bakingapp.adapter.RecipeAdapter;
import jkm.com.bakingapp.model.RecipeModel;
import jkm.com.bakingapp.util.GridSpacingItemDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class RecipeFragment extends Fragment implements RecipeAdapter.OnItemClickListener {
    private static final String TAG = RecipeFragment.class.getSimpleName();

    @BindView(R.id.rv_recipe_list)
    RecyclerView recipeRecyclerView;
    @BindView(R.id.tv_recipe_list_empty)
    TextView recipeEmptyTextView;
    @BindView(R.id.pb_recipe_list)
    ProgressBar recipeProgressBar;

    private RecipeAdapter mAdapter;
    private ArrayList<RecipeModel> mRecipeModels = new ArrayList<>();

    private OnRecipeClickListener mRecipeClickCallback;
    private OnRecipeFetchFinished mRecipeFetchFinishedCallback;
    private RecipeInterface mRecipeInterface;

    public RecipeFragment() {
        // Constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mRecipeClickCallback = (OnRecipeClickListener) context;
            mRecipeFetchFinishedCallback = (OnRecipeFetchFinished) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement both" +
                    "OnRecipeClickListener and OnRecipeFetchFinished");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        ButterKnife.bind(this, view);

        int orientation = getResources().getConfiguration().orientation;
        if (getResources().getBoolean(R.bool.isTab)) {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                configureRecyclerView(recipeRecyclerView, 3);
            } else {
                configureRecyclerView(recipeRecyclerView, 2);
            }
        } else {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                configureRecyclerView(recipeRecyclerView, 2);
            } else {
                configureRecyclerView(recipeRecyclerView, 1);
            }
        }

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.RECIPE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        mRecipeInterface = retrofit.create(RecipeInterface.class);

        if (savedInstanceState != null) {
            mRecipeModels = savedInstanceState.getParcelableArrayList(getString(R.string.recipes_key));
        } else {
            callRecipes();
        }

        mAdapter = new RecipeAdapter(getContext(), mRecipeModels, this);
        recipeRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void setOnItemClickListener(View view, int position) {
        if (mRecipeClickCallback != null) {
            mRecipeClickCallback.setOnRecipeSelected(mRecipeModels.get(position));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getString(R.string.recipes_key), mRecipeModels);
    }

    private void callRecipes() {
        showLoading();
        Call<ArrayList<RecipeModel>> recipes = mRecipeInterface.getRecipes();
        recipes.enqueue(new Callback<ArrayList<RecipeModel>>() {
            @Override
            public void onResponse(Call<ArrayList<RecipeModel>> call, Response<ArrayList<RecipeModel>> response) {
                if (response.body() != null) {
                    ArrayList<RecipeModel> recipeModels = response.body();
                    if (!recipeModels.isEmpty()) {
                        mRecipeModels.clear();
                        mRecipeModels.addAll(recipeModels);

                        mAdapter.notifyDataSetChanged();
                        hideLoading();
                    } else {
                        showEmptyMessage();
                    }
                } else {
                    showEmptyMessage();
                }

                if (mRecipeFetchFinishedCallback != null) {
                    mRecipeFetchFinishedCallback.setOnRecipeFetchFinished();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RecipeModel>> call, Throwable t) {
                Log.e(TAG, getString(R.string.failed_fetch_recipes), t.getCause());
                showEmptyMessage();
            }
        });
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

    interface OnRecipeClickListener {
        void setOnRecipeSelected(RecipeModel recipeModel);
    }

    interface OnRecipeFetchFinished {
        void setOnRecipeFetchFinished();
    }

    interface RecipeInterface {
        @GET("android-baking-app-json")
        Call<ArrayList<RecipeModel>> getRecipes();
    }
}
