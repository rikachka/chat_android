package com.rikachka.track_android_3_3.Classes;

public class AuthData {
    private String sid;
    private String cid;
    private String nick;

    public AuthData(String sid, String cid, String nick) {
        this.sid = sid;
        this.cid = cid;
        this.nick = nick;
    }

    public String getSid() {
        return sid;
    }

    public String getCid() {
        return cid;
    }

    public String getNick() {
        return nick;
    }
}
