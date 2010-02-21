package nu.lan.kiosk.components;

import nu.lan.kiosk.model.StockItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ItemList extends HorizontalLayout {

    private final GridLayout gridLayout = new GridLayout(1, 16);
    private final Purchase purchase;
    private int offset = 0;
    private final List<StockItem> currentItems;
    private final HorizontalLayout topButtons = new HorizontalLayout();
    private static final int VISIBLE_COUNT = 20;

    public ItemList(Purchase purchase, List<StockItem> items) {
        this.purchase = purchase;
        currentItems = items;
        gridLayout.setWidth("200px");
        setHeight("720px");
        setWidth("200px");
        gridLayout.addComponent(topButtons);
        addStockButtons(0);
        Button next = new Button(">");
        Button previous = new Button("<");
        Button first = new Button("<<");
        topButtons.addComponent(first);
        topButtons.addComponent(previous);
        topButtons.addComponent(next);
        addComponent(gridLayout);

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
                offset = offset > VISIBLE_COUNT ? offset - VISIBLE_COUNT : 0;
                System.out.println("offset: " + offset);
                addStockButtons(offset);
            }
        });
        next.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                offset = offset + VISIBLE_COUNT;
                System.out.println("offset: " + offset);
                addStockButtons(offset);
            }
        });
    }

    private void addStockButtons(int offset) {
        gridLayout.removeAllComponents();
        gridLayout.addComponent(topButtons);
        List<StockItem> stock = get(VISIBLE_COUNT, offset);
        for (StockItem stockItem : stock) {
            Button b = new Button(stockItem.getName() + " (" + stockItem.getPrice() + "kr)", new StockItemListener(stockItem));
            b.setWidth("200px");
            b.setHeight("30px");
            gridLayout.addComponent(b);
        }
    }

    public void setNewItems(Collection<StockItem> newItems) {
        currentItems.clear();
        currentItems.addAll(newItems);
        addStockButtons(0);

    }

    private List<StockItem> get(int count, int offset) {
        List<StockItem> stock = currentItems;
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
