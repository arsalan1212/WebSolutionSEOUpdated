package com.websolutionseo.arsalankhan.websolutionseo.helper;

/**
 * Created by Arsalan khan on 8/2/2017.
 */

public class PlaylistHelper {

    private String title;
    private String description;
    private String thumbnail;
    private String videoId;
    private String channelTitle;
    private String VideopublishDate;

    public PlaylistHelper(){

    }

    public PlaylistHelper(String title, String description, String thumbnail, String videoId, String channelTitle,String videopublishDate) {
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.videoId = videoId;
        this.channelTitle = channelTitle;
        this.VideopublishDate = videopublishDate;
    }

    public String getVideopublishDate() {
        return VideopublishDate;
    }

    public void setVideopublishDate(String videopublishDate) {
        VideopublishDate = videopublishDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }
}

