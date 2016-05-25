package com.rikachka.track_android_3_3.Messages.Client;

import com.rikachka.track_android_3_3.Messages.Data;

public class SetUserInfoData implements Data {
    private String user_status;
    private String cid;
    private String sid;

    public SetUserInfoData() {

    }

    public SetUserInfoData(String user_status, String cid, String sid) {
        this.user_status = user_status;
        this.cid = cid;
        this.sid = sid;
    }

    public String getUser_status() {
        return user_status;
    }

    public String getCid() {
        return cid;
    }

    public String getSid() {
        return sid;
    }
}
