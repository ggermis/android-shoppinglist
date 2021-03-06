package org.codenut.labs.shoppinglist.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.codenut.labs.shoppinglist.R;
import org.codenut.labs.shoppinglist.adapter.ShoppingListAdapter;
import org.codenut.labs.shoppinglist.data.StubShoppingListLoader;
import org.codenut.labs.shoppinglist.model.ShoppingList;
import org.codenut.labs.shoppinglist.model.ShoppingListItem;
import org.codenut.labs.shoppinglist.storage.Storage;
import org.codenut.labs.shoppinglist.storage.StorageFactory;


public class ShoppingListFragment extends Fragment {

    private SharedPreferences settings;

    private ShoppingListAdapter adapter;

    private ListView shoppingList;


    class FileLoadTask extends AsyncTask<Void, Void, ShoppingList> {
        private Storage storage;

        public FileLoadTask(final Storage storage) {
            this.storage = storage;
        }

        @Override
        protected ShoppingList doInBackground(Void... params) {
            return storage.load();
        }

        @Override
        protected void onPostExecute(ShoppingList shoppingList) {
            if (storage.getMessage() != null) {
                Toast.makeText(getContext(), "ERROR: " + storage.getMessage(), Toast.LENGTH_SHORT).show();
            }
            if (shoppingList.isEmpty()) {
                Toast.makeText(getContext(), "Empty shopping list loaded. Check settings and remote file content.", Toast.LENGTH_SHORT).show();
            }
            adapter.setShoppingList(shoppingList);
            super.onPostExecute(shoppingList);
        }
    }

    class FileSaveTask extends AsyncTask<Void, Void, ShoppingList> {
        private Storage storage;

        public FileSaveTask(Storage storage) {
            this.storage = storage;
        }

        @Override
        protected ShoppingList doInBackground(Void... voids) {
            return storage.save(adapter.getShoppingList());
        }

        @Override
        protected void onPostExecute(ShoppingList shoppingList) {
            if (shoppingList.isEmpty()) {
                Toast.makeText(getContext(), "Unable to save to " + storage + ". Check network", Toast.LENGTH_SHORT).show();
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shopping_list, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.miOpen:
                        doLoadData();
                        break;
                    case R.id.miSave:
                        doSaveData();
                        break;
//                    case R.id.miLoadTestData:
//                        doResetData();
//                        break;
                    case R.id.miPreferences:
                        Intent intent = new Intent();
                        intent.setClassName(getContext(), "org.codenut.labs.shoppinglist.SettingsActivity");
                        startActivity(intent);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        shoppingList = view.findViewById(R.id.shoppingList);
        adapter = new ShoppingListAdapter(getActivity(), new ShoppingList());

        if (settings.getBoolean(SettingsFragment.PREF_AUTO_LOAD, false)) {
            doLoadData();
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


    private void doLoadData() {
        Storage storage = StorageFactory.create(settings);
        Toast.makeText(getContext(), "Loading from " + storage, Toast.LENGTH_SHORT).show();
        new FileLoadTask(storage).execute();
    }

    private void doSaveData() {
        Storage storage = StorageFactory.create(settings);
        Toast.makeText(getContext(), "Saving to " + storage, Toast.LENGTH_SHORT).show();
        new FileSaveTask(storage).execute();
    }

//    private void doResetData() {
//        Storage storage = StorageFactory.create(settings);
//        Toast.makeText(getContext(), "Reloading test data", Toast.LENGTH_SHORT).show();
//        adapter.setShoppingList(new StubShoppingListLoader().load());
//    }
}
