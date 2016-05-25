package com.rikachka.track_android_3_3.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rikachka.track_android_3_3.MessageSocketService;
import com.rikachka.track_android_3_3.Messages.Client.CreateChannelData;
import com.rikachka.track_android_3_3.Messages.Message;
import com.rikachka.track_android_3_3.R;

public class ChatCreateFragment extends Fragment {
    private MessageSocketService messageSocketService;
    private String cid;
    private String sid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_create, null);

        final EditText chatNameEditText = (EditText) view.findViewById(R.id.chat_name);
        final EditText chatDescrEditText = (EditText) view.findViewById(R.id.chat_descr);
        Button chatCreate = (Button) view.findViewById(R.id.chat_create);
        messageSocketService.setChatCreateFragment(this);

        chatCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!chatDescrEditText.getText().toString().equals("") || !chatNameEditText.getText().toString().equals("")) {
                    Message message = new Message("createchannel", new CreateChannelData(cid, sid,
                            chatNameEditText.getText().toString(),
                            chatDescrEditText.getText().toString()));
                    Gson gson = new Gson();
                    String mes = gson.toJson(message);
                    messageSocketService.sendMessage(mes);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void setMessageSocketService(MessageSocketService messageSocketService) {
        this.messageSocketService = messageSocketService;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setResult(final String error, String chid) {
        if (error.equals("OK")) {
            //Переход сразу по chid
//            getActivity().runOnUiThread(new Runnable() {
//                public void run() {
//                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
//                }
//            });
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout_main, new ChatFragment());
            ft.commit();
            getActivity().setTitle(R.string.title_chats);
        } else {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getActivity(), "No connection", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
