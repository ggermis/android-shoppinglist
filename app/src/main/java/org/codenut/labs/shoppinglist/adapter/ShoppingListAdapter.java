package org.codenut.labs.shoppinglist.adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

    public void setShoppingList(ShoppingList shoppingList) {
        clear();
        this.shoppingList = shoppingList;
        addAll(shoppingList.getList());
        notifyDataSetChanged();
    }

    @Override
    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolderItem viewHolderItem;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.shopping_list_item, parent, false);

            viewHolderItem = new ViewHolderItem();
            viewHolderItem.itemName = convertView.findViewById(R.id.item_name);
            viewHolderItem.itemCount = convertView.findViewById(R.id.item_count);

            convertView.setTag(viewHolderItem);
        } else {
            viewHolderItem = (ViewHolderItem) convertView.getTag();
        }

        ShoppingListItem item = getItem(position);
        if (item != null) {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());

            final String fontSizeValue = settings.getString(SettingsFragment.PREF_FONT_SIZE, "");
            final Float fontSize = fontSizeValue.isEmpty() ? Float.valueOf("24") : Float.valueOf(fontSizeValue);
            viewHolderItem.itemName.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            viewHolderItem.itemCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

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
