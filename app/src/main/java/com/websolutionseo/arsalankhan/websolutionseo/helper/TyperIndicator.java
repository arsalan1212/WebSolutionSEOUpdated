package com.websolutionseo.arsalankhan.websolutionseo.helper;

/**
 * Created by Arsalan khan on 8/29/2017.
 */

public class TyperIndicator {

    private String isTyping;
    private String senderId;

    public TyperIndicator(){

    }

    public TyperIndicator(String isTyping, String senderId) {
        this.isTyping = isTyping;
        this.senderId = senderId;
    }

    public String getIsTyping() {
        return isTyping;
    }

    public void setIsTyping(String isTyping) {
        this.isTyping = isTyping;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
