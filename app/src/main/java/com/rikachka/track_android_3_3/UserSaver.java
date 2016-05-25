package com.rikachka.track_android_3_3;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.rikachka.track_android_3_3.Messages.Client.LoginData;
import com.rikachka.track_android_3_3.Messages.Message;

/**
 * Created by rikachka on 24/05/16.
 */
public class UserSaver {
//    private SharedPreferences sPref;
//    private MyActivity myActivity
//
//    public void saveText(String login, String password) {
//        sPref = getPreferences(MODE_PRIVATE);
//        SharedPreferences.Editor ed = sPref.edit();
//        ed.putString("LOGIN", login);
//        ed.putString("PASS", password);
//        ed.commit();
//    }
//
//    public void deleteText() {
//        sPref = getPreferences(MODE_PRIVATE);
//        SharedPreferences.Editor ed = sPref.edit();
//        ed.remove("LOGIN");
//        ed.remove("PASS");
//        ed.commit();
//    }
//
//    private void loadText() {
//        sPref = getPreferences(MODE_PRIVATE);
//        String login = sPref.getString("LOGIN", "");
//        String pass = sPref.getString("PASS", "");
//        if (!login.equals("") || !pass.equals("")) {
//            Message message = new Message("auth", new LoginData(login, pass));
//            Gson gson = new Gson();
//            String jsonMessage = gson.toJson(message, Message.class);
//            getMessageSocketService().sendMessage(jsonMessage);
//        }
//    }
}
