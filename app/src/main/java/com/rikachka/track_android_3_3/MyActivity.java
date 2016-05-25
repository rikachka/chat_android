package com.rikachka.track_android_3_3;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.rikachka.track_android_3_3.Messages.Client.LoginData;
import com.rikachka.track_android_3_3.Messages.Message;

/**
 * Created by rikachka on 24/05/16.
 */
public interface MyActivity {
    MessageSocketService getMessageSocketService();

    void saveText(String login, String password);

    void deleteText();
}
