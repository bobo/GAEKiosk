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
        HorizontalLayout top = new HorizontalLayout();
        top.addComponent(createNew);
        final EANScanner eANScanner = new EANScanner(stock, purchase);
        
        top.addComponent(eANScanner);
        addComponent(top);
        VerticalLayout left = new VerticalLayout();
        final ItemList itemList = new ItemList(purchase, stock);
        Payment payment = new Payment(purchase, itemList);
        left.addComponent(purchase.getComponent());
        left.addComponent(payment.getWindow());
        mainWindow.addComponent(left);
        addComponent(mainWindow);
        createNew.addListener(new CreateItemWindow(this));
        PurchaseHistory purchaseHistory = new PurchaseHistory();
        payment.addListener(purchaseHistory);
        mainWindow.addComponent(purchaseHistory);

        mainWindow.addComponent(itemList);
    eANScanner.focus();
    }

    private List<StockItem> getStock() {
        long start = System.currentTimeMillis();
        ImmutableList<StockItem> itemList = stockService.getItemList();
        System.out.println("time to get stock: " + (System.currentTimeMillis() - start));
        return new ArrayList<StockItem>(itemList);

    }
}
