package nu.lan.kiosk.model;

import java.io.Serializable;
import java.util.Comparator;
import lombok.Data;
@Data
public class StockItem implements Serializable, Comparable<StockItem> {

    private final String name;
    private final int price;
    private final String ean;
    private final int ammountSold;
    private final String category;
    
    public StockItem(String name, int price) {
        this.name = name;
        this.price = price;
        this.ean = "";
        this.ammountSold = 0;
        this.category="";
    }

    public StockItem(String name, int price, String ean, Integer ammountSold,String category) {
        this.name = name;
        this.price = price;
        this.ean = ean;
        this.ammountSold = ammountSold == null ? 0 : ammountSold;
        this.category = category;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String debugString() {
        return "name: " + name + ", price: " + price + " ean: " + ean + " sold:" + ammountSold;
    }


    @Override
    public boolean equals(Object other) {
        if (!(other instanceof StockItem)) {
            return false;
        }
        return getName().equals(((StockItem) other).getName());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    @Override
    public int compareTo(StockItem o) {
        return getName().compareTo(o.getName());
    }

    public static Comparator<StockItem> getAmmountComparator() {
        return new Comparator<StockItem>() {

            @Override
            public int compare(StockItem o1, StockItem o2) {
                if (o1.getAmmountSold() == o2.getAmmountSold()) {
                    return o1.getName().compareTo(o2.getName());
                } else {
                    return o1.getAmmountSold() > o2.getAmmountSold() ? -1 : 1;
                }
            }
        };
    }

    public static Comparator<StockItem> getNameComparator() {
        return new Comparator<StockItem>() {

            @Override
            public int compare(StockItem o1, StockItem o2) {
                return o1.getName().compareTo(o2.getName());
            }
        };
    }
}
