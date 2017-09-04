package jkm.com.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import jkm.com.bakingapp.model.StepModel;

public class EachStepFragment extends Fragment {

    @BindView(R.id.player_view)
    SimpleExoPlayerView mPlayerView;
    @BindView(R.id.iv_each_step)
    ImageView ivEachStep;
    @BindView(R.id.tv_each_step)
    TextView tvEachStep;

    private StepModel stepModel;

    private SimpleExoPlayer mExoPlayer;

    public EachStepFragment() {
        // Constructor
    }

    public static EachStepFragment newInstance(Context context, StepModel stepModel) {
        EachStepFragment eachStepFragment = new EachStepFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(context.getString(R.string.each_step_key), stepModel);
        eachStepFragment.setArguments(bundle);

        return eachStepFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_each_step, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            stepModel = bundle.getParcelable(getString(R.string.each_step_key));
            if (stepModel != null) {
                if (stepModel.getVideoURL() != null && !stepModel.getVideoURL().isEmpty()) {
                    mPlayerView.setVisibility(View.VISIBLE);
                    initializePlayer(Uri.parse(stepModel.getVideoURL()));
                } else {
                    mPlayerView.setVisibility(View.GONE);
                }

                if (stepModel.getThumbnailURL() != null && !stepModel.getThumbnailURL().isEmpty()) {
                    ivEachStep.setVisibility(View.VISIBLE);
                    Picasso.with(getContext())
                            .load(stepModel.getThumbnailURL())
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(ivEachStep);
                } else {
                    ivEachStep.setVisibility(View.GONE);
                }

                if (stepModel.getDescription() != null && !stepModel.getDescription().isEmpty()) {
                    tvEachStep.setVisibility(View.VISIBLE);
                    tvEachStep.setText(stepModel.getDescription());
                } else {
                    tvEachStep.setVisibility(View.GONE);
                }
            } else {
                hideAllViews();
            }
        } else {
            hideAllViews();
        }

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (mExoPlayer != null) {
                mExoPlayer.setPlayWhenReady(false);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                    new DefaultDataSourceFactory(getContext(), userAgent),
                    new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(false);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private void hideAllViews() {
        mPlayerView.setVisibility(View.INVISIBLE);
        ivEachStep.setVisibility(View.INVISIBLE);
        tvEachStep.setVisibility(View.INVISIBLE);
    }
}
