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
import android.widget.Toast;

import com.google.gson.Gson;
import com.rikachka.track_android_3_3.Messages.Message;
import com.rikachka.track_android_3_3.Messages.Client.RegistrationData;
import com.rikachka.track_android_3_3.MyActivity;
import com.rikachka.track_android_3_3.R;
import com.rikachka.track_android_3_3.SplashActivity;

public class RegistrationFragment extends Fragment {
    private Button register;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, null);

        final EditText email = (EditText) view.findViewById(R.id.email);
        final EditText password = (EditText) view.findViewById(R.id.password);
        final EditText nick = (EditText) view.findViewById(R.id.nickname);

        final MyActivity myActivity = (MyActivity) getActivity();
        myActivity.getMessageSocketService().setRegistrationFragment(this);
        register = (Button) view.findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message("register", new RegistrationData(email.getText().toString(),
                        password.getText().toString(), nick.getText().toString()));
                Gson gson = new Gson();
                String jsonMessage = gson.toJson(message, Message.class);
                myActivity.getMessageSocketService().sendMessage(jsonMessage);
            }
        });

        return view;
    }

    public void getResult(final String message) {
        Log.v("STATUS", message);
        if (message.equals("OK")) {
            try {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    register.callOnClick();
                    Toast.makeText(getActivity(), "No connection", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
