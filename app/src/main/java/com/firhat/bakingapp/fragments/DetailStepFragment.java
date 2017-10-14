package com.firhat.bakingapp.fragments;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.data.LocalUriFetcher;
import com.firhat.bakingapp.R;
import com.firhat.bakingapp.models.Recipe;
import com.firhat.bakingapp.models.Step;
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


import java.net.URLConnection;
import java.util.List;

public class DetailStepFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String TAG = DetailStepFragment.class.getSimpleName();

    private SimpleExoPlayer exoPlayer;
    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;

    private Button prevBtn;

    private SimpleExoPlayerView videoPlayer;

    Uri videoUri;
    long position;

    Recipe recipe;
    List<Step> stepList;
    int stepIndex;

    public DetailStepFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.display_detail_steps, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Step step = null;

        if (getArguments() != null) {
            step = getArguments().getParcelable(getResources().getString(R.string.parcel_step));
            recipe = getArguments().getParcelable(getResources().getString(R.string.parcel_step_data));
            stepList = recipe.getSteps();
            stepIndex = Integer.valueOf(getArguments().getString("index"));
        }
        videoUri = Uri.parse(step.getVideoURL());

        TextView instruction = (TextView) view.findViewById(R.id.tv_step_instruction);
        ImageView thumbnail = (ImageView) view.findViewById(R.id.iv_thumbnail);
        videoPlayer = (SimpleExoPlayerView) view.findViewById(R.id.video_player);
        Button nextBtn = (Button) view.findViewById(R.id.next);
        prevBtn = (Button) view.findViewById(R.id.prev);

        if (stepIndex == 0){
            prevBtn.setVisibility(View.INVISIBLE);
        }


        instruction.setText(step.getDescription());

        if (savedInstanceState != null) {
            position = savedInstanceState.getLong(getResources().getString(R.string.selected_position));
        }


        if (step.getThumbnailURL() != null && !step.getThumbnailURL().isEmpty()) {
            /*if (isVideoFile(step.getThumbnailURL())) {
                initializeVideoPlayer(Uri.parse(step.getThumbnailURL()));
            } else {
                Glide.with(getContext())
                        .load(step.getThumbnailURL())
                        .into(thumbnail);
            }*/
            Glide.with(getContext())
                    .load(step.getThumbnailURL())
                    .into(thumbnail);
        } else {
            thumbnail.setVisibility(View.GONE);
        }

        if (step.getVideoURL() != null && !step.getVideoURL().isEmpty()) {
            initializeVideoPlayer(videoUri);
        }else{
            videoPlayer.setVisibility(View.GONE);

        }

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Step listStep;

                if (stepIndex<stepList.size()-1){
                    listStep = stepList.get(stepIndex+1);
                    stepIndex = stepIndex+1;
                    //Log.e("SIZE", String.valueOf(stepList.size()));
                    //Log.e("index", String.valueOf(stepIndex));

                }else{
                    listStep = stepList.get(0);
                    stepIndex = 0;
                }

                DetailStepFragment fragment = new DetailStepFragment();
                Bundle args = new Bundle();
                args.putParcelable(getResources().getString(R.string.parcel_step), listStep);
                args.putParcelable(getResources().getString(R.string.parcel_step_data), recipe);
                args.putString("index", String.valueOf(stepIndex));
                fragment.setArguments(args);

                getFragmentManager().beginTransaction()
                        .replace(R.id.container_step, fragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Step listStep;

                if (stepIndex >= 0){
                    listStep = stepList.get(stepIndex-1);
                    DetailStepFragment fragment = new DetailStepFragment();
                    Bundle args = new Bundle();
                    args.putParcelable(getResources().getString(R.string.parcel_step), listStep);
                    args.putParcelable(getResources().getString(R.string.parcel_step_data), recipe);
                    args.putString("index", String.valueOf(stepIndex-1));
                    fragment.setArguments(args);

                    getFragmentManager().beginTransaction()
                            .replace(R.id.container_step, fragment)
                            .addToBackStack(null)
                            .commit();
                }else{
                    prevBtn.setVisibility(View.INVISIBLE);
                }



            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(getResources().getString(R.string.selected_position), position);
    }

    public static boolean isVideoFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("video");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (videoUri != null) {
            if (exoPlayer != null) {
//                initializeVideoPlayer(videoUri);
                exoPlayer.seekTo(position);
            } else {
                initializeVideoPlayer(videoUri);
            }
        }

        if (stepIndex == 0){
            prevBtn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (exoPlayer != null) {
            position = exoPlayer.getCurrentPosition();
            Log.e(TAG, "get current position : " + position);
        }
        releasePlayer();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    private void initializeMediaSession() {
        mediaSession = new MediaSessionCompat(getContext(), this.getClass().getSimpleName());
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
            MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setMediaButtonReceiver(null);
        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE |
                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                    PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mediaSession.setPlaybackState(stateBuilder.build());
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                exoPlayer.setPlayWhenReady(true);
            }

            @Override
            public void onPause() {
                exoPlayer.setPlayWhenReady(false);
            }

            @Override
            public void onSkipToPrevious() {
                exoPlayer.seekTo(0);
            }
        });
        mediaSession.setActive(true);
    }

    private void initializeVideoPlayer(Uri videoUri) {
        initializeMediaSession();
        if (exoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            videoPlayer.setPlayer(exoPlayer);
            exoPlayer.addListener(this);

            String userAgent = Util.getUserAgent(getContext(), "StepVideo");
            MediaSource mediaSource = new ExtractorMediaSource(videoUri,
                    new DefaultDataSourceFactory(getContext(), userAgent),
                    new DefaultExtractorsFactory(),
                    null,
                    null);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
            exoPlayer.seekTo(0,position);
        }
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }

        if (mediaSession != null) {
            mediaSession.setActive(false);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, exoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, exoPlayer.getCurrentPosition(), 1f);
        }
        mediaSession.setPlaybackState(stateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPositionDiscontinuity() {

    }
}
