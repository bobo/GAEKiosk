/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.lan.kiosk.server.mssql;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import nu.lan.kiosk.util.Hasher;
import nu.lan.kiosk.server.LoginService;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mikael.sundberg
 */
public class MSSQLLoginService implements LoginService {

    public MSSQLLoginService() {
    }

    @Override
    public boolean doLogin(String userName, String password) {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            PreparedStatement p = getConnection().prepareStatement("select * from Users where  userUserName=?");
            p.setString(1, userName);
            Hasher hasher = new Hasher();
            String hashedPass = hasher.hash(password);
            ResultSet res = p.executeQuery();
            if (res.next() && res.getString("UserPassword").equals(hashedPass)) {
                return true;

            } else {
                System.out.println("invalid");
                System.out.println(res.getString("UserPassword"));
                System.out.println(hashedPass);
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
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
}
