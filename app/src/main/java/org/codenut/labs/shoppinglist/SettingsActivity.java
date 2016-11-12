package org.codenut.labs.shoppinglist;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import org.codenut.labs.shoppinglist.fragment.SettingsFragment;


public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
