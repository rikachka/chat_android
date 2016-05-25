package com.rikachka.track_android_3_3.Classes;

public class User {
    private String uid;
    private String nick;

    public User() {
    }

    public User(String uid, String nick) {
        this.uid = uid;
        this.nick = nick;
    }

    public String getUid() {
        return uid;
    }

    public String getNick() {
        return nick;
    }
}
