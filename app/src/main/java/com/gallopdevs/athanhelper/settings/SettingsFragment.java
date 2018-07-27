package com.gallopdevs.athanhelper.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.gallopdevs.athanhelper.R;

public class SettingsFragment extends PreferenceFragment {

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
    }
}
