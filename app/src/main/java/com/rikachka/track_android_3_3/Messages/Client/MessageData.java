package com.rikachka.track_android_3_3.Messages.Client;


import com.rikachka.track_android_3_3.Messages.Data;

public class MessageData implements Data {
    private String cid;
    private String sid;
    private String channel;
    private String body;

    public MessageData() {

    }

    public MessageData(String cid, String sid, String channel, String body) {
        this.cid = cid;
        this.sid = sid;
        this.channel = channel;
        this.body = body;
    }

    public String getCid() {
        return cid;
    }

    public String getSid() {
        return sid;
    }

    public String getChannel() {
        return channel;
    }

    public String getBody() {
        return body;
    }
}
