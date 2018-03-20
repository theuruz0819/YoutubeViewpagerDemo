package com.balderlaidemo.android.youtubeviewpagerdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import static com.google.android.youtube.player.YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT;

public class MediaYoutubeFragment extends YouTubePlayerSupportFragment
        implements YouTubePlayer.OnInitializedListener, YouTubePlayer.OnFullscreenListener {

    private static final String KEY_VIDEO_ID = "VIDEO_ID";
    private static final String API_KEY = "API_KEY";
    public String apiKey;
    private String mVideoId;
    private YouTubePlayer mPlayer;
    boolean mIsFullScreen;
    private MediaFullScreenListener fullScreenListener;

    public void setFullScreenListener(MediaFullScreenListener fullScreenListener) {
        this.fullScreenListener = fullScreenListener;
    }

    public interface MediaFullScreenListener{
        void startFullScreen();
    }

    public MediaYoutubeFragment(){
        super();
    }

    public static MediaYoutubeFragment newInstance(String videoId, String apiKey, MediaFullScreenListener listener) {
        MediaYoutubeFragment frag = new MediaYoutubeFragment();

        Bundle args = new Bundle();
        args.putString(KEY_VIDEO_ID, videoId);
        args.putString(API_KEY, apiKey);
        frag.setArguments(args);
        frag.setFullScreenListener(listener);
        return frag;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            mVideoId = getArguments().getString(KEY_VIDEO_ID);
            apiKey = getArguments().getString(API_KEY);
        }
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // initialize youtube video on view created, and only when fragment is visible for user
        if (getUserVisibleHint()){
            initialize(apiKey, this);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // start video when fragment visibility is changed
        if (isResumed() && isVisibleToUser) {
            initialize(apiKey, this);
        } else {
            if (mPlayer != null){
                MediaData.getInstance().getVideoPlayTimeCache().put(mVideoId, String.valueOf(mPlayer.getCurrentTimeMillis()));
                mPlayer.release();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        mPlayer = youTubePlayer;

        mPlayer.setFullscreenControlFlags(FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        mPlayer.setOnFullscreenListener(this);
        mPlayer.setFullscreen(false);
        mPlayer.setShowFullscreenButton(true);

        mPlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
            @Override
            public void onPlaying() {
                /*
                if(getActivity() instanceof MediaViewPagerActivity){
                    ((MediaViewPagerActivity) getActivity()).hideSystemUi();
                }*/
            }

            @Override
            public void onPaused() {
                MediaData.getInstance().getVideoPlayTimeCache().put(mVideoId, String.valueOf(mPlayer.getCurrentTimeMillis()));
            }

            @Override
            public void onStopped() {
                MediaData.getInstance().getVideoPlayTimeCache().put(mVideoId, String.valueOf(mPlayer.getCurrentTimeMillis()));
            }

            @Override
            public void onBuffering(boolean b) {

            }

            @Override
            public void onSeekTo(int i) {

            }
        });

        if (!wasRestored) {
            String ytVideoId = mVideoId;
            String time = MediaData.getInstance().getVideoPlayTimeCache().get(ytVideoId);
            if (time != null){
                mPlayer.cueVideo(ytVideoId, Integer.parseInt(time));
            } else {
                mPlayer.cueVideo(ytVideoId);
            }
        }
        else
        {
            mPlayer.play();
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    public void onFullscreen(boolean fullscreen) {
        mPlayer.setFullscreen(false);
        fullScreenListener.startFullScreen();
    }
}
