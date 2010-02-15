package nu.lan.kiosk.server.mssql;

import nu.lan.kiosk.util.Parser;
import nu.lan.kiosk.components.Purchase;
import nu.lan.kiosk.model.StockItem;
import nu.lan.kiosk.server.StockService;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MSSQLStockService implements StockService {

    public MSSQLStockService() {
    }

    @Override
    public ImmutableList<StockItem> getItemList() {
        Map<String, Integer> countMap = getCountMap();
        Builder<StockItem> items = ImmutableList.builder();
        System.out.println("getting list");
        try {

            PreparedStatement prepareStatement = getConnection().prepareStatement("select * FROM Produkter");
            ResultSet res = prepareStatement.executeQuery();
            while (res.next()) {
                StockItem stockItem = new StockItem(res.getString("ProduktNamn"), res.getInt("ProduktPris"), res.getString("ProduktEAN"), countMap.get(res.getString("ProduktEAN")));
                items.add(stockItem);
                System.out.println(stockItem.debugString());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return items.build();
    }

    @Override
    public void completePurchase(Purchase purchase) {
        System.out.println("purchase: " + purchase);
        PreparedStatement prepareStatement = null;
        try {

            prepareStatement = getConnection().prepareStatement("INSERT INTO KIOSK (KioskEAN, KioskDate, KioskUser,KioskLan) values (?,?,?,?)");
            long time = System.currentTimeMillis();
            for (StockItem stockItem : purchase.getItemsSold()) {
                try {
                    for (int i = 0; i < purchase.numberOf(stockItem); i++) {
                        System.out.println("adding: " + stockItem + " to purchase");
                        prepareStatement.setString(1, stockItem.getEan());
                        prepareStatement.setTimestamp(2, new Timestamp(time));
                        prepareStatement.setNull(3, Types.INTEGER);
                        prepareStatement.setInt(4, getLanNumber());
                        prepareStatement.executeUpdate();
                        prepareStatement.clearWarnings();
                        prepareStatement.clearParameters();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(prepareStatement);
        }
        System.out.println("done completing purchase: " + purchase);
    }

    @Override
    public void addStockItem(StockItem stockItem) {
        System.out.println("adding: " + stockItem);
    }

    private Map<String, Integer> getCountMap() {
        try {
            Map<String, Integer> counts = new HashMap<String, Integer>();
            PreparedStatement prepareStatement = getConnection().prepareStatement("SELECT COUNT(KioskDate), ProduktEAN FROM Kiosk join Produkter on KioskEAN=ProduktEAN where KioskLan=? group by ProduktEAN");
            prepareStatement.setInt(1, getLanNumber());
            ResultSet res = prepareStatement.executeQuery();
            while (res.next()) {
                counts.put(res.getString("ProduktEAN"), res.getInt(1));
            }
            return counts;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println(":_(");
        return Collections.EMPTY_MAP;
    }

    @Override
    public Map<Long, String> getPurchaseList() {
        Map<Long, String> map = new HashMap<Long, String>();
        System.out.println("getting list");
        PreparedStatement prepareStatement = null;
        try {
            prepareStatement = getConnection().prepareStatement("SELECT KioskID, KioskDate, ProduktNamn FROM Kiosk join Produkter on KioskEAN=ProduktEAN where KioskLan=? order by KioskDate desc, ProduktNamn desc");
            prepareStatement.setInt(1, getLanNumber());
            ResultSet res = prepareStatement.executeQuery();
            Timestamp previousDate = new Timestamp(0);
            Timestamp currentDate = new Timestamp(System.currentTimeMillis());
            StringBuilder sb = new StringBuilder();
            while (res.next()) {
                currentDate = res.getTimestamp("KioskDate");
                if (previousDate.equals(currentDate)) {
                    sb.append(res.getString("ProduktNamn")).append("#");
                } else {
                    map.put(currentDate.getTime(), sb.toString());
                    sb = new StringBuilder();
                    sb.append(res.getString("ProduktNamn")).append("#");
                    previousDate = currentDate;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            close(prepareStatement);
        }
        Parser parser = new Parser(map);
        return parser.parseMap();
    }

    private void close(PreparedStatement prepareStatement) {
        try {
            prepareStatement.getConnection().close();
        } catch (Exception sQLException) {
        }
        try {
            prepareStatement.close();
        } catch (Exception ex) {
            Logger.getLogger(MSSQLStockService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int getLanNumber() {
        PreparedStatement p = null;
        try {
            p = getConnection().prepareStatement("select SettingLan from SETTING");
            ResultSet res = p.executeQuery();
            if (res.next()) {
                return res.getInt("SettingLan");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(p);
        }
        throw new RuntimeException("Could not find any setting for lan");
    }

    private Connection getConnection() throws SQLException, IOException {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MSSQLLoginService.class.getName()).log(Level.SEVERE, null, ex);
        }
        Properties p = new Properties();
        File file = new File("db.properties");
        System.out.println("file.path: " + file.getAbsolutePath());
        p.load(new FileReader(file));
        Connection con = DriverManager.getConnection(p.getProperty("url"), p.getProperty("user"), p.getProperty("password"));
        return con;
    }

    @Override
    public void removePurchase(long id) {
    }
}
