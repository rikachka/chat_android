package com.rikachka.track_android_3_3.Messages.Server.Responces;

import com.rikachka.track_android_3_3.Classes.Channel;
import com.rikachka.track_android_3_3.Messages.Data;

import java.util.List;

public class ChannelListResponceData implements Data {
    private String status;
    private String error;
    private List<Channel> channels;

    public ChannelListResponceData() {
    }

    public ChannelListResponceData(String status, String error, List<Channel> channels) {
        this.status = status;
        this.error = error;
        this.channels = channels;
    }

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public List<Channel> getChannels() {
        return channels;
    }
}
