package nu.lan.kiosk.components;

import nu.lan.kiosk.model.StockItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class ItemList extends HorizontalLayout {

    private final GridLayout gridLayout = new GridLayout(3, 3);
    private final Purchase purchase;
    private int offset = 0;
    private final List<StockItem> currentItems;

    public ItemList(Purchase purchase, List<StockItem> items) {
        this.purchase = purchase;
        currentItems = items;
        gridLayout.setWidth("600px");
        addStockButtons(0);
        Button next = new Button(">");
        Button previous = new Button("<");
        Button first = new Button("<<");
        VerticalLayout leftButtons = new VerticalLayout();
        leftButtons.addComponent(previous);
        leftButtons.addComponent(first);
        addComponent(leftButtons);
        addComponent(gridLayout);
        addComponent(next);
        first.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                offset = 0;
                addStockButtons(offset);
            }
        });
        previous.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                offset = offset > 9 ? offset - 9 : 0;
                System.out.println("offset: " + offset);
                addStockButtons(offset);
            }
        });
        next.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                offset = offset + 9;
                System.out.println("offset: " + offset);
                addStockButtons(offset);
            }
        });
    }

    private void addStockButtons(int offset) {
        gridLayout.removeAllComponents();
        List<StockItem> stock = get(9, offset);
        for (StockItem stockItem : stock) {
            Button b = new Button(stockItem.getName() + " (" + stockItem.getPrice() + "kr)", new StockItemListener(stockItem));
            b.setSizeFull();
            gridLayout.addComponent(b);
        }
    }

    private List<StockItem> get(int count, int offset) {
        List<StockItem> stock =currentItems;
        if (offset >= stock.size()) {
            offset = stock.size() - count;
        }
        int end = count + offset;
        Collections.sort(stock, StockItem.getAmmountComparator());
        stock = stock.subList(offset, stock.size() < end ? stock.size() : end);
        return stock;
    }

    private class StockItemListener implements Button.ClickListener, Serializable {

        private final StockItem stockItem;

        public StockItemListener(StockItem stockItem) {
            this.stockItem = stockItem;
        }

        @Override
        public void buttonClick(ClickEvent event) {
            purchase.purchase(stockItem);
        }
    }
}
