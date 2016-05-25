package com.rikachka.track_android_3_3.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rikachka.track_android_3_3.MainActivity;
import com.rikachka.track_android_3_3.Messages.Client.UserInfoData;
import com.rikachka.track_android_3_3.Messages.Message;
import com.rikachka.track_android_3_3.Messages.Client.SetUserInfoData;
import com.rikachka.track_android_3_3.R;

public class ChangeAboutFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_change, null);

        MainActivity mainActivity = (MainActivity)getActivity();

        TextView nickname = (TextView) view.findViewById(R.id.aboutNicknameEditText);
        nickname.setText(mainActivity.getNickname());

        final EditText status = (EditText) view.findViewById(R.id.aboutStatusEditText);
        Button aboutChange = (Button) view.findViewById(R.id.aboutButtonSave);

        aboutChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("1", status.getText().toString());
                if (!status.getText().equals("")) {
                    MainActivity mainActivity = (MainActivity)getActivity();
                    Message message = new Message("setuserinfo",
                            new SetUserInfoData(status.getText().toString(),
                                    mainActivity.getCid(),
                                    mainActivity.getSid()));
                    Gson gson = new Gson();
                    String msg = gson.toJson(message);
                    mainActivity.getMessageSocketService().sendMessage(msg);

                    message = new Message("userinfo", new UserInfoData(
                            mainActivity.getCid(),
                            mainActivity.getCid(),
                            mainActivity.getSid()));
                    gson = new Gson();
                    msg = gson.toJson(message);
                    mainActivity.getMessageSocketService().sendMessage(msg);

                    mainActivity.setStatus(status.getText().toString());
                }
            }
        });

        return view;
    }
}
