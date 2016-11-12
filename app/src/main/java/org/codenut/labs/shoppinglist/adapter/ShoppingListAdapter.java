package org.codenut.labs.shoppinglist.adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.codenut.labs.shoppinglist.R;
import org.codenut.labs.shoppinglist.fragment.SettingsFragment;
import org.codenut.labs.shoppinglist.model.ShoppingList;
import org.codenut.labs.shoppinglist.model.ShoppingListItem;


public class ShoppingListAdapter extends ArrayAdapter<ShoppingListItem> {

    private ShoppingList shoppingList;

    static class ViewHolderItem {
        TextView itemName;
        TextView itemCount;
    }

    public ShoppingListAdapter(Activity activity, ShoppingList shoppingList) {
        super(activity, 0, shoppingList.getList());
        this.shoppingList = shoppingList;
    }

    public ShoppingList getShoppingList() {
        return shoppingList;
    }

    public boolean setShoppingList(ShoppingList shoppingList) {
        if (shoppingList.isEmpty()) {
            return false;
        }
        this.shoppingList = shoppingList;
        clear();
        addAll(shoppingList.getList());
        notifyDataSetChanged();
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem viewHolderItem;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.shopping_list_item, parent, false);

            viewHolderItem = new ViewHolderItem();
            viewHolderItem.itemName = (TextView) convertView.findViewById(R.id.item_name);
            viewHolderItem.itemCount = (TextView) convertView.findViewById(R.id.item_count);

            convertView.setTag(viewHolderItem);
        } else {
            viewHolderItem = (ViewHolderItem) convertView.getTag();
        }

        ShoppingListItem item = getItem(position);
        if (item != null) {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());

            final String fontSize = settings.getString(SettingsFragment.PREF_FONT_SIZE, "24");
            viewHolderItem.itemName.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.valueOf(fontSize));
            viewHolderItem.itemCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.valueOf(fontSize));

            viewHolderItem.itemName.setText(item.getName());
            viewHolderItem.itemCount.setText(String.valueOf(item.getCount()));
            if (!item.isActive() || item.getCount() == 0) {
                viewHolderItem.itemName.setAlpha(0.2f);
                viewHolderItem.itemCount.setAlpha(0.2f);
                viewHolderItem.itemCount.getBackground().setAlpha(30);
            } else {
                viewHolderItem.itemName.setAlpha(1f);
                viewHolderItem.itemCount.setAlpha(1f);
                viewHolderItem.itemCount.getBackground().setAlpha(255);
            }
        }

        return convertView;
    }
}
