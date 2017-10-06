package com.awijaya.mybakingapp;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.BoolRes;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.awijaya.mybakingapp.Model.Recipe;
import com.awijaya.mybakingapp.Model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by awijaya on 10/5/17.
 */

public class RecipeDetailListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ExoPlayer.EventListener {

    private ArrayList<Step> mSteps = new ArrayList<Step>();
    private Recipe mRecipe;
    private int stepIndex = 0;

    private MediaSessionCompat mMediaSession;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mExoPlayerView;
    private PlaybackStateCompat.Builder mStateBuilder;
    private Context mContext;
    private long mLastPosition = 0l;
    private static final String MEDIA_TAG = "Media Session";
    private static final String TAG = "Recipe Detail Adapter";

    public RecipeDetailListAdapter (Context context, Recipe item) {
        this.mRecipe = item;
        this.mSteps = item.recipeSteps;
        this.mContext = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        Boolean shouldAttachToParent = false;
        switch (viewType) {
            case 0:
                int layoutIdForItem = R.layout.recipe_detail_video;
                LayoutInflater inflater = LayoutInflater.from(context);
                View view = inflater.inflate(layoutIdForItem, parent, shouldAttachToParent);
                return new RecipeVideoViewHolder(view);
            case 1:
                int titleLayoutId = R.layout.recipe_detail_steps;
                LayoutInflater titleInflater = LayoutInflater.from(context);
                View titleView = titleInflater.inflate(titleLayoutId, parent, shouldAttachToParent);
                return new RecipeDetailTitleViewHolder(titleView);
            case 2:
                int layoutId = R.layout.recipe_detail_ingredients;
                LayoutInflater inf = LayoutInflater.from(context);
                View stepView = inf.inflate(layoutId, parent, shouldAttachToParent);
                return new RecipeDetailListViewHolder(stepView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        Log.d(TAG, "onBindViewHolder: viewtype: " + viewType);
        switch (viewType){
            case 0:
                Uri mediaURI = Uri.parse(mRecipe.recipeSteps.get(stepIndex).stepVideoURL);
                RecipeVideoViewHolder videoHolder = (RecipeVideoViewHolder) holder;
                mExoPlayerView = videoHolder.mSimpleExoPlayer;
                initializeMediaSession();
                initializePlayer(mExoPlayerView, mediaURI);
                return;
            case 2:
                Step stepItem = mRecipe.recipeSteps.get(position-2);
                RecipeDetailListViewHolder recipeDetail = (RecipeDetailListViewHolder) holder;
                recipeDetail.mIngQty.setText(stepItem.stepShortDescription);
                recipeDetail.mIngMeasure.setText(stepItem.stepDescription);
                if (stepItem.stepThumbnailURL != null && !stepItem.stepThumbnailURL.equals("")) {
                    recipeDetail.mImageViewStep.setVisibility(View.VISIBLE);
                    Picasso.with(mContext)
                            .load(Uri.parse(stepItem.stepThumbnailURL))
                            .placeholder(R.drawable.exoplayer_artwork)
                            .into(recipeDetail.mImageViewStep);
                }
                return;
            default:
                break;
        }
    }


    @Override
    public int getItemCount() {
        return mSteps.size()+2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position > 1) {
            return 2;
        } else {
            return position;
        }
    }


    class RecipeDetailListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_view_ingredient_item_qty)
        TextView mIngQty;

        @BindView(R.id.text_view_ingredient_item_measure)
        TextView mIngMeasure;

        @BindView(R.id.image_view_steps)
        ImageView mImageViewStep;

        public RecipeDetailListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class RecipeDetailTitleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view_steps_title)
        TextView mStepTitle;

        public RecipeDetailTitleViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class RecipeVideoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.simple_exoplayer)
        SimpleExoPlayerView mSimpleExoPlayer;

        public RecipeVideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void releasePlayer(){
        if (mExoPlayer != null){
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }

    }

    public void playNextVideo(Uri mediaUri){
        if (mExoPlayer != null){
            mExoPlayer.stop();

            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            DefaultDataSourceFactory defaultDataSourceFactory = new DefaultDataSourceFactory(mContext, "BakingMedia");

            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, defaultDataSourceFactory, extractorsFactory, null, null );
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    public void initializeMedia() {
        if (mExoPlayerView != null) {
            Uri mediaURI = Uri.parse(mRecipe.recipeSteps.get(stepIndex).stepVideoURL);
            initializeMediaSession();
            initializePlayer(mExoPlayerView, mediaURI);
        }
    }

    private void initializeMediaSession() {
        mMediaSession = new MediaSessionCompat(mContext, TAG);
        mMediaSession.setMediaButtonReceiver(null);
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE
                );

        mMediaSession.setPlaybackState(mStateBuilder.build());

        mMediaSession.setCallback(new RecipeDetailListAdapter.MySessionCallback());

        mMediaSession.setActive(true);
    }

    private void initializePlayer(SimpleExoPlayerView mSimpleExoPlayerView, Uri mediaUri) {
        if(mExoPlayer == null){
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
            mSimpleExoPlayerView.setPlayer(mExoPlayer);

            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            DefaultDataSourceFactory defaultDataSourceFactory = new DefaultDataSourceFactory(mContext, "BakingMedia");

            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, defaultDataSourceFactory, extractorsFactory, null, null );
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.seekTo(mLastPosition);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private class MySessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            mExoPlayer.seekTo(mLastPosition);
            mExoPlayer.setPlayWhenReady(true);

        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }

        @Override
        public void onStop() {
            super.onStop();
            mLastPosition = mExoPlayer.getCurrentPosition();
            mExoPlayer.setPlayWhenReady(false);
            mExoPlayer.release();
        }

    }

    public long getPlayerCurrentPost(){
        if (mExoPlayer != null) {
            return mExoPlayer.getCurrentPosition();
        }
        return 0l;
    }

    public void setPlayerCurrentPost(long curPost){
        this.mLastPosition = curPost;
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

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

}
