package jkm.com.bakingapp.test;

import android.support.test.espresso.IdlingResource;

import jkm.com.bakingapp.RecipeActivity;

public class RecipeIdlingResource implements IdlingResource {
    private RecipeActivity mActivity;
    private ResourceCallback mCallback;

    public RecipeIdlingResource(RecipeActivity activity) {
        mActivity = activity;
    }

    @Override
    public String getName() {
        return mActivity.getClass().getSimpleName();
    }

    @Override
    public boolean isIdleNow() {
        if (isIdle()) mCallback.onTransitionToIdle();
        return isIdle();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.mCallback = callback;
    }

    private boolean isIdle() {
        return mActivity != null && mCallback != null && mActivity.isFetchFinished();
    }
}
