package com.balderlaidemo.android.youtubeviewpagerdemo;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class MediaFragmentViewPagerAdapter extends FragmentStatePagerAdapter
        implements MediaYoutubeFragment.MediaFullScreenListener {

    protected List<String> mResources;
    protected Context context;

    private boolean isFullScreen;

    public MediaFragmentViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        isFullScreen = false;
    }

    public int currentPosition = -1;


    @Override
    public void startFullScreen() {
        if (!isFullScreen){
            // not implement yet
        }
    }

    @Override
    public Fragment getItem(int position) {

        // get video Id from url
        String videoId = getVideoIdFromUrl(mResources.get(position));

        MediaYoutubeFragment youTubePlayerSupportFragment =
                MediaYoutubeFragment.newInstance(videoId,context.getResources().getString(R.string.youtube_apikey), this);
        return youTubePlayerSupportFragment;
    }

    @Override
    public int getCount() {
        return mResources.size();
    }

    public List<String> getmResources() {
        return mResources;
    }

    public void setmResources(List<String> mResources) {
        this.mResources = mResources;
    }

    protected String getVideoIdFromUrl(String url){
        if (url.contains("http")){
            if (!url.contains("https")){
                url = url.replace("http", "https");
            }
        }
        if (url.contains("watch?v=")){
            if(url.contains("&list=")){
                return url.substring(url.indexOf("?v=")+3, url.indexOf("&list="));
            }
            return url.replace("https://www.youtube.com/watch?v=", "");
        }else if(url.contains("https://youtu.be/")){
            return url.replace("https://youtu.be/", "");
        }
        return "";
    }
}
