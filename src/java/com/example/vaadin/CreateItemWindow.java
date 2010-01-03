package com.example.vaadin;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import java.io.Serializable;

public class CreateItemWindow extends Window implements Button.ClickListener, Serializable
{
  private final StockService stockService = new BigTableStockService();
  Window parent;

  public CreateItemWindow(Window parent) {
    super("Skapa ny vara");
    this.parent = parent;
  }

  public void buttonClick(ClickEvent event) {
    final TextField namn = new TextField("Namn");
    final TextField price = new TextField("Pris");
    final Button button = new Button("Slutför");
    addComponent(namn);
    addComponent(price);
    addComponent(button);
    center();
    parent.addWindow(this);
    button.addListener(new Button.ClickListener()
    {
      public void buttonClick(ClickEvent event) {
        stockService.addStockItem(new StockItem((String) namn.getValue(), Integer.parseInt((String) price.getValue())));
        parent.removeWindow(CreateItemWindow.this);
      }
    });
  }
}
