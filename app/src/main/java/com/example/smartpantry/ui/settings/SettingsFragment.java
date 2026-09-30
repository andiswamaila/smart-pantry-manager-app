package com.example.smartpantry.ui.settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.smartpantry.R;

/**
 * Settings screen: expiring-soon alert toggle and warning period, plus a unit
 * preference. Values are persisted automatically via SharedPreferences.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
