package com.rikachka.track_android_3_3.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rikachka.track_android_3_3.MessageSocketService;
import com.rikachka.track_android_3_3.R;

public class SplashFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().startService(new Intent(getActivity(), MessageSocketService.class));
        return inflater.inflate(R.layout.fragment_splash, null);
    }
}
