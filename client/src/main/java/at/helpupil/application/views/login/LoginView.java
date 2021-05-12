package at.helpupil.application.views.login;

import at.helpupil.application.utils.SessionStorage;
import at.helpupil.application.utils.requests.Login;
import at.helpupil.application.utils.responses.Error;
import at.helpupil.application.utils.responses.User;
import at.helpupil.application.views.main.MainView;
import com.google.gson.Gson;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import static at.helpupil.application.Application.BASE_URL;

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
            if (!email.getValue().trim().isEmpty() && !password.getValue().trim().isEmpty() && !email.isInvalid()) {
                makeLoginRequest(email.getValue().trim(), password.getValue().trim());
                clearForm();
            } else {
                Notification.show("Check your input");
            }
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
        login.addClickShortcut(Key.ENTER);
        buttonLayout.add(login);
        buttonLayout.add(clear);
        return buttonLayout;
    }

    private void makeLoginRequest(String email, String password) {
        HttpResponse<User> user = Unirest.post(BASE_URL + "/auth/login")
                .contentType("application/json")
                .body(new Login(email, password))
                .asObject(User.class);

        Error error = user.mapError(Error.class);

        if (null == error) {
            SessionStorage.set(user.getBody());
            UI.getCurrent().getPage().reload();
        } else {
            Notification.show("Check your Credentials");
        }
    }
}
