package com.rikachka.track_android_3_3;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rikachka.track_android_3_3.Fragments.LoginFragment;
import com.rikachka.track_android_3_3.Fragments.SplashFragment;
import com.rikachka.track_android_3_3.Messages.Client.LoginData;
import com.rikachka.track_android_3_3.Messages.Message;

public class SplashActivity extends AppCompatActivity implements MyActivity {
    private final String LOG_TAG = getClass().getSimpleName();
    private ServiceConnection serviceConnection;
    private MessageSocketService messageSocketService;
    private boolean bind = false;
    private FragmentTransaction fragmentTransaction;
    private SharedPreferences sPref;

    public MessageSocketService getMessageSocketService() {
        return messageSocketService;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        deleteText();

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
        setContentView(R.layout.activity_splash);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, new SplashFragment());
        fragmentTransaction.commit();

        //Меняем состояние подключения.
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.v(LOG_TAG, "connected");
                messageSocketService = ((MessageSocketService.MyBinder) service).getService();
//                System.out.println("YEP");
                loadText();
                bind = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.v(LOG_TAG, "disconnected");
                bind = false;
            }
        };

        if (serviceConnection == null) {
            Log.v(LOG_TAG, "service connection is null");
        }

        bind = bindService(new Intent(this, MessageSocketService.class), serviceConnection, 0);
        if (!bind) {
            Log.e(LOG_TAG, "Can't bind service");
        }

        if (!chechConnection()) {
            finishActivity();
        } else {
            startActivity(2000);
        }
    }

    // Проверка интернет соединения. Если его нет - закрываем приложение через 5 сек.
    private boolean chechConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            Toast disconnectedToast = Toast.makeText(getApplicationContext(), "No network connection. Good bye!", Toast.LENGTH_LONG);
            disconnectedToast.show();
            return false;
        }
    }

    private void finishActivity() {
        Thread timer = new Thread() {
            public void run() {
                try {
                    int logoTimer = 0;
                    while (logoTimer < 5000) {
                        sleep(100);
                        logoTimer = logoTimer + 100;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }
                System.exit(1);
            }
        };
        timer.start();
    }

    private void startActivity(final Integer num) {
        Thread timer = new Thread()
        {
            public void run()
            {
                try
                {
                    int logoTimer = 0;
                    while(logoTimer < num) {
                        sleep(100);
                        logoTimer = logoTimer +100;
                    };
                    if (messageSocketService.getMessageHandler().getAuthData() == null) {
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.add(R.id.frameLayout, new LoginFragment());
                        fragmentTransaction.commit();
                    } else {
                        finish();
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {

                }
            }
        };
        timer.start();
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }

    public void saveText(String login, String password) {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("LOGIN", login);
        ed.putString("PASS", password);
        ed.commit();
    }

    public void deleteText() {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.remove("LOGIN");
        ed.remove("PASS");
        ed.commit();
    }

    private void loadText() {
        sPref = getPreferences(MODE_PRIVATE);
        String login = sPref.getString("LOGIN", "");
        String pass = sPref.getString("PASS", "");
        if (!login.equals("") || !pass.equals("")) {
            Message message = new Message("auth", new LoginData(login, pass));
            Gson gson = new Gson();
            String jsonMessage = gson.toJson(message, Message.class);
            getMessageSocketService().sendMessage(jsonMessage);
        }
    }
}

