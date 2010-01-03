/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.vaadin;

import com.google.common.collect.ImmutableList;
import java.io.Serializable;

/**
 *
 * @author mikael.sundberg
 */
public interface StockService extends Serializable
{
  public ImmutableList<StockItem> getItemList();

  public void completePurchase(Purchase purchase);

  public void addStockItem(StockItem stockItem);
}
