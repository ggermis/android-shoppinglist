package org.codenut.labs.shoppinglist.storage;

import org.codenut.labs.shoppinglist.model.ShoppingList;

public interface Storage {
    ShoppingList load();
    ShoppingList save(final ShoppingList shoppingList);
}
