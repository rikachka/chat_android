package com.rikachka.track_android_3_3.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rikachka.track_android_3_3.Classes.User;
import com.rikachka.track_android_3_3.MainActivity;
import com.rikachka.track_android_3_3.R;

public class AboutFragment extends Fragment {
    private String nick;
    private String sid;
    private String cid;
    private String user_id = "";
    private View aboutView;

    public void setInfo(String nick, String sid, String cid) {
        this.nick = nick;
        this.cid = cid;
        this.sid = sid;
    }

    public void setInfo(String user_id) {
        this.user_id = user_id;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, null);

        ((MainActivity)getActivity()).getMessageSocketService().getMessageHandler().setAboutFragment(this);

//        TextView nickTv = (TextView) view.findViewById(R.id.nick);
//        TextView cidTv = (TextView) view.findViewById(R.id.cid);
//        TextView sidTv = (TextView) view.findViewById(R.id.sid);
//
//        nickTv.setText(nick);
//        cidTv.setText(cid);
//        sidTv.setText(sid);

        Log.e("user_id", user_id);
        MainActivity mainActivity = (MainActivity)getActivity();
        TextView nickname = (TextView) view.findViewById(R.id.aboutNickname);
        TextView status = (TextView) view.findViewById(R.id.aboutStatus);
        Button aboutEdit = (Button) view.findViewById(R.id.aboutEdit);
        if (user_id.equals("") || user_id.equals(mainActivity.getCid())) {
            nickname.setText(mainActivity.getNickname());
            status.setText(mainActivity.getStatus());

            aboutEdit.setVisibility(View.VISIBLE);
            aboutEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frameLayout_main, new AboutChangeFragment());
                    ft.addToBackStack("about");
                    ft.commit();
                }
            });
        } else {
            User user = mainActivity.getMessageSocketService().getMessageHandler().getCurrentUser();
            nickname.setText(user.getNick());
            status.setText(user.getStatus());

            aboutEdit.setVisibility(View.INVISIBLE);
        }

        aboutView = view;
        return view;
    }

    public void refresh() {
        aboutView.invalidate();
        Log.e("1", "invalidate");
    }
}
