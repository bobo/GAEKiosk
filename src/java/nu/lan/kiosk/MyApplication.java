/*
 * MyApplication.java
 *
 * Created on den 31 december 2009, 16:36
 */
package nu.lan.kiosk;

import com.vaadin.Application;
import com.vaadin.ui.*;

/** 
 *
 * @author mikael.sundberg
 * @version 
 */
public class MyApplication extends Application {

    @Override
    public void init() {
        Window mainWindow = new Window("Kiosken");
        final VerticalLayout login = new LoginWindow(mainWindow);
        mainWindow.setContent(login);
        setMainWindow(mainWindow);

    }
}
