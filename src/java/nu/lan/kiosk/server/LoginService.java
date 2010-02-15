/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nu.lan.kiosk.server;

/**
 *
 * @author mikael.sundberg
 */
public interface LoginService {

    public boolean doLogin(String userName, String password);

}
