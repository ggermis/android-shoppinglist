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
    public static final String PREF_AUTO_LOAD = "PREF_AUTO_LOAD";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        this.settings = getPreferenceScreen().getSharedPreferences();
        setSummaryValueFor(PREF_PROTOCOL);
        setSummaryValueFor(PREF_HOST);
        setSummaryValueFor(PREF_PORT);
        setSummaryValueFor(PREF_USER);
        setSummaryValueFor(PREF_FILE);
        setSummaryValueFor(PREF_FONT_SIZE);
        setSummaryValueFor(PREF_PASS);
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
        setSummaryValueFor(key);
    }


    private void setSummaryValueFor(final String key) {
        Preference preference = findPreference(key);
        final String value = getValueOrDefault(settings.getString(key, ""), "<empty>", key.equals(PREF_PASS));
        preference.setSummary(value);
    }


    private String getValueOrDefault(String value, String defaultValue, boolean obfuscated) {
        return value.isEmpty() ? defaultValue : obfuscated ? convertToStars(value) : value;
    }

    private String convertToStars(String value) {
        return value.replaceAll(".", "*");
    }
}
