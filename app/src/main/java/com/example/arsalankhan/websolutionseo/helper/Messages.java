package com.example.arsalankhan.websolutionseo.helper;

/**
 * Created by Arsalan khan on 8/27/2017.
 */

public class Messages {

    private String senderId;
    private String message;
    private String displayName;
    private String photoUrl;

    public Messages(){

    }

    public Messages(String userId, String message,String displayName,String photoUrl) {
        this.senderId = userId;
        this.message = message;
        this.displayName = displayName;
        this.photoUrl = photoUrl;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String userId) {
        this.senderId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
