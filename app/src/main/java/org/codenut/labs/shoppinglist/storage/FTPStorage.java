package org.codenut.labs.shoppinglist.storage;

import android.content.SharedPreferences;
import android.util.Log;

import org.apache.commons.net.ftp.FTPClient;
import org.codenut.labs.shoppinglist.data.ShoppingListParser;
import org.codenut.labs.shoppinglist.fragment.SettingsFragment;
import org.codenut.labs.shoppinglist.model.ShoppingList;

import java.io.IOException;
import java.io.InputStream;

class FTPStorage implements Storage {

    private FTPClient ftp = new FTPClient();

    private String host;
    private String port;
    private String username;
    private String password;
    private String filename;
    private String message;

    FTPStorage(final SharedPreferences settings) {
        this.host = settings.getString(SettingsFragment.PREF_HOST, "");
        this.port = settings.getString(SettingsFragment.PREF_PORT, "");
        this.username = settings.getString(SettingsFragment.PREF_USER, "");
        this.password = settings.getString(SettingsFragment.PREF_PASS, "");
        this.filename = settings.getString(SettingsFragment.PREF_FILE, "");
    }


    public ShoppingList load() {
        clearMessage();
        ShoppingList shoppingList = new ShoppingList();
        try {
            ftp.connect(host, Integer.parseInt(port.isEmpty() ? "21" : port));
            if (ftp.login(username, password)) {
                InputStream in = ftp.retrieveFileStream(filename);
                if (in == null) {
                    this.message = "Unable to read file " + filename;
                    return shoppingList;
                }
                shoppingList = new ShoppingListParser().parse(in);
                in.close();
                ftp.logout();
            }
            ftp.disconnect();
        } catch (IOException e) {
            this.message = e.getMessage();
            Log.d("exception", e.getMessage());
        }
        return shoppingList;
    }


    public ShoppingList save(final ShoppingList shoppingList) {
        clearMessage();
        try {
            ftp.connect(host);
            if (ftp.login(username, password)) {
                ftp.setFileTransferMode(FTPClient.PASSIVE_REMOTE_DATA_CONNECTION_MODE);
                ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftp.setBufferSize(1024);
                ftp.storeFile(filename, shoppingList.asInputStream());
                ftp.logout();
            }
            ftp.disconnect();
        } catch (IOException e) {
            this.message = e.getMessage();
            Log.d("exception", e.getMessage());
        }
        return shoppingList;
    }

    public String getMessage() {
        return this.message;
    }

    private void clearMessage() {
        this.message = null;
    }

    @Override
    public String toString() {
        return host;
    }
}
