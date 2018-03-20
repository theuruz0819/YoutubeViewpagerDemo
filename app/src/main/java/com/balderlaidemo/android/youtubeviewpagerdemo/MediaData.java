package com.balderlaidemo.android.youtubeviewpagerdemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
 store youtube video play time,
 */
public class MediaData {
    private static final MediaData ourInstance = new MediaData();

    public static MediaData getInstance() {
        return ourInstance;
    }

    protected List<String> mediaDatas = new ArrayList<>();

    private Map<String, String> videoPlayTimeCache = new HashMap<>();

    public List<String> getMediaDatas() {
        return mediaDatas;
    }

    public void setMediaDatas(List<String> mediaDatas) {
        this.mediaDatas = mediaDatas;
    }

    public Map<String, String> getVideoPlayTimeCache() {
        return videoPlayTimeCache;
    }

    public void setVideoPlayTimeCache(Map<String, String> videoPlayTimeCache) {
        this.videoPlayTimeCache = videoPlayTimeCache;
    }
}
