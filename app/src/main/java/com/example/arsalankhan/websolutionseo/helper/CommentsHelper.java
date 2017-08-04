package com.example.arsalankhan.websolutionseo.helper;

/**
 * Created by Arsalan khan on 8/3/2017.
 */

public class CommentsHelper {

    private String DisplayName;
    private String profileImage;
    private String comment;
    private String publishedAt;
    private String replyCount;


    public CommentsHelper(String displayName, String profileImage, String comment, String replyCount,String publishedAt) {
        DisplayName = displayName;
        this.profileImage = profileImage;
        this.comment = comment;
        this.replyCount = replyCount;
        this.publishedAt = publishedAt;
    }

    public String getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(String replyCount) {
        this.replyCount = replyCount;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }
}
