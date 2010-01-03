package com.example.vaadin;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.VerticalLayout;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemList extends VerticalLayout
{
  private final GridLayout gridLayout = new GridLayout(3, 3);
  private final Purchase purchase;
  StockService stockService = new BigTableStockService();

  public ItemList(Purchase purchase) {
    this.purchase = purchase;
    gridLayout.setWidth("600px");
    addComponent(gridLayout);
    addStockButtons();
  }

  private void addStockButtons() {

    List<StockItem> stock = getTop(9);
    for (StockItem stockItem : stock) {
      Button b = new Button(stockItem.getName() + " (" + stockItem.getPrice() + "kr)", new StockItemListener(stockItem));
      b.setSizeFull();
      gridLayout.addComponent(b);
    }
  }

  private List<StockItem> getTop(int number) {
    List<StockItem> stock = new ArrayList(stockService.getItemList());
    Collections.sort(stock, StockItem.getAmmountComparator());
    stock = stock.subList(0, stock.size() < number ? stock.size() : number);
    return stock;

  }

  private class StockItemListener implements Button.ClickListener, Serializable
  {
    private final StockItem stockItem;

    public StockItemListener(StockItem stockItem) {
      this.stockItem = stockItem;
    }

    public void buttonClick(ClickEvent event) {
      purchase.purchase(stockItem);
    }
  }
}
