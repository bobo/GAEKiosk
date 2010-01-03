package com.example.vaadin;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
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
    for (StockItem item : purchase.getItemsSold()) {
      try {
        Entity entity = datastore.get(KeyFactory.createKey("StockItem", item.getName()));
        Long ammountSold = (Long) entity.getProperty("Sold");
        ammountSold = ammountSold + purchase.numberOf(item);
        entity.setProperty("Sold", ammountSold);
        System.out.println(item.getName() + " now: " + ammountSold);
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
}
