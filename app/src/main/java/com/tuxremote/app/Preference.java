package com.tuxremote.app;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class Preference extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preference);
    }

    public static Preference newInstance() {
        return new Preference();
    }
}
