package jkm.com.bakingapp;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import jkm.com.bakingapp.model.StepModel;

public class EachStepFragment extends Fragment implements ExoPlayer.EventListener {

    @BindView(R.id.player_view)
    SimpleExoPlayerView mPlayerView;
    @BindView(R.id.player_container)
    FrameLayout mPlayerContainer;
    @BindView(R.id.iv_each_step)
    ImageView ivEachStep;
    @BindView(R.id.tv_each_step)
    TextView tvEachStep;

    private StepModel stepModel;

    private SimpleExoPlayer mExoPlayer;
    private long playerPosition;
    private boolean isPlaying;

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

        int orientation = getResources().getConfiguration().orientation;
        setPlayerLayoutParams(orientation);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            stepModel = bundle.getParcelable(getString(R.string.each_step_key));
            if (stepModel != null) {
                if (stepModel.getVideoURL() != null && !stepModel.getVideoURL().isEmpty()) {
                    mPlayerContainer.setVisibility(View.VISIBLE);
                    if (savedInstanceState != null) {
                        isPlaying = savedInstanceState.getBoolean(getString(R.string.playing_key));
                        playerPosition = savedInstanceState.getLong(getString(R.string.player_position_key), 0);
                    }
                    initializePlayer(Uri.parse(stepModel.getVideoURL()), isPlaying);
                    mExoPlayer.seekTo(playerPosition);
                } else {
                    mPlayerContainer.setVisibility(View.GONE);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(getString(R.string.playing_key), isPlaying);
        if (mExoPlayer != null) {
            outState.putLong(getString(R.string.player_position_key), mExoPlayer.getCurrentPosition());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    private void initializePlayer(Uri mediaUri, boolean isPlaying) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.addListener(this);

            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                    new DefaultDataSourceFactory(getContext(), userAgent),
                    new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(isPlaying);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private void setPlayerLayoutParams(int orientation) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;

        int playerHeight;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            playerHeight = height / 3;
        } else {
            if (!getResources().getBoolean(R.bool.isTab)) {
                playerHeight = height;
            } else {
                playerHeight = height / 2;
            }
        }

        LinearLayout.LayoutParams layoutParamsForPlayer = new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, playerHeight);
        mPlayerContainer.setLayoutParams(layoutParamsForPlayer);
    }

    private void hideAllViews() {
        mPlayerContainer.setVisibility(View.INVISIBLE);
        ivEachStep.setVisibility(View.INVISIBLE);
        tvEachStep.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
        // must-override method
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        // must-override method
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        // must-override method
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            isPlaying = true;
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            isPlaying = false;
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        // must-override method
    }

    @Override
    public void onPositionDiscontinuity() {
        // must-override method
    }
}
