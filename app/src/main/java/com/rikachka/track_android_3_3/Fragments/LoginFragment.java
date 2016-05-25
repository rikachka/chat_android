package com.rikachka.track_android_3_3.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rikachka.track_android_3_3.MainActivity;
import com.rikachka.track_android_3_3.Messages.Client.LoginData;
import com.rikachka.track_android_3_3.Messages.Message;
import com.rikachka.track_android_3_3.MyActivity;
import com.rikachka.track_android_3_3.R;
import com.rikachka.track_android_3_3.SplashActivity;

public class LoginFragment extends Fragment {
    private Button login;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, null);

        final EditText loginEditView = (EditText) view.findViewById(R.id.emailEditView);
        final EditText passEditView = (EditText) view.findViewById(R.id.passwordEditView);

        login = (Button) view.findViewById(R.id.login);
        Button register = (Button) view.findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.frameLayout, new RegistrationFragment());
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();
            }
        });

        final SplashActivity splashActivity = (SplashActivity) getActivity();
        splashActivity.getMessageSocketService().setLoginFragment(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message("auth", new LoginData(loginEditView.getText().toString(),
                        passEditView.getText().toString()));
                splashActivity.saveText(loginEditView.getText().toString(),
                        passEditView.getText().toString());
//                Message message = new Message("auth", new LoginData("MY_LOGIN1", "MD5_FROM_PASS1"));
                Gson gson = new Gson();
                String jsonMessage = gson.toJson(message, Message.class);
                splashActivity.getMessageSocketService().sendMessage(jsonMessage);
            }
        });

        return view;
    }

    public void getResult(final String message) {
        Log.v("STATUS", message);
        if (message.equals("OK")) {
            try {
                getActivity().finish();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            SplashActivity splashActivity = (SplashActivity) getActivity();
            splashActivity.deleteText();
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    login.callOnClick();
                    Toast.makeText(getActivity(), "No connection", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

//public class LoginFragment extends Fragment {
//    private Button login;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.login_fragment, null);
//
//        final EditText loginEditView = (EditText) view.findViewById(R.id.emailEditView);
//        final EditText passEditView = (EditText) view.findViewById(R.id.passwordEditView);
//
//        login = (Button) view.findViewById(R.id.login);
//        Button register = (Button) view.findViewById(R.id.register);
//
//        register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.add(R.id.frameLayout, new RegistrationFragment());
//                fragmentTransaction.addToBackStack("");
//                fragmentTransaction.commit();
//            }
//        });
//
//        final MyActivity myActivity = (MyActivity) getActivity();
//        myActivity.getMessageSocketService().setLoginFragment(this);
//
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Message message = new Message("auth", new LoginData(loginEditView.getText().toString(),
//                        passEditView.getText().toString()));
//                saveText(loginEditView.getText().toString(),
//                        passEditView.getText().toString());
////                Message message = new Message("auth", new LoginData("MY_LOGIN1", "MD5_FROM_PASS1"));
//                Gson gson = new Gson();
//                String jsonMessage = gson.toJson(message, Message.class);
//                myActivity.getMessageSocketService().sendMessage(jsonMessage);
//            }
//        });
//
//        return view;
//    }
//
//    public void getResult(final String message) {
//        Log.v("STATUS", message);
//        if (message.equals("OK")) {
//            try {
//                getActivity().finish();
//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                startActivity(intent);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            MyActivity myActivity = (MyActivity) getActivity();
//            deleteText();
//            getActivity().runOnUiThread(new Runnable() {
//                public void run() {
//                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//                    login.callOnClick();
//                    Toast.makeText(getActivity(), "Соединение прервано", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
//
//    public void saveText(String login, String password) {
//        SharedPreferences sPref = getActivity().getPreferences(Context.MODE_PRIVATE);
//        SharedPreferences.Editor ed = sPref.edit();
//        ed.putString("LOGIN", login);
//        ed.putString("PASS", password);
//        ed.commit();
//    }
//
//    public void deleteText() {
//        SharedPreferences sPref = getActivity().getPreferences(Context.MODE_PRIVATE);
//        SharedPreferences.Editor ed = sPref.edit();
//        ed.remove("LOGIN");
//        ed.remove("PASS");
//        ed.commit();
//    }
//}
