package at.helpupil.application.views.login;

import at.helpupil.application.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "login", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Login")
@CssImport("./views/login/login-view.css")
public class LoginView extends Div {

    private EmailField email = new EmailField("Email address");
    private PasswordField password = new PasswordField("Password");
    private Button forgotPassword = new Button("Forgot Password");

    private Button clear = new Button("Clear");
    private Button login = new Button("Login");


    public LoginView() {
        addClassName("login-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());

        clearForm();

        clear.addClickListener(e -> clearForm());
        login.addClickListener(e -> {
            Notification.show("Logged stored.");
            clearForm();
        });
        forgotPassword.addClickListener(e -> {
            Notification.show("You forgot your password.");
        });
    }

    private void clearForm() {
        email.setValue("");
        password.setValue("");
    }

    private Component createTitle() {
        return new H3("Login");
    }

    private Component createFormLayout() {
        VerticalLayout formLayout = new VerticalLayout();
        formLayout.addClassName("form-layout");
        email.setErrorMessage("Please enter a valid email address");
        forgotPassword.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
        formLayout.add(email, password, forgotPassword);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        login.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(login);
        buttonLayout.add(clear);
        return buttonLayout;
    }
}
