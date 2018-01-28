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

    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");

    public ChatMessage(String messageText) {
        this.messageText = messageText;
//        this.messageUser = messageUser;

        messageTime= dateFormat.format(new Date()).toString();
    }

    public ChatMessage() {
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
}
