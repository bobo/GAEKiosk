/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.lan.kiosk.server.mssql;

import nu.lan.kiosk.util.Hasher;
import nu.lan.kiosk.server.LoginService;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static nu.lan.kiosk.server.ConnectionFactory.*;

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
}
