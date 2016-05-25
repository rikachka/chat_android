package com.rikachka.track_android_3_3.Messages.Server.Events;

import com.rikachka.track_android_3_3.Messages.Data;

public class EvEnterResponceData implements Data {
    private String chid;
    private String uid;
    private String nick;

    public EvEnterResponceData() {
    }

    public EvEnterResponceData(String chid, String uid, String nick) {
        this.chid = chid;
        this.uid = uid;
        this.nick = nick;
    }

    public String getChid() {
        return chid;
    }

    public String getUid() {
        return uid;
    }

    public String getNick() {
        return nick;
    }
}
