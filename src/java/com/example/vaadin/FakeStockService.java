package com.example.vaadin;

import com.google.common.collect.ImmutableList;

public class FakeStockService implements StockService
{
  public FakeStockService() {
  }

  public ImmutableList<StockItem> getItemList() {

    return ImmutableList.of(new StockItem("Godis", 10), new StockItem("Kinder", 5), new StockItem("PowerKing", 8, 20), new StockItem("Fanta", 5), new StockItem("Cola", 5), new StockItem("Sprite", 5), new StockItem("Fanta Exotic", 5), new StockItem("Chips", 12), new StockItem("Jolt", 5, 10));

  }

  public void completePurchase(Purchase purchase) {
  }

  public void addStockItem(StockItem stockItem) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  
}
