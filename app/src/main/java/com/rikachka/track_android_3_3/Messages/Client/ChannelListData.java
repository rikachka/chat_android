package com.rikachka.track_android_3_3.Messages.Client;

import com.rikachka.track_android_3_3.Messages.Data;

public class ChannelListData implements Data {
    private String cid;
    private String sid;

    public ChannelListData() {

    }

    public ChannelListData(String cid, String sid) {
        this.cid = cid;
        this.sid = sid;
    }

    public String getCid() {
        return cid;
    }

    public String getSid() {
        return sid;
    }
}
