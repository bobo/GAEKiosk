package com.example.vaadin;

import com.vaadin.data.Property.ConversionException;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.terminal.Paintable.RepaintRequestEvent;
import com.vaadin.terminal.Paintable.RepaintRequestListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import java.io.Serializable;

public class Payment implements Serializable
{
  private final Label sumLabel = new Label();
  private final Label returnMoney = new Label();
  private final TextField recievedMoney = new TextField();
  StockService fakeStockService = new BigTableStockService();
  Purchase purchase;
  private final HorizontalLayout window = new HorizontalLayout();

  public Payment(Purchase purchase) {
    this.purchase = purchase;
    returnMoney.setContentMode(Label.CONTENT_XHTML);
    window.addComponent(sumLabel);
    window.addComponent(recievedMoney);
    window.addComponent(returnMoney);
    purchase.addListener(listener);
    window.setWidth("600px");
    Button finish = new Button("Slutför köp");
    window.addComponent(finish);
    finish.addListener(new Button.ClickListener()
    {
      public void buttonClick(ClickEvent event) {
        fakeStockService.completePurchase(Payment.this.purchase);
        clearPurchase();
      }
    });
    recievedMoney.setImmediate(true);
    recievedMoney.addListener(new ValueChangeListener()
    {
      public void valueChange(ValueChangeEvent event) {
        updateReturnMoney();
      }
    });
  }
  private PurchaseListener listener = new PurchaseListener()
  {
    public void fireChange() {
      sumLabel.setCaption("Pris: " + purchase.getPurchasePrice());
      updateReturnMoney();
    }
  };

  private void clearPurchase() throws ConversionException, ReadOnlyException {
    Payment.this.purchase.clear();
    recievedMoney.setValue(0);
    updateReturnMoney();
    listener.fireChange();
  }

  public Component getWindow() {
    return window;
  }

  private void updateReturnMoney() {
    try {
      int recieved = Integer.parseInt(recievedMoney.getValue().toString());
      if (recieved >= purchase.getPurchasePrice())
        returnMoney.setValue("Växel<br> Totalt:<b>" + (recieved - purchase.getPurchasePrice()) + "</b> <br>Valutahjälp<br> " + formatMoney(recieved - purchase.getPurchasePrice()));
      else
        returnMoney.setValue("<FONT color='RED'>För lite betalt</FONT>");
    }
    catch (Exception e) {
      System.out.println("not a number" + recievedMoney.getValue());
    }
  }

  String formatMoney(int sum) {
    StringBuilder toReturn = new StringBuilder();
    sum = appendHundreds(sum, toReturn);
    sum = appendFifties(sum, toReturn);
    sum = appendTwenties(sum, toReturn);
    sum = appendTens(sum, toReturn);
    sum = appendFivers(sum, toReturn);
    sum = appendSingles(sum, toReturn);
    return toReturn.toString();
  }

  private int appendAmmount(int sum, StringBuilder toReturn, int amount) {
    if (sum >= amount) {
      int count = (int) Math.floor(sum / amount);
      toReturn.append(count).append("*").append(amount).append("kr").append("<br>");
      sum = sum - count * amount;
    }
    return sum;
  }

  private int appendFifties(int sum, StringBuilder toReturn) {
    return appendAmmount(sum, toReturn, 50);
  }

  private int appendFivers(int sum, StringBuilder toReturn) {
    return appendAmmount(sum, toReturn, 5);
  }

  private int appendHundreds(int sum, StringBuilder toReturn) {
    return appendAmmount(sum, toReturn, 100);
  }

  private int appendSingles(int sum, StringBuilder toReturn) {
    return appendAmmount(sum, toReturn, 1);
  }

  private int appendTens(int sum, StringBuilder toReturn) {
    return appendAmmount(sum, toReturn, 10);
  }

  private int appendTwenties(int sum, StringBuilder toReturn) {
    return appendAmmount(sum, toReturn, 20);
  }
}
