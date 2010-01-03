package com.example.vaadin;

import com.vaadin.data.Property;
import com.vaadin.ui.ComboBox;

public class StockItemsComboBox extends ComboBox
{
  private final Purchase purchase;
  private final StockService stockService;

  public StockItemsComboBox(Purchase purchase, StockService service) {
    this.purchase = purchase;
    this.stockService = service;
    setNewItemsAllowed(false);
    setNullSelectionAllowed(false);
    setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);
    setImmediate(true);
    addListener(new PurchaseChange());
    addListener(new UpdateContentListener());
    updateContent();
  }

  private class PurchaseChange implements ComboBox.ValueChangeListener
  {
    public void valueChange(Property.ValueChangeEvent event) {
      purchase.purchase((StockItem) event.getProperty().getValue());
    }
  }

  private void updateContent() throws UnsupportedOperationException {
    for (StockItem item : stockService.getItemList()) {
      if (!containsId(item))
        addItem(item);
    }
  }

  private class UpdateContentListener implements RepaintRequestListener
  {
    public void repaintRequested(RepaintRequestEvent event) {
      System.out.println("repaint: " + event);
      updateContent();
    }
  }
}
