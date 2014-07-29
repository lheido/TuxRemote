package com.tuxremote.app;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public class Preference extends PreferenceActivity {
    public static final String TAG="SettingsFragment";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preference);
    }

    public static Preference newInstance() {
        return new Preference();
    }
}
