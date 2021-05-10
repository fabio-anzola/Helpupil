package at.helpupil.application.views.login;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import at.helpupil.application.views.main.MainView;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.shared.Registration;

import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

@Route(value = "login", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Login")
@CssImport("./views/login/login-view.css")
public class LoginView extends Div {

    public LoginView() {
        addClassName("login-view");

        LoginForm loginForm = new LoginForm();

        loginForm.addLoginListener(e -> {
//            boolean isAuthenticated = authenticate(e);
//            if (isAuthenticated) {
//                navigateToMainPage();
//            } else {
//                component.setError(true);
//            }

            Notification.show("Login Attempt");
        });
        add(loginForm);




        // The login button is disabled when clicked to prevent multiple submissions.
        // To restore it, call component.setEnabled(true)
//        Button restoreLogin = new Button("Restore login button",
//                event -> component.setEnabled(true));

        // Setting error to true also enables the login button.
//        Button showError = new Button("Show error",
//                event -> component.setError(true));

    }

}
