package com.rikachka.track_android_3_3.Fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rikachka.track_android_3_3.Classes.Channel;
import com.rikachka.track_android_3_3.Classes.LastMsg;
import com.rikachka.track_android_3_3.MainActivity;
import com.rikachka.track_android_3_3.Messages.Client.EnterData;
import com.rikachka.track_android_3_3.Messages.Client.LeaveData;
import com.rikachka.track_android_3_3.Messages.Client.UserInfoData;
import com.rikachka.track_android_3_3.Messages.Message;
import com.rikachka.track_android_3_3.Messages.Client.MessageData;
import com.rikachka.track_android_3_3.R;

import java.util.ArrayList;
import java.util.List;

public class ChannelFragment extends Fragment implements View.OnClickListener {
    private final String LOG_TAG = getClass().getSimpleName();
    private ListView msgListView;
    private EditText msg_edittext;
    private List<LastMsg> chatlist;
    private static ChatAdapter chatAdapter;
    private View view;

    private String sid;
    private String cid;
    private Channel channel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.channel_fragment, null);

        getActivity().setTitle(channel.getName());

        msg_edittext = (EditText) view.findViewById(R.id.messageEditText);
        msgListView = (ListView) view.findViewById(R.id.msgListView);
        ImageButton sendButton = (ImageButton) view
                .findViewById(R.id.sendMessageButton);
        sendButton.setOnClickListener(this);

        msgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        msgListView.setStackFromBottom(true);

        ((MainActivity)getActivity()).getMessageSocketService().getMessageHandler().setChannelFragment(this);

        chatlist = new ArrayList<>();

