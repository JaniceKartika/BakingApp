package jkm.com.bakingapp;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jkm.com.bakingapp.model.StepModel;

public class StepDetailFragment extends Fragment {

    @BindView(R.id.view_pager_step_detail)
    ViewPager stepDetailViewPager;
    @BindView(R.id.tab_step_detail)
    TabLayout stepDetailTabLayout;
    @BindView(R.id.iv_arrow_left_step_detail)
    ImageView ivArrowLeft;
    @BindView(R.id.iv_arrow_right_step_detail)
    ImageView ivArrowRight;
    @BindView(R.id.relative_layout_tab)
    RelativeLayout bottomNavigationLayout;

    private OnPageSelected mPageSelectedCallback;

    public StepDetailFragment() {
        // Constructor
    }

    public static StepDetailFragment newInstance(Context context, ArrayList<StepModel> stepModels, int initialPosition) {
        StepDetailFragment stepDetailFragment = new StepDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(context.getString(R.string.steps_key), stepModels);
        bundle.putInt(context.getString(R.string.step_position_key), initialPosition);
        stepDetailFragment.setArguments(bundle);

        return stepDetailFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mPageSelectedCallback = (OnPageSelected) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnPageSelected");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(this, view);

        int orientation = getResources().getConfiguration().orientation;
        if (!getResources().getBoolean(R.bool.isTab)) {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                bottomNavigationLayout.setVisibility(View.GONE);
            } else {
                bottomNavigationLayout.setVisibility(View.VISIBLE);
            }
        }

        setTabLayoutParams();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            ArrayList<StepModel> stepModels = bundle.getParcelableArrayList(getString(R.string.steps_key));
            int initialPosition = bundle.getInt(getString(R.string.step_position_key), 0);
            if (stepModels != null) {
                stepDetailViewPager.setAdapter(new StepViewPagerAdapter(getFragmentManager(), stepModels));
                stepDetailTabLayout.setupWithViewPager(stepDetailViewPager, true);
                stepDetailViewPager.setCurrentItem(initialPosition, true);
            } else {
                bottomNavigationLayout.setVisibility(View.INVISIBLE);
            }
        } else {
            bottomNavigationLayout.setVisibility(View.INVISIBLE);
        }

        ivArrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepDetailViewPager.setCurrentItem(stepDetailViewPager.getCurrentItem() - 1, true);
            }
        });

        ivArrowRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepDetailViewPager.setCurrentItem(stepDetailViewPager.getCurrentItem() + 1, true);
            }
        });

        stepDetailViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // must-override method
            }

            @Override
            public void onPageSelected(int position) {
                if (mPageSelectedCallback != null) {
                    mPageSelectedCallback.setOnPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // must-override method
            }
        });

        return view;
    }

    private void setTabLayoutParams() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        int width = displaymetrics.widthPixels;
        int tabLayoutMargin = width / 4;

        RelativeLayout.LayoutParams layoutParamsForTabLayout = new RelativeLayout
                .LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParamsForTabLayout.rightMargin = tabLayoutMargin;
        layoutParamsForTabLayout.leftMargin = tabLayoutMargin;
        layoutParamsForTabLayout.addRule(RelativeLayout.CENTER_HORIZONTAL);
        stepDetailTabLayout.setLayoutParams(layoutParamsForTabLayout);
    }

    interface OnPageSelected {
        void setOnPageSelected(int position);
    }

    private class StepViewPagerAdapter extends FragmentStatePagerAdapter {
        private ArrayList<StepModel> stepModels;

        StepViewPagerAdapter(FragmentManager manager, ArrayList<StepModel> stepModels) {
            super(manager);
            this.stepModels = stepModels;
        }

        @Override
        public Fragment getItem(int position) {
            return EachStepFragment.newInstance(getContext(), stepModels.get(position));
        }

        @Override
        public int getCount() {
            if (stepModels == null) return 0;
            else return stepModels.size();
        }
    }
}
