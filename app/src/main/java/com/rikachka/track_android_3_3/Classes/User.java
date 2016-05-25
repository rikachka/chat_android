package com.rikachka.track_android_3_3.Classes;

public class User {
    private String uid;
    private String nick;
    private String status;

    public User() {
    }

    public User(String uid, String nick) {
        this.uid = uid;
        this.nick = nick;
    }

    public User(String uid, String nick, String status) {
        this.uid = uid;
        this.nick = nick;
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public String getNick() {
        return nick;
    }

    public String getStatus() {
        return status;
    }
}
