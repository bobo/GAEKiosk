package com.example.vaadin;

import com.google.appengine.repackaged.com.google.common.collect.ImmutableList;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import java.awt.Stroke;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Purchase implements Serializable
{
  private Multimap<StockItem, StockItem> purchasedItems = LinkedListMultimap.create();
  private final Table table = new Table();
  private final List<PurchaseListener> listeners = new ArrayList<PurchaseListener>();

  public Purchase() {
    table.addContainerProperty("X", Button.class, null);
    table.addContainerProperty("-1", Button.class, null);
    table.addContainerProperty("Name", String.class, null);
    table.addContainerProperty("Count", Integer.class, 0);
    table.addContainerProperty("Price", Integer.class, 0);
    table.addContainerProperty("Total", Integer.class, 0);
    table.addContainerProperty("+1", Button.class, null);
    table.addContainerProperty("+5", Button.class, null);
    table.setWidth("600px");
    table.setHeight("600px");
  }

  public void purchase(StockItem stockItem) {
    purchasedItems.put(stockItem, stockItem);
    redraw(stockItem);
    fireListeners();
  }

  public void clear() {
    purchasedItems.clear();
    table.removeAllItems();
  }

  public int getPurchasePrice() {
    int sum = 0;
    for (StockItem item : purchasedItems.keySet()) {
      sum += purchasedItems.get(item).size() * item.getPrice();
    }
    return sum;
  }

  public Component getComponent() {
    return table;
  }

  public void addListener(PurchaseListener purchaseListener) {
    listeners.add(purchaseListener);
  }

  public ImmutableList<StockItem> getItemsSold() {
    return ImmutableList.copyOf(purchasedItems.keySet());
  }

  public int numberOf(StockItem item) {
    if (purchasedItems.containsKey(item))
      return purchasedItems.get(item).size();
    return 0;
  }

  private void redraw(StockItem stockItem) {
    Item item = table.addItem(stockItem.getName());
    if (item == null)
      item = table.getItem(stockItem.getName());
    item.getItemProperty("X").setValue(getRemoveButton(stockItem));
    item.getItemProperty("-1").setValue(createRemoveButton(stockItem, 1));
    item.getItemProperty("Name").setValue(stockItem.getName());
    item.getItemProperty("Price").setValue(stockItem.getPrice());
    item.getItemProperty("Total").setValue(stockItem.getPrice() * purchasedItems.get(stockItem).size());
    item.getItemProperty("+1").setValue(createAddButton(stockItem, 1));
    item.getItemProperty("+5").setValue(createAddButton(stockItem, 5));
    item.getItemProperty("Count").setValue(purchasedItems.get(stockItem).size());

  }

  private Button getRemoveButton(final StockItem stockItem) {
    Button b = new Button("X");
    b.addListener(new Button.ClickListener()
    {
      public void buttonClick(ClickEvent event) {
        table.removeItem(stockItem.getName());
        purchasedItems.removeAll(stockItem);
        fireListeners();
      }
    });
    return b;
  }

  private Button createAddButton(final StockItem stockItem, final int ammount) {
    Button b = new Button("+" + ammount);
    b.addListener(new Button.ClickListener()
    {
      public void buttonClick(ClickEvent event) {
        for (int i = 0; i < ammount; i++)
          purchase(stockItem);
      }
    });
    return b;
  }

  private Button createRemoveButton(final StockItem stockItem, final int amount) {
    Button b = new Button("-" + amount);
    b.addListener(new Button.ClickListener()
    {
      public void buttonClick(ClickEvent event) {
        for (int i = 0; i < amount; i++)
          purchasedItems.get(stockItem).remove(stockItem);
        fireListeners();
        redraw(stockItem);
      }
    });
    return b;
  }

  private void fireListeners() {
    for (PurchaseListener listener : listeners)
      listener.onPurchase();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (StockItem stockItem : purchasedItems.keySet()) {
      sb.append(stockItem.getName()).append(": ").append(purchasedItems.get(stockItem).size()).append(" \t");
    }
    return sb.toString();
  }
}
