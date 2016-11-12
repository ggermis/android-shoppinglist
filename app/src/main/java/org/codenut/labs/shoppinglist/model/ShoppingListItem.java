package org.codenut.labs.shoppinglist.model;

import android.util.Log;

public class ShoppingListItem {

    private String name;
    private Integer count;
    private String shop;

    private boolean active;


    public ShoppingListItem(final String name, final Integer count, final String shop) {
        this.name = name;
        this.count = count;
        this.shop = shop;
        this.active = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return isActive() ? count : 0;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void toggleActive() {
        Log.d("model", "Toggling [" + this.name + ", " + this.count + "]: " + this.active + " to " + !this.active);
        this.active = !this.active;
    }

    public String asString() {
        return getCount() + " " + getName() + " (" + getShop() + ")";
    }
}
