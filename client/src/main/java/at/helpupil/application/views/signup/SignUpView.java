package at.helpupil.application.views.signup;

import at.helpupil.application.utils.Auth;
import at.helpupil.application.utils.OpenView;
import at.helpupil.application.utils.SessionStorage;
import at.helpupil.application.utils.ThemeHelper;
import at.helpupil.application.utils.requests.Login;
import at.helpupil.application.utils.requests.SignUp;
import at.helpupil.application.utils.responses.Error;
import at.helpupil.application.utils.responses.User;
import at.helpupil.application.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
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
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import static at.helpupil.application.Application.BASE_URL;

@Route(value = "signup", layout = MainView.class)
@PageTitle("Sign Up")
@CssImport("./views/signup/sign-up-view.css")
public class SignUpView extends OpenView {

    private EmailField email = new EmailField("Email address");
    private TextField name = new TextField("Username: ");
    private PasswordField password = new PasswordField("Password");

    private Button clear = new Button("Clear");
    private Button signUp = new Button("Sign Up");


    public SignUpView() {
        ThemeHelper.onLoad();

        addClassName("sign-up-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());

        clearForm();

        clear.addClickListener(e -> clearForm());
        signUp.addClickListener(e -> {
            if (!email.getValue().trim().isEmpty() && !email.isInvalid() && !name.getValue().trim().isEmpty() && !password.getValue().trim().isEmpty()) {
                makeSignUpRequest(email.getValue().trim(), name.getValue().trim(), password.getValue().trim());
                clearForm();
            } else {
                Notification.show("Check your input");
            }
        });
    }

    private void clearForm() {
        email.setValue("");
        name.setValue("");
        password.setValue("");
    }

    private Component createTitle() {
        return new H3("Sign Up");
    }

    private Component createFormLayout() {
        VerticalLayout formLayout = new VerticalLayout();
        formLayout.addClassName("form-layout");
        email.setErrorMessage("Please enter a valid email address");
        formLayout.add(email, name, password);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        signUp.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        signUp.addClickShortcut(Key.ENTER);
        buttonLayout.add(signUp);
        buttonLayout.add(clear);
        return buttonLayout;
    }

    private void makeSignUpRequest(String email, String name, String password) {
        HttpResponse<User> user = Unirest.post(BASE_URL + "/auth/register")
                .contentType("application/json")
                .body(new SignUp(email, name, password))
                .asObject(User.class);

        Error error = user.mapError(Error.class);

        if (null == error) {
            sendVerifyEmail(user.getBody());
            Auth.redirectOnRegister();
        } else {
            Notification.show(error.getMessage());
        }
    }

    private void sendVerifyEmail(User user) {
        Unirest.post(BASE_URL + "/auth/send-verification-email")
                .queryString("token", user.getTokens().getAccess().getToken())
                .header("Authorization", "Bearer " + user.getTokens().getAccess().getToken())
                .asEmpty();
    }
}
