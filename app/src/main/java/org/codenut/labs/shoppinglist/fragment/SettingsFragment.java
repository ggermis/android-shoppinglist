package org.codenut.labs.shoppinglist.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import org.codenut.labs.shoppinglist.R;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

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

        // Show values in summary
        SharedPreferences settings = getPreferenceScreen().getSharedPreferences();
        ListPreference prefProtocol = (ListPreference) findPreference(PREF_PROTOCOL);
        prefProtocol.setSummary(settings.getString(PREF_PROTOCOL, "No protocol specified"));
        EditTextPreference prefHost = (EditTextPreference) findPreference(PREF_HOST);
        prefHost.setSummary(settings.getString(PREF_HOST, "No host specified"));
        EditTextPreference prefPort = (EditTextPreference) findPreference(PREF_PORT);
        prefPort.setSummary(settings.getString(PREF_PORT, "No port specified"));
        EditTextPreference prefUser = (EditTextPreference) findPreference(PREF_USER);
        prefUser.setSummary(settings.getString(PREF_USER, "No username specified"));
        EditTextPreference prefPassword = (EditTextPreference) findPreference(PREF_PASS);
        final String password = settings.getString(PREF_PASS, "");
        prefPassword.setSummary(password.isEmpty() ? "No password specified" : "****");
        EditTextPreference prefFile = (EditTextPreference) findPreference(PREF_FILE);
        prefFile.setSummary(settings.getString(PREF_FILE, "No file specified"));
        EditTextPreference prefFontSize = (EditTextPreference) findPreference(PREF_FONT_SIZE);
        prefFontSize.setSummary(settings.getString(PREF_FONT_SIZE, "No font size specified"));
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
}
