package com.example.vaadin;

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import java.util.Map;

public class PurchaseHistory extends Table implements PayementListener
{
  StockService service = new BigTableStockService();

  public PurchaseHistory() {
    addContainerProperty("Ta bort", Button.class, null);
    addContainerProperty("Köp", String.class, null);
    updateHistory();
    setHeight("600px");
    setWidth("300px");

  }

  public void onPayment() {
    updateHistory();
  }

  public void updateHistory() {
    System.out.println("updating history");
    removeAllItems();


    Map<Long, String> purchaseList = service.getPurchaseList();
    for (Long id : purchaseList.keySet()) {
      Item i = addItem(id);
      i.getItemProperty("Köp").setValue(purchaseList.get(id));
      i.getItemProperty("Ta bort").setValue(new DeleteButton(id));
    }
  }

  private class DeleteButton extends Button
  {
    final long id;

    public DeleteButton(long id) {
      this.id = id;
      setCaption("X");
      addListener(new ClickListener()
      {
        public void buttonClick(ClickEvent event) {
          System.out.println("delete item: " + DeleteButton.this.id);
          service.removePurchase(DeleteButton.this.id);
          updateHistory();
        }
      });
    }
  }
}
