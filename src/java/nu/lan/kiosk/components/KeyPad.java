package nu.lan.kiosk.components;

import nu.lan.kiosk.PurchaseListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;

public class KeyPad extends GridLayout {

    private int sum = 0;
    final private TextField field = new TextField();
    private final PurchaseListener listeners;

    public KeyPad(PurchaseListener listeners) {
        super(5, 5);
        this.listeners = listeners;

        addComponent(getAdativeButton(1), 0, 0, 0, 1);
        addComponent(getAdativeButton(5), 0, 2, 0, 2);
        addComponent(getAdativeButton(500), 0, 3, 0, 3);
        addComponent(new ConcatButton("0"), 2, 3, 3, 3);
        addComponent(getAdativeButton(10));
        addComponent(new ConcatButton("7"));
        addComponent(new ConcatButton("8"));
        addComponent(new ConcatButton("9"));
        addComponent(getAdativeButton(20));
        addComponent(new ConcatButton("4"));
        addComponent(new ConcatButton("5"));
        addComponent(new ConcatButton("6"));
        addComponent(getAdativeButton(50));
        addComponent(new ConcatButton("1"));
        addComponent(new ConcatButton("2"));
        addComponent(new ConcatButton("3"));
        addComponent(getAdativeButton(100));
        addComponent(new ClearButton());
        addComponent(field, 0, 4, 4, 4);
    }

    private NativeButton getAdativeButton(final int number) {
        NativeButton b = new NativeButton("" + number);
        b.setWidth("60px");
        b.setHeight("30px");
        b.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                sum += number;
                printValue();
            }
        });
        return b;
    }

    private class ConcatButton extends NativeButton {

        public ConcatButton(final String number) {
            super(number);
            setWidth("60px");
            setHeight("30px");
            super.addListener(new ClickListener() {

                @Override
                public void buttonClick(ClickEvent event) {
                    sum = Integer.parseInt(sum + number);
                    printValue();
                }
            });
        }
    }

    private class ClearButton extends NativeButton {

        public ClearButton() {
            super("C");
            super.setSizeFull();
            super.addListener(new ClickListener() {

                @Override
                public void buttonClick(ClickEvent event) {
                    sum = 0;
                    printValue();
                }
            });
        }
    }

    private void printValue() {
        this.field.setValue(sum);
        listeners.onPurchase();

    }

    public int getSum() {
        return sum;
    }
    public void clear(){
        sum=0;
        field.setValue(sum);

    }
}
