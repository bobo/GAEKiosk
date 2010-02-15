package nu.lan.kiosk;

import nu.lan.kiosk.model.StockItem;
import nu.lan.kiosk.server.StockService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import java.io.Serializable;

public class CreateItemWindow extends Window implements Button.ClickListener, Serializable {

    private final StockService stockService = ServiceFactory.getStockService();
    Layout parent;

    public CreateItemWindow(Layout parent) {
        super("Skapa ny vara");
        this.parent = parent;
    }

    @Override
    public void buttonClick(ClickEvent event) {
        final TextField namn = new TextField("Namn");
        final TextField price = new TextField("Pris");
        final Button button = new Button("Slutför");
        addComponent(namn);
        addComponent(price);
        addComponent(button);
        center();
        getApplication().getMainWindow().addWindow(this);
        button.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                stockService.addStockItem(new StockItem((String) namn.getValue(), Integer.parseInt((String) price.getValue())));
                getApplication().getMainWindow().removeWindow(CreateItemWindow.this);
            }
        });
    }
}
