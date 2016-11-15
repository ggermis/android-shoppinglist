package org.codenut.labs.shoppinglist.data;

import android.util.Log;

import org.codenut.labs.shoppinglist.model.ShoppingList;
import org.codenut.labs.shoppinglist.model.ShoppingListItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Format:
 *
 *    <count> <item to get> (<store to get it from>)
 *
 */
public class ShoppingListParser {
    public ShoppingList parse(InputStream in) {
        ShoppingList shoppingList = new ShoppingList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        Pattern p = Pattern.compile("(\\d+) ([^(]+)\\(([^)]+)\\)");
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher m = p.matcher(line.trim());
                if (m.matches()) {
                    final Integer count = Integer.parseInt(m.group(1).trim());
                    final String name = m.group(2).trim();
                    final String shop = m.group(3).trim();
                    shoppingList.add(new ShoppingListItem(name, count, shop));
                }
            }
        } catch (IOException e) {
            Log.e("exception", e.getMessage());
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                Log.e("exception", e.getMessage());
            }
        }
        return shoppingList;
    }
}
