/*
 * MyApplication.java
 *
 * Created on den 31 december 2009, 16:36
 */
package com.example.vaadin;

import com.vaadin.Application;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.terminal.Paintable.RepaintRequestEvent;
import com.vaadin.terminal.Paintable.RepaintRequestListener;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** 
 *
 * @author mikael.sundberg
 * @version 
 */
public class MyApplication extends Application
{
  StockService stockService = new BigTableStockService();
  private Purchase purchase = new Purchase();

  @Override
  public void init() {
    final Window mainWindow = new Window("Kiosk");
    Button createNew = new Button("Lägg till");
    setMainWindow(mainWindow);
    addStockButtons(mainWindow);
    HorizontalLayout horizontalLayout = new HorizontalLayout();
    horizontalLayout.addComponent(new StockItemsComboBox(purchase, stockService));
    horizontalLayout.addComponent(createNew);
    mainWindow.addComponent(horizontalLayout);
    mainWindow.addComponent(purchase.getComponent());
    Payment payment = new Payment(purchase);
    mainWindow.addComponent(payment.getWindow());
    createNew.addListener(new CreateItemWindow(mainWindow));
  }

  private void addStockButtons(Window mainWindow) {
    GridLayout buttonsPanel = new GridLayout(3, 3);
    buttonsPanel.setWidth("600px");
    List<StockItem> stock = getTop(9);
    for (StockItem stockItem : stock) {
      Button b = new Button(stockItem.getName() + " (" + stockItem.getPrice() + "kr)", new StockItemListener(stockItem));
      b.setSizeFull();
      buttonsPanel.addComponent(b);
    }
    mainWindow.addComponent(buttonsPanel);
  }

  private List<StockItem> getTop(int number) {
    List<StockItem> stock = new ArrayList(stockService.getItemList());
    System.out.println("stock size: " + stock.size());
    Collections.sort(stock, StockItem.getAmmountComparator());
    stock = stock.subList(0, stock.size() < number ? stock.size() : number);
    return stock;

  }

  private class StockItemListener implements Button.ClickListener,
          Serializable
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
