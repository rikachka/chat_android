package com.rikachka.track_android_3_3.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rikachka.track_android_3_3.Classes.Channel;
import com.rikachka.track_android_3_3.MainActivity;
import com.rikachka.track_android_3_3.MessageSocketService;
import com.rikachka.track_android_3_3.Messages.Client.ChannelListData;
import com.rikachka.track_android_3_3.Messages.Message;
import com.rikachka.track_android_3_3.R;

import java.util.List;

public class ChatFragment extends Fragment {

    private MessageSocketService messageSocketService;
    private String cid;
    private String sid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment, null);

        final RecyclerView rv = (RecyclerView) view.findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        final List<Channel> channels = ((MainActivity) getActivity()).getMessageSocketService().getMessageHandler().getChannels();

        final RVAdapter adapter = new RVAdapter(channels);
        rv.setAdapter(adapter);

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipereflesh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final MainActivity activity = (MainActivity) getActivity();
                Message message = new Message("channellist", new ChannelListData(activity.getCid(), activity.getSid()));
                Gson gson = new Gson();
                String mes = gson.toJson(message);
                activity.getMessageSocketService().sendMessage(mes);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RVAdapter adapter = null;
                        adapter = new RVAdapter(activity.getMessageSocketService().getMessageHandler().getChannels());
                        rv.setAdapter(adapter);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2500);
            }
        });

        Button chatAdd = (Button) view.findViewById(R.id.chatAdd);

        chatAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ChatCreateFragment chatCreateFragment = new ChatCreateFragment();
                chatCreateFragment.setMessageSocketService(messageSocketService);
                chatCreateFragment.setCid(cid);
                chatCreateFragment.setSid(sid);
                ft.replace(R.id.frameLayout_main, chatCreateFragment);
                ft.addToBackStack("chat");
                ft.commit();
                getActivity().setTitle(R.string.title_chat_create);
            }
        });

        return view;
    }

    public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {
        private List<Channel> channels;

        public RVAdapter(List<Channel> channels) {
            this.channels = channels;
        }

        @Override
        public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
            PersonViewHolder pvh = new PersonViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(PersonViewHolder holder, int position) {
            final Channel channel = channels.get(position);
            String info = channel.getName() + " (" + channel.getOnline() + ")";
            holder.personName.setText(info);
            holder.personAge.setText(channel.getDescr());
            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ChannelFragment channelFragment = new ChannelFragment();

                    channelFragment.setChannel(channel);
                    MainActivity mainActivity = ((MainActivity)getActivity());
                    channelFragment.setSid(mainActivity.getSid());
                    channelFragment.setCid(mainActivity.getCid());

                    ft.replace(R.id.frameLayout_main, channelFragment);
                    ft.addToBackStack("chat");
                    ft.commit();
                }
            });
        }

        @Override
        public int getItemCount() {
            return channels.size();
        }

        public class PersonViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            TextView personName;
            TextView personAge;
            ImageView personPhoto;

            PersonViewHolder(View itemView) {
                super(itemView);
                cv = (CardView) itemView.findViewById(R.id.cv);
                personName = (TextView) itemView.findViewById(R.id.person_name);
                personAge = (TextView) itemView.findViewById(R.id.person_age);
                personPhoto = (ImageView) itemView.findViewById(R.id.person_photo);
            }
        }
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
}
