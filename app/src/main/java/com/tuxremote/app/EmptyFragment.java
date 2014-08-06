package com.tuxremote.app;

import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tuxremote.app.TuxeRemoteSsh.BashReturn;

import java.util.ArrayList;

public class EmptyFragment extends Fragment{

    public static final String TAG="EmptyFragment";

    public static EmptyFragment newInstance() {
        return new EmptyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.empty_fragment, container, false);

        return rootView;
    }
}
