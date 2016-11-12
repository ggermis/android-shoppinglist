package org.codenut.labs.shoppinglist.storage;

import android.content.SharedPreferences;
import android.util.Log;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import org.codenut.labs.shoppinglist.data.ShoppingListParser;
import org.codenut.labs.shoppinglist.fragment.SettingsFragment;
import org.codenut.labs.shoppinglist.model.ShoppingList;

import java.io.IOException;
import java.io.InputStream;



class SFTPStorage implements Storage {

    private String host;
    private String port;
    private String username;
    private String password;
    private String filename;


    SFTPStorage(final SharedPreferences settings) {
        this.host = settings.getString(SettingsFragment.PREF_HOST, "");
        this.port = settings.getString(SettingsFragment.PREF_PORT, "");
        this.username = settings.getString(SettingsFragment.PREF_USER, "");
        this.password = settings.getString(SettingsFragment.PREF_PASS, "");
        this.filename = settings.getString(SettingsFragment.PREF_FILE, "");
    }

    public ShoppingList load() {
        ShoppingList shoppingList = new ShoppingList();
        Session session = null;
        Channel channel = null;
        try {
            JSch ssh = new JSch();
            JSch.setConfig("StrictHostKeyChecking", "no");
            session = ssh.getSession(username, host, Integer.parseInt(port));
            session.setPassword(password);
            session.connect(5000);
            if (session.isConnected()) {
                channel = session.openChannel("sftp");
                channel.connect(5000);
                ChannelSftp sftp = (ChannelSftp) channel;
                InputStream in = sftp.get(filename);
                shoppingList = new ShoppingListParser().parse(in);
                in.close();
            }
        } catch(JSchException | IOException | SftpException e) {
            Log.e("exception", e.getMessage());
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
        return shoppingList;
    }

    public ShoppingList save(final ShoppingList shoppingList) {
        Session session = null;
        Channel channel = null;
        try {
            JSch ssh = new JSch();
            JSch.setConfig("StrictHostKeyChecking", "no");
            session = ssh.getSession(username, host, Integer.parseInt(port));
            session.setPassword(password);
            session.connect(5000);
            channel = session.openChannel("sftp");
            channel.connect(5000);
            ChannelSftp sftp = (ChannelSftp) channel;
            sftp.put(shoppingList.asInputStream(), filename);
        } catch(JSchException | SftpException e) {
            Log.e("exception", e.getMessage());
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
        return shoppingList;
    }

    @Override
    public String toString() {
        return host;
    }
}
