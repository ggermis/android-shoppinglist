package org.codenut.labs.shoppinglist.storage;

import android.content.SharedPreferences;

import org.codenut.labs.shoppinglist.fragment.SettingsFragment;


public class StorageFactory {
    public static Storage create(final SharedPreferences settings) {
        String protocol = settings.getString(SettingsFragment.PREF_PROTOCOL, "");
        switch (protocol) {
            case "FTP":
                return new FTPStorage(settings);
            case "SFTP":
                return new SFTPStorage(settings);
            default:
                // not implemented yet
                return null;
        }
    }
}
