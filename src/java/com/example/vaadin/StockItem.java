package com.example.vaadin;

import java.io.Serializable;
import java.util.Comparator;

public class StockItem implements Serializable, Comparable<StockItem>
{
  private final String name;
  private final int ammountSold;
  private final int price;
  public StockItem(String name, int price) {
    this.name = name;
    this.ammountSold = 0;
    this.price=price;
  }

  public StockItem(String name,int price, int ammountSold) {
    this.name = name;
    this.price=price;
    this.ammountSold = ammountSold;
  }

  public String getName() {
    return name;
  }

  public int getPrice() {
    return price;
  }

  public int getAmmountSold() {
    return ammountSold;
  }

  @Override
  public String toString() {
    return getName();
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof StockItem))
      return false;
    return getName().equals(((StockItem) other).getName());
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 59 * hash + (this.name != null ? this.name.hashCode() : 0);
    return hash;
  }

  public int compareTo(StockItem o) {
    return getName().compareTo(o.getName());
  }

  public static Comparator<StockItem> getAmmountComparator() {
    return new Comparator<StockItem>()
    {
      public int compare(StockItem o1, StockItem o2) {
        if (o1.getAmmountSold() == o2.getAmmountSold())
          return o1.getName().compareTo(o2.getName());
        else
          return o1.getAmmountSold() > o2.getAmmountSold() ? -1 : 1;
      }
    };
  }
}
