package com.example.truongle.btlandroid_appraoban.Chat.model;

/**
 * Created by truongle on 06/05/2017.
 */

public class UserContact {
    private String image;
    private String name;
    private String lastMessage;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    private String from;
    private String to;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public UserContact() {

    }

    public UserContact(String image, String name, String lastMessage, String from, String to) {
        this.image = image;
        this.name = name;
        this.lastMessage = lastMessage;
        this.from = from;
        this.to = to;
    }
}
