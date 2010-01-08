package com.example.vaadin;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BigTableStockService implements StockService
{
  public ImmutableList<StockItem> getItemList() {
    insertData();
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    List<StockItem> itemsToReturn = new ArrayList<StockItem>();
    for (Entity e : datastore.prepare(new Query("StockItem")).asIterable()) {
      itemsToReturn.add(new StockItem(e.getKey().getName(), ((Long) e.getProperty("Price")).intValue(), ((Long) e.getProperty("Sold")).intValue()));
    }
    return ImmutableList.copyOf(itemsToReturn);
  }

  public void completePurchase(Purchase purchase) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    incrementPurchecount(purchase, datastore);
    Entity entity = new Entity("Purchase");
    for (StockItem item : purchase.getItemsSold()) {
      entity.setProperty(item.getName(), purchase.numberOf(item));
      entity.setProperty("Time", System.currentTimeMillis());
    }
    datastore.put(entity);
  }

  private void incrementPurchecount(Purchase purchase, DatastoreService datastore) {
    for (StockItem item : purchase.getItemsSold()) {
      try {
        Entity entity = datastore.get(KeyFactory.createKey("StockItem", item.getName()));
        Long ammountSold = (Long) entity.getProperty("Sold");
        ammountSold = ammountSold + purchase.numberOf(item);
        entity.setProperty("Sold", ammountSold);
        datastore.put(entity);
      }
      catch (EntityNotFoundException ex) {
        Logger.getLogger(BigTableStockService.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  public void addStockItem(StockItem item) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Entity entity = new Entity("StockItem", item.getName());
    entity.setProperty("Price", item.getPrice());
    entity.setProperty("Sold", item.getAmmountSold());
    datastore.put(entity);

  }

  public Map<Long, String> getPurchaseList() {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Map<Long, String> itemsToReturn = new TreeMap<Long, String>(new LongDescendingComparator());
    for (Entity e : datastore.prepare(new Query("Purchase")).asIterable()) {
      StringBuilder sb = new StringBuilder();
      for (String key : e.getProperties().keySet()) {
        if (!key.equals("Time"))
          sb.append(key).append(":").append(e.getProperty(key)).append("\t");
      }
      itemsToReturn.put(e.getKey().getId(), sb.toString());
    }
    return itemsToReturn;
  }

  public void removePurchase(long id) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.delete(KeyFactory.createKey("Purchase", id));
  }

  private void insertData() {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    FakeStockService fakeStockService = new FakeStockService();

    for (StockItem item : fakeStockService.getItemList()) {
      Entity entity = new Entity("StockItem", item.getName());
      entity.setProperty("Price", item.getPrice());
      entity.setProperty("Sold", item.getAmmountSold());
      datastore.put(entity);
    }
  }

  static class LongDescendingComparator implements Comparator<Long>
  {
    public LongDescendingComparator() {
    }

    public int compare(Long o1, Long o2) {
      return o1.compareTo(o2) * -1;
    }
  }
}
