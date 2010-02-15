
package nu.lan.kiosk;

import nu.lan.kiosk.server.StockService;
import nu.lan.kiosk.server.mssql.MSSQLStockService;



public class ServiceFactory {


  public static StockService getStockService(){
    return new MSSQLStockService();
  }
}
