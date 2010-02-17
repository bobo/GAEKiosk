/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nu.lan.kiosk.server;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.lan.kiosk.server.mssql.MSSQLLoginService;
import nu.lan.kiosk.server.mssql.MSSQLStockService;

/**
 *
 * @author mikael.sundberg
 */
public class ConnectionFactory {

     public static Connection getConnection() throws SQLException {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MSSQLLoginService.class.getName()).log(Level.SEVERE, null, ex);
        }
        Properties p = new Properties();
        File file = new File("db.properties");
        System.out.println("file.path: " + file.getAbsolutePath());
        try {
            p.load(new FileReader(file));
        } catch (IOException ex) {
            Logger.getLogger(MSSQLStockService.class.getName()).log(Level.SEVERE, null, ex);
        }
        Connection con = DriverManager.getConnection(p.getProperty("url"), p.getProperty("user"), p.getProperty("password"));
        return con;
    }
}
