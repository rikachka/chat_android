package com.rikachka.track_android_3_3.Messages.Server.Responces;


import com.rikachka.track_android_3_3.Messages.Data;

public class RegistrationResponceData implements Data {
    private String status;
    private String error;

    public RegistrationResponceData() {
    }

    public RegistrationResponceData(String status, String error) {
        this.status = status;
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }
}
