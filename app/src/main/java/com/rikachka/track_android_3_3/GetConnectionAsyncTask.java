package com.rikachka.track_android_3_3;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.Socket;

//Класс для подключения к сокету.
public class GetConnectionAsyncTask extends AsyncTask<Void, Void, Socket> {

    private final String address = "188.166.49.215";
    private final Integer port = 7777;

    @Override
    protected Socket doInBackground(Void... params) {
        Socket socket = null;
        try {
            socket = new Socket(address, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return socket;
    }
}
