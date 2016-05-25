package com.rikachka.track_android_3_3;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.rikachka.track_android_3_3.Classes.Channel;
import com.rikachka.track_android_3_3.Classes.LastMsg;
import com.rikachka.track_android_3_3.Classes.User;
import com.rikachka.track_android_3_3.Fragments.*;
import com.rikachka.track_android_3_3.Messages.Server.Responces.ChannelListResponceData;
import com.rikachka.track_android_3_3.Messages.Server.Responces.CreateChannelResponceData;
import com.rikachka.track_android_3_3.Messages.Server.Responces.EnterResponceData;
import com.rikachka.track_android_3_3.Messages.Server.Events.EvMessageResponceData;
import com.rikachka.track_android_3_3.Messages.Server.Responces.LoginResponceData;
import com.rikachka.track_android_3_3.Messages.Message;
import com.rikachka.track_android_3_3.Messages.Server.Responces.RegistrationResponceData;
import com.rikachka.track_android_3_3.Messages.Server.Responces.SetUserInfoResponceData;
import com.rikachka.track_android_3_3.Messages.Server.Responces.UserInfoResponceData;
import com.rikachka.track_android_3_3.Messages.Server.Responces.WelcomeResponceData;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MessageSocketService extends Service {
    private final String LOG_TAG = getClass().getSimpleName();
    // Сокет для получения и отправки данных.
    private Socket socket = null;
    private MyBinder binder = new MyBinder();
    private MessageHandler messageHandler;
    private LoginFragment loginFragment;
    private RegistrationFragment registrationFragment;
    private ChatCreateFragment chatCreateFragment;
    private Gson gson;
    private MainActivity mainActivity;

    public MessageSocketService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        messageHandler = new MessageHandler(this);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Message.class, new MessageDeserializer());
        gson = gsonBuilder.create();


        //Поток получения данных.
        ContextThread contextThread = new ContextThread();
        contextThread.setSocket(socket);
        contextThread.setService(this);
        contextThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOG_TAG, LOG_TAG + " запускается");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        Log.i(LOG_TAG, LOG_TAG + " завершен");
        super.onDestroy();
    }

    public void sendMessage(final String message) {
        try {
            if (socket != null) {
                socket.getOutputStream().write(message.getBytes());
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            socket.getOutputStream().write(message.getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, 750);
            }
        } catch (IOException e) {
            try {
                Toast.makeText(getApplicationContext(), "No connection. Try again later.", Toast.LENGTH_SHORT).show();
                Log.e(LOG_TAG, e.getMessage());
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void getResult(String result) {
        Log.e(LOG_TAG, result);
        String[] res = result.split("\\}\\}\\{");
        if (res.length == 1) {
            Message message = gson.fromJson(result, Message.class);
            messageHandler.handle(message);
        } else {
            Message message = gson.fromJson(res[0]+"}}", Message.class);
            Log.e(LOG_TAG, res[0]+"}}");
            messageHandler.handle(message);
            Message message1 = gson.fromJson("{"+res[1], Message.class);
            messageHandler.handle(message1);
            Log.e(LOG_TAG, "{"+res[1]);
        }
    }

    class MyBinder extends Binder {
        MessageSocketService getService() {
            return MessageSocketService.this;
        }
    }

    private class MessageDeserializer implements JsonDeserializer<Message> {
        public Message deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            Message message = null;
            String string0 = null, string1 = null, string2 = null, string3 = null, string4 = null, string5 = null;
            JsonObject jsonObject = json.getAsJsonObject();
            JsonObject data = jsonObject.getAsJsonObject("data");

            String action = jsonObject.get("action").getAsString();
            switch (action) {
                case "welcome" :
                    message = new Message(action, new WelcomeResponceData());
                    break;
                case "register":
                    string0 = data.get("status").getAsString();
                    string1 = data.get("error").getAsString();
                    message = new Message(action, new RegistrationResponceData(string0, string1));
                    break;
                case "auth":
                    string0 = data.get("status").getAsString();
                    string1 = data.get("error").getAsString();

                    if (data.has("sid")) {
                        string2 = data.get("sid").getAsString();
                        string3 = data.get("cid").getAsString();
                        string4 = data.get("nick").getAsString();
                    } else {
                        string2 = "";
                        string3 = "";
                        string4 = "";
                    }
                    message = new Message(action, new LoginResponceData(string0, string1, string2, string3, string4));
                    break;
                case "userinfo":
                    string0 = data.get("status").getAsString();
                    string1 = data.get("error").getAsString();

                    if (data.has("nick")) {
                        string2 = data.get("nick").getAsString();
                        string3 = data.get("user_status").getAsString();
                    } else {
                        string2 = "";
                        string3 = "";
                    }
                    message = new Message(action, new UserInfoResponceData(string0, string1, string2, string3));
                    break;
                case "channellist":
                    string0 = data.get("status").getAsString();
                    string1 = data.get("error").getAsString();
                    List<Channel> channels = null;
                    if (data.has("channels")) {
                        channels = new ArrayList<>();
                        JsonArray jsonArray = data.getAsJsonArray("channels");
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject asJsonObject = jsonArray.get(i).getAsJsonObject();
                            Channel channel = new Channel(asJsonObject.get("chid").getAsString(),
                                    asJsonObject.get("name").getAsString(),
                                    asJsonObject.get("descr").getAsString(),
                                    asJsonObject.get("online").getAsString());
                            channels.add(channel);
                        }
                    }
                    message = new Message(action, new ChannelListResponceData(string0, string1, channels));
                    break;
                case "createchannel":
                    string0 = data.get("status").getAsString();
                    string1 = data.get("error").getAsString();
                    if (data.has("chid")) {
                        string2 = data.get("chid").getAsString();
                    } else {
                        string2 = "";
                    }
                    message = new Message(action, new CreateChannelResponceData(string0, string1, string2));
                    break;
                case "setuserinfo":
                    string0 = data.get("status").getAsString();
                    string1 = data.get("error").getAsString();
                    message = new Message(action, new SetUserInfoResponceData(string0, string1));
                    break;
                case "enter":
                    string0 = data.get("status").getAsString();
                    string1 = data.get("error").getAsString();
                    List<LastMsg> lastMsgs = null;
                    if (data.has("last_msg")) {
                        lastMsgs = new ArrayList<>();
                        JsonArray jsonArray = data.getAsJsonArray("last_msg");
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject asJsonObject = jsonArray.get(i).getAsJsonObject();
                            LastMsg lastMsg = new LastMsg(asJsonObject.get("mid").getAsString(),
                                    asJsonObject.get("from").getAsString(),
                                    asJsonObject.get("nick").getAsString(),
                                    asJsonObject.get("body").getAsString(),
                                    asJsonObject.get("time").getAsString());
                            lastMsgs.add(lastMsg);
                        }
                    }
                    List<User> users = null;
                    if (data.has("users")) {
                        users = new ArrayList<>();
                        JsonArray jsonArray = data.getAsJsonArray("users");
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject asJsonObject = jsonArray.get(i).getAsJsonObject();
                            User user = new User(asJsonObject.get("uid").getAsString(),
                                    asJsonObject.get("nick").getAsString());
                            users.add(user);
                        }
                    }
                    message = new Message(action, new EnterResponceData(string0, string1, users, lastMsgs));
                    break;
                case "ev_message":
                    string0 = data.get("chid").getAsString();
                    string1 = data.get("from").getAsString();
                    string2 = data.get("nick").getAsString();
                    string3 = data.get("body").getAsString();
                    message = new Message(action, new EvMessageResponceData(string0, string1, string2, string3));
                    break;
            }
            return message;
        }
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public void setLoginFragment(LoginFragment loginFragment) {
        this.loginFragment = loginFragment;
    }

    public LoginFragment getLoginFragment() {
        return loginFragment;
    }

    public RegistrationFragment getRegistrationFragment() {
        return registrationFragment;
    }

    public void setRegistrationFragment(RegistrationFragment registrationFragment) {
        this.registrationFragment = registrationFragment;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ChatCreateFragment getChatCreateFragment() {
        return chatCreateFragment;
    }

    public void setChatCreateFragment(ChatCreateFragment chatCreateFragment) {
        this.chatCreateFragment = chatCreateFragment;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
}
