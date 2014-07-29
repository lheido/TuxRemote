package com.tuxremote.app;

import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EmptyFragment extends Fragment{

    public static final String TAG="EmptyFragment";

    public static EmptyFragment newInstance() {
        return new EmptyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.empty_fragment, container, false);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Global.getStaticContext());
        if(pref.getBoolean("drawer_open_at_connexion", true)) {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                NavigationDrawerFragment frag = (NavigationDrawerFragment) fragmentManager.findFragmentById(R.id.navigation_drawer);
                if (frag != null) {
                    frag.openDrawer();
                }
            }
        }
        return rootView;
    }
}
