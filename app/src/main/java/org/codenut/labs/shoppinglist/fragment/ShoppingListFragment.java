package org.codenut.labs.shoppinglist.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.jcraft.jsch.*;
import org.apache.commons.net.ftp.FTPClient;
import org.codenut.labs.shoppinglist.R;
import org.codenut.labs.shoppinglist.adapter.ShoppingListAdapter;
import org.codenut.labs.shoppinglist.data.ShoppingListParser;
import org.codenut.labs.shoppinglist.data.StubShoppingListLoader;
import org.codenut.labs.shoppinglist.model.ShoppingList;
import org.codenut.labs.shoppinglist.model.ShoppingListItem;

import java.io.IOException;
import java.io.InputStream;


public class ShoppingListFragment extends Fragment {

    private SharedPreferences settings;

    private ShoppingListAdapter adapter;

    private ListView shoppingList;


    class SFTPOpenTask extends AsyncTask<String, Void, ShoppingList> {
        @Override
        protected ShoppingList doInBackground(String... params) {
            Log.d("sftp", "Opening SFTP file");
            ShoppingList shoppingList = new ShoppingList();
            Session session = null;
            Channel channel = null;
            try {
                JSch ssh = new JSch();
                JSch.setConfig("StrictHostKeyChecking", "no");
                session = ssh.getSession(params[1], params[0], Integer.parseInt(params[4]));
                session.setPassword(params[2]);
                session.connect(5000);
                if (session.isConnected()) {
                    channel = session.openChannel("sftp");
                    channel.connect(5000);
                    ChannelSftp sftp = (ChannelSftp) channel;
                    InputStream in = sftp.get(params[3]);
                    shoppingList = new ShoppingListParser().parse(in);
                    in.close();
                }
            } catch(JSchException e) {
                Log.e("exception", e.getMessage());
            } catch(IOException e) {
                Log.e("exception", e.getMessage());
            } catch(SftpException e) {
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
        protected void onPostExecute(ShoppingList shoppingList) {
            if (!shoppingList.isEmpty()) {
                adapter.setShoppingList(shoppingList);
            } else {
                Toast.makeText(getContext(), "Unable to download file from SFTP. Check network", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(shoppingList);
        }
    }

    class FTPOpenTask extends AsyncTask<String, Void, ShoppingList> {
        private FTPClient ftp = new FTPClient();

        @Override
        protected ShoppingList doInBackground(String... params) {
            Log.d("ftp", "Opening FTP file");
            ShoppingList shoppingList = new ShoppingList();
            try {
                ftp.connect(params[0], Integer.parseInt(params[4]));
                Log.d("ftp", "connected " + params[0]);
                if (ftp.login(params[1], params[2])) {
                    Log.d("ftp", "logged in " + params[1] + ", " + params[2]);
                    InputStream in = ftp.retrieveFileStream(params[3]);
                    Log.d("ftp", "got file " + params[3]);
                    shoppingList = new ShoppingListParser().parse(in);
                    in.close();
                    ftp.logout();
                }
                ftp.disconnect();
            } catch (IOException e) {
                Log.d("exception", e.getMessage());
            }
            return shoppingList;
        }

        @Override
        protected void onPostExecute(ShoppingList shoppingList) {
            if (!shoppingList.isEmpty()) {
                adapter.setShoppingList(shoppingList);
            } else {
                Toast.makeText(getContext(), "Unable to download file from FTP. Check network", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(shoppingList);
        }
    }

    class SFTPSaveTask extends AsyncTask<String, Void, ShoppingList> {
        @Override
        protected ShoppingList doInBackground(String... params) {
            ShoppingList shoppingList = adapter.getShoppingList();
            Session session = null;
            Channel channel = null;
            try {
                JSch ssh = new JSch();
                JSch.setConfig("StrictHostKeyChecking", "no");
                session = ssh.getSession(params[1], params[0], Integer.parseInt(params[4]));
                session.setPassword(params[2]);
                session.connect(5000);
                channel = session.openChannel("sftp");
                channel.connect(5000);
                ChannelSftp sftp = (ChannelSftp) channel;
                sftp.put(shoppingList.asInputStream(), params[3]);
            } catch(JSchException e) {
                Log.e("exception", e.getMessage());
            } catch(SftpException e) {
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
        protected void onPostExecute(ShoppingList shoppingList) {
            if (shoppingList.isEmpty()) {
                Toast.makeText(getContext(), "Unable to save file to SFTP. Check network", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(shoppingList);
        }
    }

    class FTPSaveTask extends AsyncTask<String, Void, ShoppingList> {
        private FTPClient ftp = new FTPClient();

        @Override
        protected ShoppingList doInBackground(String... params) {
            ShoppingList shoppingList = adapter.getShoppingList();
            try {
                ftp.connect(params[0]);
                if (ftp.login(params[1], params[2])) {
                    ftp.setFileTransferMode(FTPClient.PASSIVE_REMOTE_DATA_CONNECTION_MODE);
                    ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                    ftp.setBufferSize(1024);
                    ftp.storeFile(params[3], shoppingList.asInputStream());
                    ftp.logout();
                }
                ftp.disconnect();
            } catch (IOException e) {
                Log.d("exception", e.getMessage());
            }
            return shoppingList;
        }

        @Override
        protected void onPostExecute(ShoppingList shoppingList) {
            if (shoppingList.isEmpty()) {
                Toast.makeText(getContext(), "Unable to save file to FTP. Check network", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(shoppingList);
        }
    }

    @Override
    public void onResume() {
        Log.d("activity", "onResume called");
        super.onResume();
        shoppingList.setAdapter(adapter);
        shoppingList.setClickable(true);
        shoppingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShoppingListItem item = (ShoppingListItem) shoppingList.getItemAtPosition(i);
                item.toggleActive();
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shopping_list, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                String protocol = settings.getString(SettingsFragment.PREF_PROTOCOL, "");
                String host = settings.getString(SettingsFragment.PREF_HOST, "");
                String port = settings.getString(SettingsFragment.PREF_PORT, "");
                String username = settings.getString(SettingsFragment.PREF_USER, "");
                String password = settings.getString(SettingsFragment.PREF_PASS, "");
                String file = settings.getString(SettingsFragment.PREF_FILE, "");

                switch(item.getItemId()) {
                    case R.id.miOpen:
                        Toast.makeText(getContext(), "Loading file from " + host, Toast.LENGTH_SHORT).show();
                        if (protocol.equals("SFTP")) {
                            new SFTPOpenTask().execute(host, username, password, file, port);
                        } else {
                            new FTPOpenTask().execute(host, username, password, file, port);
                        }
                        break;
                    case R.id.miSave:
                        Toast.makeText(getContext(), "Saving file to " + host, Toast.LENGTH_SHORT).show();
                        if (protocol.equals("SFTP")) {
                            new SFTPSaveTask().execute(host, username, password, file, port);
                        } else {
                            new FTPSaveTask().execute(host, username, password, file, port);
                        }
                        break;
                    case R.id.miLoadTestData:
                        Toast.makeText(getContext(), "Reloading test data", Toast.LENGTH_SHORT).show();
                        adapter.setShoppingList(new StubShoppingListLoader().load());
                        break;
                    case R.id.miPreferences:
                        Intent intent = new Intent();
                        intent.setClassName(getContext(), "org.codenut.labs.shoppinglist.SettingsActivity");
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
        settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        shoppingList = (ListView)view.findViewById(R.id.shoppingList);
        adapter = new ShoppingListAdapter(getActivity(), new ShoppingList());
        if (settings.getBoolean(SettingsFragment.PREF_AUTO_LOAD, false)) {
            final String host = settings.getString("PREF_HOST", "");
            Toast.makeText(getContext(), "Loading file from " + host, Toast.LENGTH_SHORT).show();
            if (settings.getString(SettingsFragment.PREF_PROTOCOL, "").equals("SFTP")) {
                new SFTPOpenTask().execute(host, settings.getString("PREF_USER", ""), settings.getString("PREF_PASSWORD", ""), settings.getString("PREF_FILE", ""), settings.getString("PREF_PORT", ""));
            } else {
                new FTPOpenTask().execute(host, settings.getString("PREF_USER", ""), settings.getString("PREF_PASSWORD", ""), settings.getString("PREF_FILE", ""), settings.getString("PREF_PORT", ""));
            }
        }
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.configuration_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
