package org.codenut.labs.shoppinglist.data;

import org.codenut.labs.shoppinglist.model.ShoppingList;
import org.codenut.labs.shoppinglist.model.ShoppingListItem;



public class StubShoppingListLoader {
    public ShoppingList load() {
        ShoppingList list = new ShoppingList();
        list.add(new ShoppingListItem("kalkoenfilet", 2, "Aldi"));
        list.add(new ShoppingListItem("appel", 10, "Aldi"));
        list.add(new ShoppingListItem("redbull", 24, "Aldi"));
        list.add(new ShoppingListItem("sigaretten", 1, "Aldi"));
        list.add(new ShoppingListItem("pickle chips", 2, "Aldi"));
        list.add(new ShoppingListItem("coca cola", 2, "Aldi"));
        list.add(new ShoppingListItem("fanta", 2, "Aldi"));
        list.add(new ShoppingListItem("peren", 6, "Aldi"));
        list.add(new ShoppingListItem("sla", 12, "Aldi"));
        list.add(new ShoppingListItem("kaas", 2, "Aldi"));
        list.add(new ShoppingListItem("peper", 2, "Aldi"));
        list.add(new ShoppingListItem("zout", 4, "Aldi"));
        list.add(new ShoppingListItem("pizza", 2, "Aldi"));
        list.add(new ShoppingListItem("taart", 2, "Aldi"));
        list.add(new ShoppingListItem("brood", 2, "Aldi"));
        list.add(new ShoppingListItem("wc-papier", 2, "Aldi"));
        list.add(new ShoppingListItem("water", 2, "Aldi"));
        list.add(new ShoppingListItem("finley", 2, "Aldi"));
        list.add(new ShoppingListItem("tonic", 2, "Aldi"));
        list.add(new ShoppingListItem("gin", 4, "Aldi"));
        list.add(new ShoppingListItem("calvados", 2, "Aldi"));
        list.add(new ShoppingListItem("verjaardagskaart", 7, "Aldi"));
        list.add(new ShoppingListItem("bloem", 2, "Aldi"));
        list.add(new ShoppingListItem("suiker", 1, "Aldi"));
        list.add(new ShoppingListItem("deeg", 1, "Aldi"));
        return list;
    }
}
