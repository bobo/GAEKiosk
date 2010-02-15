   /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.lan.kiosk.server;

import nu.lan.kiosk.components.Purchase;
import nu.lan.kiosk.model.StockItem;
import com.google.common.collect.ImmutableList;
import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author mikael.sundberg
 */
public interface StockService extends Serializable
{
  public ImmutableList<StockItem> getItemList();

  public void completePurchase(Purchase purchase);

  public void addStockItem(StockItem stockItem);

  public Map<Long, String> getPurchaseList();

  public void removePurchase(long id);
}
