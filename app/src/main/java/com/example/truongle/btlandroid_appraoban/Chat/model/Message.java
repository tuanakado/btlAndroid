package com.example.truongle.btlandroid_appraoban.Chat.model;

/**
 * Created by truongle on 25/04/2017.
 */

public class Message {
    private String mess, date, current_user, time;
    private boolean position;


    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Message() {

    }

    public String getCurrent_user() {
        return current_user;
    }

    public void setCurrent_user(String current_user) {
        this.current_user = current_user;
    }

    public boolean isPosition() {
        return position;
    }

    public void setPosition(boolean position) {
        this.position = position;
    }

    public Message(String mess, String date, String time , String current_user) {
        this.mess = mess;
        this.date = date;
        this.time = time;
        this.current_user = current_user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
