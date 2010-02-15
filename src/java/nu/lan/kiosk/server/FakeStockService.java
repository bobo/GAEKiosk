package nu.lan.kiosk.server;

import nu.lan.kiosk.components.Purchase;
import nu.lan.kiosk.model.StockItem;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Map;

public class FakeStockService implements StockService
{
  public FakeStockService() {
  }

  @Override
  public ImmutableList<StockItem> getItemList() {
//
//    return ImmutableList.of(new StockItem("Godis", 10), new StockItem("Kinder", 5), new StockItem("PowerKing", 8, 20), new StockItem("Fanta", 5), new StockItem("Cola", 5), new StockItem("Sprite", 5), new StockItem("Fanta Exotic", 5), new StockItem("Chips", 12), new StockItem("Jolt", 5, 10));
  return ImmutableList.of();
  }

  @Override
  public void completePurchase(Purchase purchase) {
  }

  @Override
  public void addStockItem(StockItem stockItem) {
  }

  @Override
  public Map<Long, String> getPurchaseList() {
   return Maps.newHashMap();
  }

  @Override
  public void removePurchase(long id) {
  }
}
