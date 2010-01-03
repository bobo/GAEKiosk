/*
 * MyApplication.java
 *
 * Created on den 31 december 2009, 16:36
 */
package com.example.vaadin;

import com.vaadin.Application;
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
    mainWindow.addComponent(new ItemList(purchase));
    HorizontalLayout horizontalLayout = new HorizontalLayout();
    horizontalLayout.addComponent(new StockItemsComboBox(purchase, stockService));
    horizontalLayout.addComponent(createNew);
    mainWindow.addComponent(horizontalLayout);
    mainWindow.addComponent(purchase.getComponent());
    Payment payment = new Payment(purchase);
    mainWindow.addComponent(payment.getWindow());
    createNew.addListener(new CreateItemWindow(mainWindow));
  }
}
