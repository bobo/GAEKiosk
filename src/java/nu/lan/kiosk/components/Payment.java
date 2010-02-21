package nu.lan.kiosk.components;

import nu.lan.kiosk.PayementListener;
import nu.lan.kiosk.PurchaseHistory;
import nu.lan.kiosk.PurchaseListener;
import nu.lan.kiosk.ServiceFactory;
import nu.lan.kiosk.server.StockService;
import com.vaadin.data.Property.ConversionException;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window.Notification;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Payment implements Serializable, PurchaseListener {

    private final Label sumLabel = new Label();
    private final Label returnMoney = new Label();
    private StockService stockService = ServiceFactory.getStockService();
    private Purchase purchase;
    private final GridLayout grid = new GridLayout(3, 2);
    private Button finish = new Button("Slutför köp");
    private Button cancel = new Button("Avbryt köp");
    private final List<PayementListener> listeners = new ArrayList<PayementListener>();
    private final KeyPad keyPad;
    private final ItemList itemList;

    public Payment(Purchase purchase, ItemList itemList) {
        this.purchase = purchase;
        this.itemList = itemList;
        returnMoney.setContentMode(Label.CONTENT_XHTML);
        this.keyPad = new KeyPad(this);
        addComponents();
        addListeners();

    }

    private void addComponents() {
        grid.addComponent(keyPad);
        grid.addComponent(returnMoney);
        grid.addComponent(sumLabel);
        grid.addComponent(new Label(""));
        grid.addComponent(finish);
        grid.addComponent(cancel);
        grid.setSizeFull();
    }

    private void addListeners() {
        finish.addListener(new FinishListener());
        cancel.addListener(new FinishListener());
        purchase.addListener(this);
    }

    private void firePaymentListeners() {
        for (PayementListener payementListener : listeners) {
            payementListener.onPayment();
        }
    }

    @Override
    public void onPurchase() {
        sumLabel.setCaption("Pris: " + purchase.getPurchasePrice());
        updateReturnMoney();

    }

    private void clearPurchase() throws ConversionException, ReadOnlyException {
        Payment.this.purchase.clear();
        Payment.this.sumLabel.setValue(0);
        Payment.this.sumLabel.setValue(0);
        keyPad.clear();
        updateReturnMoney();
        onPurchase();
    }

    public Component getWindow() {
        return grid;
    }

    private void updateReturnMoney() {
        try {
            int recieved = keyPad.getSum();
            if (recieved >= purchase.getPurchasePrice()) {
                returnMoney.setValue("Växel<br> Totalt:<b>" + (recieved - purchase.getPurchasePrice()) + "</b> <br>Valutahjälp<br> " + formatMoney(recieved - purchase.getPurchasePrice()));
            } else {
                returnMoney.setValue("<FONT color='RED'>För lite betalt</FONT>");
            }
        } catch (Exception e) {
        }
    }

    public String formatMoney(int sum) {
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

    public void addListener(PurchaseHistory purchaseHistory) {
        listeners.add(purchaseHistory);
    }

    private class FinishListener implements ClickListener {

        public FinishListener() {
        }

        @Override
        public void buttonClick(ClickEvent event) {
            if (event.getSource().equals(finish)) {
                stockService.completePurchase(Payment.this.purchase);
                itemList.setNewItems(stockService.getItemList());
                firePaymentListeners();

                getWindow().getApplication().getMainWindow().showNotification("Köp genomfört", Notification.POSITION_CENTERED);
            } else {
                getWindow().getApplication().getMainWindow().showNotification("Köp Avbrutet", Notification.POSITION_CENTERED);

            }
            clearPurchase();
        }
    }
}
