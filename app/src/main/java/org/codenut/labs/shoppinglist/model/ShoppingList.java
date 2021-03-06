package org.codenut.labs.shoppinglist.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ShoppingList {
    private List<ShoppingListItem> list;

    public ShoppingList() {
        list = new ArrayList<>();
    }

    public List<ShoppingListItem> getList() {
        return new ArrayList<>(list);
    }

    public boolean add(final ShoppingListItem item) {
        return list.add(item);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    private byte[] asByteArray() {
        StringBuilder builder = new StringBuilder();
        for (ShoppingListItem item : list) {
            builder.append(item.asString()).append("\n");
        }
        return builder.toString().getBytes(StandardCharsets.UTF_8);
    }

    public InputStream asInputStream() {
        return new ByteArrayInputStream(asByteArray());
    }
}
