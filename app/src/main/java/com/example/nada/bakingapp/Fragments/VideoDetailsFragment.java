package com.example.nada.bakingapp.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.nada.bakingapp.Models.Step;
import com.example.nada.bakingapp.R;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.nada.bakingapp.Utils.Constants.VIDEO_KEY;

public class VideoDetailsFragment extends Fragment implements Player.EventListener {

    private static final String TAG = VideoDetailsFragment.class.getName();

    //Views
    @BindView(R.id.textView_videoDescription)
    TextView textView_videoDescription;
    @BindView(R.id.videoPlayerView)
    PlayerView videoPlayerView;
    @BindView(R.id.videoLayout)
    ConstraintLayout videoLayout;
    @BindView(R.id.videoLoader)
    ProgressBar videoLoader;

    //Vars
    private Step mStep;
    private SimpleExoPlayer simpleExoPlayer;
    private static MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder playbackStateBuilder;
    private boolean isPlaying = true;
    private long currentPlayerPosition = 0;

    public VideoDetailsFragment() {

    }

    public static VideoDetailsFragment newInstance(Step step) {
        VideoDetailsFragment videoDetailsFragment = new VideoDetailsFragment();
        Bundle videoDetailsBundle = new Bundle();
        videoDetailsBundle.putParcelable(VIDEO_KEY, step);
        videoDetailsFragment.setArguments(videoDetailsBundle);
        return videoDetailsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video_details, container, false);

        ButterKnife.bind(this, rootView);

        mStep = (Step) getArguments().getParcelable(VIDEO_KEY);

        initializeMediaSession();
        if (!mStep.getVideoURL().isEmpty()) {
            setPlayerAndLoadingIndicatorSize();
            initializePlayer(Uri.parse(mStep.getVideoURL()));
        } else {
            if (!mStep.getThumbnailURL().isEmpty()) {
                setPlayerAndLoadingIndicatorSize();
                initializePlayer(Uri.parse(mStep.getThumbnailURL()));
            } else {
                videoLayout.setVisibility(View.GONE);
            }
        }

        textView_videoDescription.setText(mStep.getDescription());

        return rootView;
    }

    private void setPlayerAndLoadingIndicatorSize() {
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;

        int videoHeightPixels = (1080 * widthPixels) / 1920;
        ViewGroup.LayoutParams layoutParams = videoPlayerView.getLayoutParams();
        layoutParams.height = videoHeightPixels;
        videoPlayerView.setLayoutParams(layoutParams);
    }

    private void initializeMediaSession() {
        mediaSession = new MediaSessionCompat(getContext(), TAG);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mediaSession.setMediaButtonReceiver(null);

        playbackStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mediaSession.setPlaybackState(playbackStateBuilder.build());

        mediaSession.setCallback(new MediaSessionCallbacks());
        mediaSession.setActive(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        initializeMediaSession();
        if (Util.SDK_INT <= 23 || simpleExoPlayer == null) {
            if (!mStep.getVideoURL().isEmpty()) {
                setPlayerAndLoadingIndicatorSize();
                initializePlayer(Uri.parse(mStep.getVideoURL()));
            } else {
                if (!mStep.getThumbnailURL().isEmpty()) {
                    setPlayerAndLoadingIndicatorSize();
                    initializePlayer(Uri.parse(mStep.getThumbnailURL()));
                } else {
                    videoLayout.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeMediaSession();
        if (Util.SDK_INT <= 23 || simpleExoPlayer == null) {
            if (!mStep.getVideoURL().isEmpty()) {
                setPlayerAndLoadingIndicatorSize();
                initializePlayer(Uri.parse(mStep.getVideoURL()));
            } else {
                if (!mStep.getThumbnailURL().isEmpty()) {
                    setPlayerAndLoadingIndicatorSize();
                    initializePlayer(Uri.parse(mStep.getThumbnailURL()));
                } else {
                    videoLayout.setVisibility(View.GONE);
                }
            }
        }
    }

    private void initializePlayer(Uri videoUrl) {
        if (simpleExoPlayer == null) {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(bandwidthMeter);
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            videoPlayerView.setPlayer(simpleExoPlayer);
            simpleExoPlayer.addListener(this);

            String userAgent = Util.getUserAgent(getContext(), "Baking App");
            MediaSource mediaSource = new ExtractorMediaSource.Factory(
                    new DefaultDataSourceFactory(getContext(), userAgent))
                    .createMediaSource(videoUrl);
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.seekTo(currentPlayerPosition);
            simpleExoPlayer.setPlayWhenReady(isPlaying);
            isPlaying = true;
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (Player.STATE_READY == playbackState) {
            videoLoader.setVisibility(View.GONE);
            videoPlayerView.setVisibility(View.VISIBLE);
            if (playWhenReady) {
                playbackStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                        simpleExoPlayer.getCurrentPosition(), 1f);
                isPlaying = true;
            } else {
                playbackStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                        simpleExoPlayer.getCurrentPosition(), 1f);
                isPlaying = false;
            }
        }

    }

    public class MediaSessionCallbacks extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            simpleExoPlayer.setPlayWhenReady(true);
            isPlaying = true;
        }

        @Override
        public void onPause() {
            simpleExoPlayer.setPlayWhenReady(false);
            isPlaying = false;
        }

        @Override
        public void onSkipToPrevious() {
            simpleExoPlayer.seekTo(0);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
        if (simpleExoPlayer != null) {
            currentPlayerPosition = simpleExoPlayer.getCurrentPosition();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        deactivateMediaSession();
    }

    private void deactivateMediaSession() {
        if (mediaSession != null)
            mediaSession.setActive(false);
    }

    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mediaSession, intent);
        }

    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
}
