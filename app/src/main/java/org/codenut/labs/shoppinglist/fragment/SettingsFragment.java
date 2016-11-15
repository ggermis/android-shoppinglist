package org.codenut.labs.shoppinglist.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
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
        setSummaryValues();
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
        Preference pref = findPreference(key);
        if (pref instanceof ListPreference) {
            ListPreference lp = (ListPreference) pref;
            pref.setSummary(lp.getValue());
        }
        if (pref instanceof EditTextPreference) {
            EditTextPreference etp = (EditTextPreference) pref;
            if (key.equals(PREF_PASS)) {
                final String password = etp.getText();
                pref.setSummary(password.isEmpty() ? "No password specified" : "****");
            } else {
                pref.setSummary(etp.getText());
            }
        }
    }


    private void setSummaryValues() {
        setSummaryValueFor(PREF_PROTOCOL, "No protocol specified");
        setSummaryValueFor(PREF_HOST, "No host specified");
        setSummaryValueFor(PREF_PORT, "No port specified");
        setSummaryValueFor(PREF_USER, "No username specified");
        setSummaryValueFor(PREF_FILE, "No file specified");
        setSummaryValueFor(PREF_FONT_SIZE, "No font size specified");

        EditTextPreference prefPassword = (EditTextPreference) findPreference(PREF_PASS);
        final String password = settings.getString(PREF_PASS, "");
        prefPassword.setSummary(password.isEmpty() ? "No password specified" : "****");
    }

    private void setSummaryValueFor(final String key, final String defaultText) {
        Preference pref = findPreference(key);
        if (pref instanceof EditTextPreference) {
            EditTextPreference preference = (EditTextPreference) findPreference(key);
            preference.setSummary(settings.getString(key, defaultText));
        }
        if (pref instanceof  ListPreference) {
            ListPreference preference = (ListPreference) findPreference(key);
            preference.setSummary(settings.getString(key, defaultText));
        }
    }
}
