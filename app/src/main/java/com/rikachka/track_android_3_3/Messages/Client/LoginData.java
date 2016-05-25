package com.rikachka.track_android_3_3.Messages.Client;

import com.rikachka.track_android_3_3.Messages.Data;

public class LoginData implements Data {
    private String login;
    private String pass;

    public LoginData(String text) {

    }

    public LoginData(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }
}
