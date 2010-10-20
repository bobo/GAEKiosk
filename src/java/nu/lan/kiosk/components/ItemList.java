package nu.lan.kiosk.components;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.util.Comparator;
import nu.lan.kiosk.model.StockItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import nu.lan.kiosk.ServiceFactory;

public class ItemList extends HorizontalLayout {

    private static final int VISIBLE_COUNT = 20;
    private Comparator<StockItem> currentComparator = StockItem.getAmmountComparator();
    Predicate<StockItem> currentFilter = Predicates.alwaysTrue();
    private final GridLayout gridLayout = new GridLayout(1, 16);
    private final Purchase purchase;
    private int offset = 0;
    private final List<StockItem> currentItems;
    private final HorizontalLayout topButtons = new HorizontalLayout();
    private final Button next = new Button(">");
    private final Button previous = new Button("<");
    private final Button first = new Button("<<");

    public ItemList(Purchase purchase, List<StockItem> items) {
        this.purchase = purchase;
        currentItems = items;
        gridLayout.setWidth("200px");
        setHeight("720px");
        setWidth("200px");
        gridLayout.addComponent(topButtons);
        addAllButtons();
        topButtons.addComponent(first);
        topButtons.addComponent(previous);
        topButtons.addComponent(next);
        addComponent(gridLayout);

        first.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                offset = 0;
                addAllButtons();
            }
        });
        previous.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                offset = offset > VISIBLE_COUNT ? offset - VISIBLE_COUNT : 0;
                addAllButtons();
            }
        });
        next.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                offset = offset + VISIBLE_COUNT;
                addAllButtons();
            }
        });

        addCategoryButtons();
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

    private void addSortingButtons() {

        for (final String name : SortingMap.keySet()) {
            gridLayout.addComponent(new SortingButton(name));
        }

    }

    private void addCategoryButtons() {
        VerticalLayout layout = new VerticalLayout();
        ImmutableList<String> categories = ServiceFactory.getStockService().getCategories();
        for (final String category : categories) {
            layout.addComponent(new CategoryButton(category));
        }
        gridLayout.addComponent(layout);
    }

    private void addAllButtons() {
        addStockButtons(offset);
        addSortingButtons();
        addCategoryButtons();
    }

    public void setNewItems(Collection<StockItem> newItems) {
        currentItems.clear();
        currentItems.addAll(newItems);
        addStockButtons(0);

    }

    private List<StockItem> get(int count, int offset) {
        List<StockItem> filtered = Lists.newArrayList(Collections2.filter(currentItems, currentFilter));
        if (offset >= filtered.size()) {
            offset = filtered.size() - count;
        }

        int end = count + offset;
        Collections.sort(filtered, currentComparator);
        List<StockItem> visibleStock = filtered.subList(offset, filtered.size() < end ? filtered.size() : end);
        return visibleStock;
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

    private class CategoryPredicate implements Predicate<StockItem> {

        private final String category;

        public CategoryPredicate(String category) {
            this.category = category;
        }

        public boolean apply(StockItem t) {
            return t.getCategory().equals(category);
        }
    }

    private class CategoryButton extends Button {

        public CategoryButton(final String title) {
            super(title);
            addListener(new Button.ClickListener() {

                public void buttonClick(ClickEvent event) {
                    currentFilter = new CategoryPredicate(title);
                    addAllButtons();
                }
            });

        }
    }

    private class SortingButton extends Button {

        public SortingButton(final String name) {
            super(name);
            addListener(new Button.ClickListener() {

                public void buttonClick(ClickEvent event) {
                    currentComparator = SortingMap.get(name);
                    addAllButtons();
                }
            });
        }
    }
    final ImmutableMap<String, Comparator<StockItem>> SortingMap = ImmutableMap.of(
            "Alfa", StockItem.getNameComparator(),
            "Count", StockItem.getAmmountComparator());
}
