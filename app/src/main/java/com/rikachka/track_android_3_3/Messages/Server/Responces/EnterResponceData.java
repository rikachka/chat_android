package com.rikachka.track_android_3_3.Messages.Server.Responces;

import com.rikachka.track_android_3_3.Classes.LastMsg;
import com.rikachka.track_android_3_3.Classes.User;
import com.rikachka.track_android_3_3.Messages.Data;

import java.util.List;

public class EnterResponceData implements Data {
    private String status;
    private String error;
    private List<User> users;
    private List<LastMsg> last_msg;

    public EnterResponceData() {
    }

    public EnterResponceData(String status, String error, List<User> users, List<LastMsg> last_msg) {
        this.status = status;
        this.error = error;
        this.users = users;
        this.last_msg = last_msg;
    }

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<LastMsg> getLast_msg() {
        return last_msg;
    }
}
