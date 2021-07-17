package at.helpupil.application.views.signup;

import at.helpupil.application.utils.Auth;
import at.helpupil.application.utils.OpenView;
import at.helpupil.application.utils.ThemeHelper;
import at.helpupil.application.utils.TooltipComp;
import at.helpupil.application.utils.requests.SignUp;
import at.helpupil.application.utils.responses.Error;
import at.helpupil.application.utils.responses.User;
import at.helpupil.application.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import static at.helpupil.application.Application.BASE_URL;

/**
 * This view is show to sign up
 */
@Route(value = "signup", layout = MainView.class)
@PageTitle("Sign Up")
@CssImport("./views/signup/sign-up-view.css")
public class SignUpView extends OpenView {

    /**
     * email of new user
     */
    private final EmailField email = new EmailField("Email address");
    /**
     * name of new user
     */
    private final TextField name = new TextField("Username");
    /**
     * password of new user
     */
    private final PasswordField password = new PasswordField("Password");

    /**
     * button to clear fields
     */
    private final Button clear = new Button("Clear");
    /**
     * button to sign up
     */
    private final Button signUp = new Button("Sign Up");


    /**
     * initializes Sign Up View
     */
    public SignUpView() {
        ThemeHelper.onLoad();

        addClassName("sign-up-view");

        add(createTitleLayout());
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

    /**
     * clears all fields in form
     */
    private void clearForm() {
        email.setValue("");
        name.setValue("");
        password.setValue("");
    }

    /**
     * @return title-layout for tile & tooltip
     */
    private Component createTitleLayout() {
        H3 title = new H3("Sign Up");
        title.addClassName("view-title");
        TooltipComp signUpTooltip = new TooltipComp("Email-verification required to access your account.");
        HorizontalLayout titleLayout = new HorizontalLayout(title, signUpTooltip);
        titleLayout.addClassName("view-title-layout");
        return titleLayout;
    }

    /**
     * @return layout for form
     */
    private Component createFormLayout() {
        VerticalLayout formLayout = new VerticalLayout();
        formLayout.addClassName("form-layout");
        email.setErrorMessage("Please enter a valid email address");
        formLayout.add(email, name, password);
        return formLayout;
    }

    /**
     * @return layout for buttons
     */
    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        signUp.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        signUp.addClickShortcut(Key.ENTER);
        buttonLayout.add(signUp);
        buttonLayout.add(clear);
        return buttonLayout;
    }

    /**
     * makes sign up request to api to register a new user
     * @param email of new user
     * @param name of new user
     * @param password of new user
     */
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

    /**
     * sends verification email to user after sign up
     * @param user which will get a verification email
     */
    private void sendVerifyEmail(User user) {
        Unirest.post(BASE_URL + "/auth/send-verification-email")
                .queryString("token", user.getTokens().getAccess().getToken())
                .header("Authorization", "Bearer " + user.getTokens().getAccess().getToken())
                .asEmpty();
    }
}
