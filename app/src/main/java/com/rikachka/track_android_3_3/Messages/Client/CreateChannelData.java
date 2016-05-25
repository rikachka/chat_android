package com.rikachka.track_android_3_3.Messages.Client;

import com.rikachka.track_android_3_3.Messages.Data;

/**
 * Created by rikachka on 23/05/16.
 */
public class CreateChannelData implements Data {
    private String cid;
    private String sid;
    private String name;
    private String descr;

    public CreateChannelData() {
    }

    public CreateChannelData(String cid, String sid, String name, String descr) {
        this.cid = cid;
        this.sid = sid;
        this.name = name;
        this.descr = descr;
    }

    public String getCid() {
        return cid;
    }

    public String getSid() {
        return sid;
    }

    public String getName() {
        return name;
    }

    public String getDescr() {
        return descr;
    }
}
