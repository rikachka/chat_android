package com.rikachka.track_android_3_3.Messages.Client;

import com.rikachka.track_android_3_3.Classes.User;
import com.rikachka.track_android_3_3.Messages.Data;

public class UserInfoData implements Data {
    private String user;
    private String cid;
    private String sid;

    public UserInfoData() {
    }

    public UserInfoData(String user, String cid, String sid) {
        this.user = user;
        this.cid = cid;
        this.sid = sid;
    }

    public String getUser() {
        return user;
    }

    public String getCid() {
        return cid;
    }

    public String getSid() {
        return sid;
    }
}
