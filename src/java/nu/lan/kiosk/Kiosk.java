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

    HorizontalLayout mainWindow = new HorizontalLayout();
    Button createNew = new Button("Lägg till");
    VerticalLayout left = new VerticalLayout();
    PurchaseHistory purchaseHistory = new PurchaseHistory();
    StockService stockService = ServiceFactory.getStockService();
    private Purchase purchase = new Purchase();

    public Kiosk() {
        init();
    }

    public final void init() {


        List<StockItem> stock = getStock();
        HorizontalLayout top = new HorizontalLayout();
        top.addComponent(createNew);
        final EANScanner eANScanner = new EANScanner(stock, purchase);

        top.addComponent(eANScanner);
        addComponent(top);

        final ItemList itemList = new ItemList(purchase, stock);
        Payment payment = new Payment(purchase, itemList);
        left.addComponent(purchase.getComponent());
        left.addComponent(payment.getWindow());
        mainWindow.addComponent(left);
        addComponent(mainWindow);
        createNew.addListener(new CreateItemWindow(this));

        payment.addListener(purchaseHistory);
        mainWindow.addComponent(purchaseHistory);

        mainWindow.addComponent(itemList);
        eANScanner.focus();
    }

    private List<StockItem> getStock() {
        ImmutableList<StockItem> itemList = stockService.getItemList();
        return new ArrayList<StockItem>(itemList);

    }
}
