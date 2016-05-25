package com.rikachka.track_android_3_3.Messages;


public class Message {
    private String action;
    private Data data;

    public Message() {

    }

    public Message(String action, Data data) {
        this.action = action;
        this.data = data;
    }

    public String getAction() {
        return action;
    }

    public Data getData() {
        return data;
    }
}
