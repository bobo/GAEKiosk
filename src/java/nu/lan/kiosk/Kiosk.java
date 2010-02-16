/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.lan.kiosk;

import com.google.common.collect.ImmutableList;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.List;
import nu.lan.kiosk.components.EANScanner;
import nu.lan.kiosk.components.ItemList;
import nu.lan.kiosk.components.Payment;
import nu.lan.kiosk.components.Purchase;
import nu.lan.kiosk.model.StockItem;
import nu.lan.kiosk.server.StockService;

/**
 *
 * @author mikael.sundberg
 */
public class Kiosk extends VerticalLayout {

    StockService stockService = ServiceFactory.getStockService();
    private Purchase purchase = new Purchase();

    public Kiosk() {
        init();
    }

    public void init() {
        Button createNew = new Button("Lägg till");
        HorizontalLayout mainWindow = new HorizontalLayout();
        List<StockItem> stock = getStock();
        addComponent(new ItemList(purchase, stock));
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.addComponent(createNew);
        horizontalLayout.addComponent(new EANScanner(stock, purchase));
        addComponent(horizontalLayout);
        mainWindow.addComponent(purchase.getComponent());
        addComponent(mainWindow);
        Payment payment = new Payment(purchase);
        addComponent(payment.getWindow());
        createNew.addListener(new CreateItemWindow(this));

        PurchaseHistory purchaseHistory = new PurchaseHistory();
        payment.addListener(purchaseHistory);
        mainWindow.addComponent(purchaseHistory);
    }

    private List<StockItem> getStock() {
        ImmutableList<StockItem> itemList = stockService.getItemList();
        for (StockItem stockItem : itemList) {
        System.out.println(stockItem.getEan());
        }
        return new ArrayList<StockItem>(itemList);

    }
}