//        chatlist.add(new MessageData("1", "1", "1", "1"));
//        chatlist.add(new MessageData("1", "1", "1", "2"));
//        chatlist.add(new MessageData("1", "1", "1", "3"));
//        chatlist.add(new MessageData("2", "1", "1", "4"));
//        chatlist.add(new MessageData("1", "1", "1", "5"));
//        chatlist.add(new MessageData("2", "1", "1", "6"));
//        chatlist.add(new MessageData("1", "1", "1", "7"));
//        chatlist.add(new MessageData("1", "1", "1", "8"));
//        chatlist.add(new MessageData("1", "1", "1", "9"));
//        chatlist.add(new MessageData("1", "1", "1", "10"));
//        chatlist.add(new MessageData("1", "1", "1", "1"));
//        chatlist.add(new MessageData("1", "1", "1", "2"));
//        chatlist.add(new MessageData("1", "1", "1", "3"));
//        chatlist.add(new MessageData("2", "1", "1", "4asdfsadsdfasadf"));
//        chatlist.add(new MessageData("1", "1", "1", "5"));
//        chatlist.add(new MessageData("2", "1", "1", "6"));
//        chatlist.add(new MessageData("1", "1", "1", "7"));
//        chatlist.add(new MessageData("1", "1", "1", "8asdfafsdsdf"));
//        chatlist.add(new MessageData("1", "1", "1", "9"));
//        chatlist.add(new MessageData("1", "1", "1", "10"));
//        chatlist.add(new MessageData("1", "1", "1", "1"));
//        chatlist.add(new MessageData("1", "1", "1", "2"));
//        chatlist.add(new MessageData("1", "1", "1", "3"));
//        chatlist.add(new MessageData("2", "1", "1", "4sadfasdfsfd"));
//        chatlist.add(new MessageData("1", "1", "1", "5asdfdsfasdf5asdfdsfasdf5asdfdsfasdf5asdfdsfasdf5asdfdsfasdf5asdfdsfasdf5asdfdsfasdf5asdfdsfasdf5asdfdsfasdf5asdfdsfasdf5asdfdsfasdf5asdfdsfasdf5asdfdsfasdf5asdfdsfasdf5asdfdsfasdf"));
//        chatlist.add(new MessageData("2", "1", "1", "10101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010"));
//        chatlist.add(new MessageData("1", "1", "1", "7"));
//        chatlist.add(new MessageData("1", "1", "1", "8"));
//        chatlist.add(new MessageData("1", "1", "1", "9"));
//        chatlist.add(new MessageData("1", "1", "1", "1010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010"));

        Log.v(LOG_TAG, "SEND TO REFRESH");
        Message message = new Message("enter", new EnterData(cid, sid, channel.getChid()));
        Gson gson = new Gson();
        String msg = gson.toJson(message);
        ((MainActivity)getActivity()).getMessageSocketService().sendMessage(msg);
        ((MainActivity)getActivity()).getMessageSocketService().getMessageHandler().setChannel(channel);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                chatAdapter = new ChatAdapter(getActivity(), ((MainActivity)getActivity()).getMessageSocketService().getMessageHandler().getMessages());
                msgListView.setAdapter(chatAdapter);
            }
        }, 400);

        chatAdapter = new ChatAdapter(getActivity(), chatlist);
        msgListView.setAdapter(chatAdapter);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendMessageButton:
                if (!msg_edittext.getText().toString().equals("")) {
                    Message message = new Message("message",
                            new MessageData(cid, sid, channel.getChid(), msg_edittext.getText().toString()));
                    Gson gson = new Gson();
                    String msg = gson.toJson(message);
                    ((MainActivity)getActivity()).getMessageSocketService().sendMessage(msg);
                    msg_edittext.setText("");
                }
        }
    }

    public class ChatAdapter extends BaseAdapter {

        private LayoutInflater inflater = null;
        ArrayList<LastMsg> chatMessageList;

        public void setChatMessageList(ArrayList<LastMsg> chatMessageList) {
            this.chatMessageList = chatMessageList;
        }

        public ChatAdapter(Activity activity, List<LastMsg> list) {
            chatMessageList = (ArrayList<LastMsg>) list;
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            try {
                return chatMessageList.size();
            } catch (NullPointerException e) {
                Log.e("ChatAdapter", "NullPointerException");
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LastMsg message = chatMessageList.get(position);
            View vi = convertView;
            if (convertView == null)
                vi = inflater.inflate(R.layout.chatbubble, null);

            TextView msg = (TextView) vi.findViewById(R.id.message_text);
            TextView num_message = (TextView) vi.findViewById(R.id.num_message);

            LinearLayout layout = (LinearLayout) vi
                    .findViewById(R.id.bubble_layout);
            LinearLayout parent_layout = (LinearLayout) vi
                    .findViewById(R.id.bubble_layout_parent);
            LinearLayout person_left = (LinearLayout) vi.findViewById(R.id.person_left_layout);
            LinearLayout person_right = (LinearLayout) vi.findViewById(R.id.person_right_layout);
            num_message.setText("Message №" + (position+1));

            // if message is mine then align to right
//            if (position % 2 == 0) {
            if (message.getFrom().equals(cid)) {
//                layout.setBackgroundResource(R.drawable.bubble_7);
                msg.setText(message.getBody());
                parent_layout.setGravity(Gravity.RIGHT);
                person_left.setVisibility(View.INVISIBLE);
                person_right.setVisibility(View.VISIBLE);
            } else {
//                layout.setBackgroundResource(R.drawable.bubble_5);
                msg.setText(message.getBody());
                parent_layout.setGravity(Gravity.LEFT);
                person_left.setVisibility(View.VISIBLE);
                person_right.setVisibility(View.INVISIBLE);
            }
            msg.setTextColor(Color.BLACK);

            TextView time1 = (TextView) vi.findViewById(R.id.time1);
            time1.setText(message.getTime());
            TextView time2 = (TextView) vi.findViewById(R.id.time2);
            time2.setText(message.getTime());

            ImageButton person_left_image = (ImageButton) vi.findViewById(R.id.person_left_image);
            person_left_image.setOnClickListener(goToProfile);
            person_left_image.setTag(message.getFrom());
            ImageButton person_right_image = (ImageButton) vi.findViewById(R.id.person_right_image);
            person_right_image.setOnClickListener(goToProfile);
            person_right_image.setTag(message.getFrom());

            return vi;
        }

        public void add(LastMsg object) {
            chatMessageList.add(object);
        }

        View.OnClickListener goToProfile = new View.OnClickListener() {
            public void onClick(View v) {
                String sender = (String) v.getTag();

                Message message = new Message("userinfo", new UserInfoData(sender, cid, sid));
                Gson gson = new Gson();
                String msg = gson.toJson(message);
                ((MainActivity)getActivity()).getMessageSocketService().sendMessage(msg);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                AboutFragment aboutFragment = new AboutFragment();
                aboutFragment.setInfo(sender);
                ft.replace(R.id.frameLayout_main, aboutFragment);
                ft.addToBackStack("channel");
                ft.commit();
                getActivity().setTitle(R.string.title_profile);
            }
        };
    }



    @Override
    public void onDestroy() {
        leaveChannel();
        super.onDestroy();
    }

    private void leaveChannel() {
        Log.v(LOG_TAG, "LEAVE");
        Message message = new Message("leave", new LeaveData(cid, sid, channel.getChid()));
        Gson gson = new Gson();
        String msg = gson.toJson(message);
        ((MainActivity)getActivity()).getMessageSocketService().sendMessage(msg);
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void refresh() {
//        chatAdapter = new ChatAdapter(getActivity(), ((MainActivity) getActivity()).getMessageSocketService().getMessageHandler().getMessages());
//        msgListView.setAdapter(chatAdapter);


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chatlist = ((MainActivity)getActivity()).getMessageSocketService().getMessageHandler().getMessages();
                chatAdapter.setChatMessageList((ArrayList<LastMsg>)chatlist);
                chatAdapter.notifyDataSetInvalidated();
                msgListView.deferNotifyDataSetChanged();
                msgListView.invalidateViews();
            }
        });
    }
}
