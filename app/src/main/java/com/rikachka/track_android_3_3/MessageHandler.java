package com.rikachka.track_android_3_3;

import android.util.Log;

import com.google.gson.Gson;
import com.rikachka.track_android_3_3.Fragments.AboutFragment;
import com.rikachka.track_android_3_3.Fragments.ChannelFragment;
import com.rikachka.track_android_3_3.Messages.*;
import com.rikachka.track_android_3_3.Classes.*;
import com.rikachka.track_android_3_3.Messages.Client.UserInfoData;
import com.rikachka.track_android_3_3.Messages.Server.Events.EvEnterResponceData;
import com.rikachka.track_android_3_3.Messages.Server.Responces.ChannelListResponceData;
import com.rikachka.track_android_3_3.Messages.Server.Responces.CreateChannelResponceData;
import com.rikachka.track_android_3_3.Messages.Server.Responces.EnterResponceData;
import com.rikachka.track_android_3_3.Messages.Server.Events.EvMessageResponceData;
import com.rikachka.track_android_3_3.Messages.Server.Responces.LoginResponceData;
import com.rikachka.track_android_3_3.Messages.Server.Responces.RegistrationResponceData;
import com.rikachka.track_android_3_3.Messages.Server.Responces.SetUserInfoResponceData;
import com.rikachka.track_android_3_3.Messages.Server.Responces.UserInfoResponceData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MessageHandler {
    private AuthData authData = null;
    private MessageSocketService messageSocketService;
    private List<Channel> channels;
    private List<LastMsg> messages;
    private Map<String, User> users;
    private Channel currentChannel;
    private User currentUser;
    private String currentUserId;

    private ChannelFragment channelFragment;
    private AboutFragment aboutFragment;

    public MessageHandler(MessageSocketService messageSocketService) {
        this.messageSocketService = messageSocketService;
    }

    public void handle(Message message) {
        if (message != null) {
            String action = message.getAction();
            switch (action) {
                case "register":
                    RegistrationResponceData registrationResponceData = (RegistrationResponceData) message.getData();
                    messageSocketService.getRegistrationFragment().getResult(registrationResponceData.getError());
                    break;
                case "auth":
                    LoginResponceData loginResponceData = (LoginResponceData) message.getData();
                    if (loginResponceData.getStatus().equals("0")) {
                        authData = new AuthData(loginResponceData.getSid(),
                                loginResponceData.getCid(),
                                loginResponceData.getNick());
                    }
                    messageSocketService.getLoginFragment().getResult(loginResponceData.getError());
                    break;
                case "userinfo":
                    UserInfoResponceData userInfoResponceData = (UserInfoResponceData) message.getData();
                    currentUser = new User("",
                            userInfoResponceData.getNick(),
                            userInfoResponceData.getUserStatus());

                    aboutFragment.refresh();
//                    messageSocketService.getMainActivity().setStatus(userInfoResponceData.getUserStatus());
                    break;
                case "channellist":
                    ChannelListResponceData channelListResponceData = (ChannelListResponceData) message.getData();
                    channels = channelListResponceData.getChannels();
                    break;
                case "createchannel":
                    CreateChannelResponceData createChannelResponceData = (CreateChannelResponceData) message.getData();
                    messageSocketService.getChatCreateFragment().setResult(createChannelResponceData.getError(),
                            createChannelResponceData.getChid());
                    break;
                case "setuserinfo":
                    SetUserInfoResponceData setUserInfoResponceData = (SetUserInfoResponceData) message.getData();
                    messageSocketService.getMainActivity().makeToastWithText(setUserInfoResponceData.getError());
                    break;
                case "enter":
                    EnterResponceData enterResponceData = (EnterResponceData) message.getData();
                    messages = enterResponceData.getLastMsg();

//                    List<User> users_list = enterResponceData.getUsers();
//                    Iterator<User> users_iterator = users_list.iterator();
//                    users = new HashMap<>();
//                    while(users_iterator.hasNext()) {
//                        User next = users_iterator.next();
//                        users.put(next.getUid(), next);
//                    }
                    break;
                case "ev_message":
                    EvMessageResponceData evMessageResponceData = (EvMessageResponceData) message.getData();
                    if (evMessageResponceData.getChid().equals(currentChannel.getChid())) {
                        messages.add(new LastMsg("0", evMessageResponceData.getFrom(),
                                evMessageResponceData.getNick(),
                                evMessageResponceData.getBody(),
                                new SimpleDateFormat("hh:mm").format(new Date())));
                        channelFragment.refresh();

                        currentUserId = evMessageResponceData.getFrom();
                        MainActivity mainActivity = messageSocketService.getMainActivity();
                        Message new_message = new Message("userinfo",
                                new UserInfoData(currentUserId, mainActivity.getCid(), mainActivity.getSid()));
                        Gson gson = new Gson();
                        String msg = gson.toJson(new_message);
                        messageSocketService.sendMessage(msg);

                    }
                    break;
                case "ev_enter":
                    EvEnterResponceData evEnterResponceData = (EvEnterResponceData) message.getData();
                    if (evEnterResponceData.getChid().equals(currentChannel.getChid())) {
//                        users.put(evEnterResponceData.getUid(),
//                                evEnterResponceData.getNick());

                    }
                    break;
            }
        }
    }

    public AuthData getAuthData() {
        return authData;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setAuthData(AuthData authData) {
        this.authData = authData;
    }

    public List<LastMsg> getMessages() {
        return messages;
    }

    public void setChannel(Channel channel) {
        this.currentChannel = channel;
    }

    public void setChannelFragment(ChannelFragment channelFragment) {
        this.channelFragment = channelFragment;
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public User getCurrentUser() {
        Log.e("1", currentUser.getNick());
        Log.e("1", currentUser.getStatus());
        return currentUser;
    }

    public void setAboutFragment(AboutFragment aboutFragment) {
        this.aboutFragment = aboutFragment;
    }
}
