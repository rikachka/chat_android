package com.rikachka.track_android_3_3.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rikachka.track_android_3_3.MainActivity;
import com.rikachka.track_android_3_3.R;

public class AboutFragment extends Fragment {
    private String nick;
    private String sid;
    private String cid;

    public void setInfo(String nick, String sid, String cid) {
        this.nick = nick;
        this.cid = cid;
        this.sid = sid;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, null);

//        TextView nickTv = (TextView) view.findViewById(R.id.nick);
//        TextView cidTv = (TextView) view.findViewById(R.id.cid);
//        TextView sidTv = (TextView) view.findViewById(R.id.sid);
//
//        nickTv.setText(nick);
//        cidTv.setText(cid);
//        sidTv.setText(sid);

        MainActivity mainActivity = (MainActivity)getActivity();
        TextView nickname = (TextView) view.findViewById(R.id.aboutNickname);
        nickname.setText(mainActivity.getNickname());
        TextView status = (TextView) view.findViewById(R.id.aboutStatus);
        status.setText(mainActivity.getStatus());

        Button aboutEdit = (Button) view.findViewById(R.id.aboutEdit);

        aboutEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frameLayout_main, new ChangeAboutFragment());
                ft.addToBackStack("about");
                ft.commit();
            }
        });

        return view;
    }
}
