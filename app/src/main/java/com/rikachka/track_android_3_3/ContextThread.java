package com.rikachka.track_android_3_3;


import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

public class ContextThread extends Thread {
    private Socket socket;
    private MessageSocketService service;
    private final String address = "188.166.49.215";
//    private final String address = "localhost";
    private final Integer port = 7777;

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            service.setSocket(socket);
        } catch (IOException e) {
            //Невозможно подключиться
            e.printStackTrace();
        }
        if (socket != null) {
            while (true) {
                if (!socket.isConnected()) {
                    try {
                        socket.close();
                        socket = new Socket(address, port);
                        service.setSocket(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    DownloadMessage downloadMessage = new DownloadMessage(socket);
                    try {
                        String result = downloadMessage.execute().get();
                        try {
                            service.getResult(result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setService(MessageSocketService service) {
        this.service = service;
    }

}
