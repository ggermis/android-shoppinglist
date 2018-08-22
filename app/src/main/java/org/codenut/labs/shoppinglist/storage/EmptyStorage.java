package org.codenut.labs.shoppinglist.storage;

import org.codenut.labs.shoppinglist.model.ShoppingList;

public class EmptyStorage implements Storage {
    private ShoppingList shoppingList;

    EmptyStorage() {
        this.shoppingList = new ShoppingList();
    }

    @Override
    public ShoppingList load() {
        return shoppingList;
    }

    @Override
    public ShoppingList save(ShoppingList shoppingList) {
        return shoppingList;
    }

    @Override
    public String getMessage() {
        return null;
    }

    @Override
    public String toString() {
        return "<empty>";
    }
}
