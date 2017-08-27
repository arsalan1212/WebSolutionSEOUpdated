package com.example.arsalankhan.websolutionseo.helper;

/**
 * Created by Arsalan khan on 8/27/2017.
 */

public class Messages {

    private String senderId;
    private String message;

    public Messages(){

    }

    public Messages(String userId, String message) {
        this.senderId = userId;
        this.message = message;
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


}
