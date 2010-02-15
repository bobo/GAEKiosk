/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.lan.kiosk.components;

import nu.lan.kiosk.model.StockItem;
import com.vaadin.data.Property;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window.Notification;
import java.util.List;

/**
 *
 * @author mikael.sundberg
 */
public class EANScanner extends TextField {

    private final List<StockItem> stock;
    private final Purchase purchase;

    public EANScanner(List<StockItem> stock, Purchase purchase) {
        this.stock = stock;
        this.purchase = purchase;
        setImmediate(true);
        addListener(new EANListener(this.purchase));

    }

    private StockItem findItemForEAN(String ean) {
        for (StockItem stockItem : stock) {
            if (stockItem.getEan().equalsIgnoreCase(ean))
                return stockItem;
        }
        return null;

    }

    private class EANListener implements ValueChangeListener {

        private final Purchase purchase;

        public EANListener(Purchase purchase) {
            this.purchase = purchase;
        }

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            StockItem item = findItemForEAN(getValue().toString().trim());
            if (item != null)
                purchase.purchase(item);
            else {
                Notification n = new Notification("Hittade ingen vara", Notification.TYPE_WARNING_MESSAGE);
                n.setDelayMsec(1500);
                n.setPosition(Notification.POSITION_CENTERED_TOP);
                getApplication().getMainWindow().showNotification(n);
            }
            setValue("");
        }
    }
}
