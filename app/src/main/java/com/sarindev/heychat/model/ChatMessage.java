package com.sarindev.heychat.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SARIN on 3/6/2017.
 */

public class ChatMessage {
    private String messageText;
    private String messageUser;
    private String messageTime;
    private String photoURL;


    public ChatMessage() {
    }

    public ChatMessage(String messageText, String messageUser, String photoURL, String messageTime) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.photoURL = photoURL;
        this.messageTime = messageTime;
    }


    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}
