package com.awijaya.mybakingapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.session.MediaSession;
import android.net.Uri;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.VectorEnabledTintResources;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.awijaya.mybakingapp.Model.Ingredient;
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
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by awijaya on 9/18/17.
 */

public class RecipeDetailListViewAdapter extends BaseAdapter implements ExoPlayer.EventListener{

    private LayoutInflater inflater;
    private ArrayList<Step> mSteps = new ArrayList<Step>();
    private Recipe mRecipe;
    private int mIndex;

    private MediaSessionCompat mMediaSession;
    private SimpleExoPlayer mExoPlayer;
    private PlaybackStateCompat.Builder mStateBuilder;
    private Context mContext;
    private static final String MEDIA_TAG = "Media Session";

    private static final String TAG = "Recipe Detail Adapter";

    public RecipeDetailListViewAdapter(Context context, Recipe recipe, int stepIndex){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRecipe = recipe;
        mSteps = recipe.recipeSteps;
        mIndex = stepIndex;

    }


    @Override
    public int getCount() {
            return mSteps.size()+2;
    }

    @Override
    public Object getItem(int i) {
            return mSteps.get(i-2);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else if (position == 1){
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if (position == 0) {
            VideoViewHolder videoViewHolder;

            if (view != null) {
                videoViewHolder = (VideoViewHolder) view.getTag();
            } else {
                view = inflater.inflate(R.layout.recipe_detail_video, viewGroup, false);
                videoViewHolder = new VideoViewHolder(view);
                view.setTag(videoViewHolder);
            }

            mContext = view.getContext();


            Uri mediaURI = Uri.parse(mRecipe.recipeSteps.get(mIndex).stepVideoURL);

            initializeMediaSession();
            initializePlayer(videoViewHolder.mSimpleExoPlayer, mediaURI);

            return view;
        } else if (position == 1) {
            StepsViewHolder stepsViewHolder;

            if(view != null) {
                stepsViewHolder = (StepsViewHolder) view.getTag();
            } else {
                view = inflater.inflate(R.layout.recipe_detail_steps, viewGroup, false);
                stepsViewHolder = new StepsViewHolder(view);
                view.setTag(stepsViewHolder);
            }

            return view;

        }
            else {
            IngredientsViewHolder ingredientsViewHolder;

            if(view != null) {
                ingredientsViewHolder = (IngredientsViewHolder) view.getTag();
            } else {
                view = inflater.inflate(R.layout.recipe_detail_ingredients, viewGroup, false);
                ingredientsViewHolder = new IngredientsViewHolder(view);
                view.setTag(ingredientsViewHolder);
            }
            Step stepItem = mRecipe.recipeSteps.get(position-2);
            ingredientsViewHolder.mIngQty.setText(stepItem.stepShortDescription);
            ingredientsViewHolder.mIngMeasure.setText(stepItem.stepDescription);

            return view;
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


    public void releasePlayer(){
        if (mExoPlayer != null){
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }

    }

    public void playNextVideo(Uri mediaUri){
        mExoPlayer.stop();

        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        DefaultDataSourceFactory defaultDataSourceFactory = new DefaultDataSourceFactory(mContext, "BakingMedia");

        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, defaultDataSourceFactory, extractorsFactory, null, null );
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(true);

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

        mMediaSession.setCallback(new MySessionCallback());

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
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private class MySessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
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
    }



    // View Holder Class for 3 different types of view holder
    static class VideoViewHolder {
        @BindView(R.id.simple_exoplayer)
        SimpleExoPlayerView mSimpleExoPlayer;

        public VideoViewHolder(View view){

            ButterKnife.bind(this, view);
            mSimpleExoPlayer.setDefaultArtwork(BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.exoplayer_artwork));

        }

    }


    static class StepsViewHolder {
        @BindView(R.id.text_view_steps_title)
        TextView mIngTitle;

        public StepsViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }

    static class IngredientsViewHolder {
        @BindView(R.id.text_view_ingredient_item_qty)
        TextView mIngQty;

        @BindView(R.id.text_view_ingredient_item_measure)
        TextView mIngMeasure;

        public IngredientsViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
