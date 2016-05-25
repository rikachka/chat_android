package com.rikachka.track_android_3_3;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rikachka.track_android_3_3.Fragments.AboutChangeFragment;
import com.rikachka.track_android_3_3.Fragments.AboutFragment;
import com.rikachka.track_android_3_3.Fragments.ChatFragment;
import com.rikachka.track_android_3_3.Fragments.LoginFragment;
import com.rikachka.track_android_3_3.Fragments.SettingsFragment;
import com.rikachka.track_android_3_3.Messages.Client.ChannelListData;
import com.rikachka.track_android_3_3.Messages.Client.LoginData;
import com.rikachka.track_android_3_3.Messages.Client.UserInfoData;
import com.rikachka.track_android_3_3.Messages.Message;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MyActivity {
    private final String LOG_TAG = getClass().getSimpleName();
    private ServiceConnection serviceConnection;
    private boolean bind = false;
    private MessageSocketService messageSocketService;
    private TextView nickname;
    private TextView status;
    private NavigationView navigationView;
    private String cid;
    private String sid;
    private String nick;

    private SharedPreferences sPref;


    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.inflateHeaderView(R.layout.main_nav_header);

        nickname = (TextView) header.findViewById(R.id.nickname_nav);
        status = (TextView) header.findViewById(R.id.status_nav);
        Log.v(LOG_TAG, "INIT");

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                AboutFragment aboutFragment = new AboutFragment();
                aboutFragment.setInfo(nick, sid, cid);
                ft.replace(R.id.frameLayout_main, aboutFragment);
                ft.commit();
                setTitle(R.string.title_profile);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });

        //Меняем состояние подключения.
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                messageSocketService = ((MessageSocketService.MyBinder) service).getService();
                messageSocketService.setMainActivity(MainActivity.this);
                Log.v(LOG_TAG, "connected");
//                nickname.setText(messageSocketService.getMessageHandler().getAuthData().getNick());
//                status.setText(messageSocketService.getMessageHandler().getAuthData().getSid());
                nick = messageSocketService.getMessageHandler().getAuthData().getNick();
                cid = messageSocketService.getMessageHandler().getAuthData().getCid();
                sid = messageSocketService.getMessageHandler().getAuthData().getSid();

                nickname.setText(nick);

                Message message = new Message("channellist", new ChannelListData(cid, sid));
                Gson gson = new Gson();
                String mes = gson.toJson(message);
                messageSocketService.sendMessage(mes);

                message = new Message("userinfo", new UserInfoData(cid, cid, sid));
                gson = new Gson();
                mes = gson.toJson(message);
                messageSocketService.sendMessage(mes);
                bind = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.v(LOG_TAG, "disconnected");
                bind = false;
            }
        };
        bindService(new Intent(this, MessageSocketService.class), serviceConnection, 0);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout_main, new AboutChangeFragment());
        ft.commit();
        setTitle(R.string.title_profile_edit);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
//            Message message = new Message("userinfo", new UserInfoData(new User(cid, nick), cid, sid));
//            Gson gson = new Gson();
//            String mes = gson.toJson(message);
//            Log.v(LOG_TAG, mes);
//            messageSocketService.sendMessage(mes);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (id) {
            case  R.id.nav_message:

//        if (id == R.id.nav_message) {
//            Message message = new Message("channellist", new ChannelListData(cid, sid));
//            Gson gson = new Gson();
//            String mes = gson.toJson(message);
//            messageSocketService.sendMessage(mes);

//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ChatFragment chatFragment = new ChatFragment();
                chatFragment.setMessageSocketService(messageSocketService);
                chatFragment.setCid(cid);
                chatFragment.setSid(sid);
                ft.replace(R.id.frameLayout_main, chatFragment);
                ft.commit();
                setTitle(R.string.title_chats);
                break;
            case R.id.nav_settings:
//        } else if (id == R.id.nav_settings) {
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frameLayout_main, new SettingsFragment());
                ft.commit();
                setTitle(R.string.title_settings);
                break;
            case R.id.nav_exit:
                deleteText();
//                ft.replace(R.id.frameLayout_main, new LoginFragment());
//                ft.commit();

//                finish();
//                Intent intent = new Intent(MainActivity.this, SplashActivity.class);
//                startActivity(intent);

                Toast disconnectedToast = Toast.makeText(getApplicationContext(), "Sorry, it is still impossible yet", Toast.LENGTH_LONG);
                disconnectedToast.show();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        //При закрытии отсоединяемся.
        if (!bind) return;
        unbindService(serviceConnection);
        //Проверка завершения
        //stopService(new Intent(this, MessageSocketService.class));
        super.onDestroy();
    }

    public MessageSocketService getMessageSocketService() {
        return messageSocketService;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        switch (item.getItemId()) {
//            case R.id.add:
//                ChatCreateFragment chatFragment = new ChatCreateFragment();
//                chatFragment.setMessageSocketService(messageSocketService);
//                chatFragment.setCid(cid);
//                chatFragment.setSid(sid);
//                ft.replace(R.id.frameLayout_main, chatFragment);
//                ft.commit();
//                setTitle(R.string.title_chat_create);
//                return true;
//            case R.id.changeInfo:
//                ft.replace(R.id.frameLayout_main, new AboutChangeFragment());
//                ft.commit();
//                setTitle(R.string.title_profile_edit);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    public String getCid() {
        return cid;
    }

    public String getSid() {
        return sid;
    }

    public void makeToastWithText(final String message) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public String getNickname() {
        return nick;
    }

    public String getStatus() {
        return status.getText().toString();
    }

    public void setStatus(String stat) {
        status.setText(stat);
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

    public void finishActivity() {
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
}
