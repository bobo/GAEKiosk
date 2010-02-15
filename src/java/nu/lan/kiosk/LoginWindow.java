/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.lan.kiosk;

import nu.lan.kiosk.server.LoginService;
import nu.lan.kiosk.server.mssql.MSSQLLoginService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 *
 * @author mikael.sundberg
 */
public class LoginWindow extends VerticalLayout {

    private TextField userName = new TextField("Username:");
    private TextField password = new TextField("Password");
    private Button submit = new Button("Send");
    private LoginService loginService = new MSSQLLoginService();

    public LoginWindow(Window window) {

        password.setSecret(true);
        addComponent(userName);
        addComponent(password);
        addComponent(submit);

        submit.addListener(new LoginListener(window));

    }

    private class LoginListener implements ClickListener {

        final private Window window;

        public LoginListener(Window comp) {
            this.window = comp;
        }

        @Override
        public void buttonClick(ClickEvent event) {
            if (loginService.doLogin(userName.getValue().toString(), password.getValue().toString())) {
                window.removeAllComponents();
                window.addComponent(new Kiosk());
            }
        }
    }
}
