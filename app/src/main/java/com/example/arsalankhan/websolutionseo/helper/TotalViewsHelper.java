package com.example.arsalankhan.websolutionseo.helper;

/**
 * Created by Arsalan khan on 8/3/2017.
 */

public class TotalViewsHelper {

    private String totalViews;
    private String totalLiskes;
    private String totlaDislikes;
    private String commentCount;

    public TotalViewsHelper(String totalViews, String totalLiskes, String totlaDislikes, String commentCount) {
        this.totalViews = totalViews;
        this.totalLiskes = totalLiskes;
        this.totlaDislikes = totlaDislikes;
        this.commentCount = commentCount;
    }

    public String getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(String totalViews) {
        this.totalViews = totalViews;
    }

    public String getTotalLiskes() {
        return totalLiskes;
    }

    public void setTotalLiskes(String totalLiskes) {
        this.totalLiskes = totalLiskes;
    }

    public String getTotlaDislikes() {
        return totlaDislikes;
    }

    public void setTotlaDislikes(String totlaDislikes) {
        this.totlaDislikes = totlaDislikes;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }
}
