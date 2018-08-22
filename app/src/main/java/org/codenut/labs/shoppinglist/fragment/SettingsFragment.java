package org.codenut.labs.shoppinglist.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import org.codenut.labs.shoppinglist.R;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences settings;

    public static final String PREF_FONT_SIZE = "PREF_FONT_SIZE";
    public static final String PREF_PROTOCOL = "PREF_PROTOCOL";
    public static final String PREF_HOST = "PREF_HOST";
    public static final String PREF_PORT = "PREF_PORT";
    public static final String PREF_USER = "PREF_USER";
    public static final String PREF_PASS = "PREF_PASSWORD";
    public static final String PREF_FILE = "PREF_FILE";
    static final String PREF_AUTO_LOAD = "PREF_AUTO_LOAD";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        this.settings = getPreferenceScreen().getSharedPreferences();
        setSummaryValueFor(PREF_PROTOCOL, settings.getString(PREF_PROTOCOL,""));
        setSummaryValueFor(PREF_HOST, settings.getString(PREF_HOST, ""));
        setSummaryValueFor(PREF_PORT, settings.getString(PREF_PORT, "0"));
        setSummaryValueFor(PREF_USER, settings.getString(PREF_USER, ""));
        setSummaryValueFor(PREF_FILE, settings.getString(PREF_FILE, ""));
        setSummaryValueFor(PREF_FONT_SIZE, settings.getString(PREF_FONT_SIZE, "18"));
        setSummaryValueFor(PREF_PASS, settings.getString(PREF_PASS, ""));
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (PREF_AUTO_LOAD.equals(key)) {
            setSummaryValueFor(key, settings.getBoolean(key, Boolean.FALSE));
        } else {
            setSummaryValueFor(key, settings.getString(key, ""));
        }
    }


    private <T> void setSummaryValueFor(final String key, final T currentValue) {
        Preference preference = findPreference(key);
        final String summary = getValueOrDefault(currentValue.toString(), key.equals(PREF_PASS) );
        preference.setSummary(summary);
    }


    private String getValueOrDefault(String value, boolean obfuscated) {
        if (value.isEmpty()) {
            return "<empty>";
        } else {
            return obfuscated ? "*****" : value;
        }
    }
}
