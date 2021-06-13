package at.helpupil.application.views.error;

import at.helpupil.application.utils.Auth;
import at.helpupil.application.utils.OpenView;
import at.helpupil.application.utils.ThemeHelper;
import at.helpupil.application.views.main.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.*;

import javax.servlet.http.HttpServletResponse;

/**
 * This view is shown to the user if the page can not be found
 */
@Route(value = "errorpage", layout = MainView.class)
@PageTitle("404 Page")
@CssImport("./views/error/error-view.css")
public class ErrorView extends OpenView {
    /**
     * User can click on a button to get back to login
     */
    public ErrorView() {
        ThemeHelper.onLoad();

        addClassName("error-view");


        Div errorDiv = new Div();
        errorDiv.addClassName("error-div");

        H1 errorHeading = new H1("404 Page not found");

        Button loginRedirectButton = new Button("Back to login");
        loginRedirectButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        loginRedirectButton.addClickListener(e -> UI.getCurrent().getPage().executeJs("window.location = \"" + Auth.getURL() + "/login\""));

        errorDiv.add(errorHeading, loginRedirectButton);


        add(errorDiv);
    }
}

class RouteNotFound extends RouteNotFoundError {
    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
        event.rerouteTo(ErrorView.class);
        return HttpServletResponse.SC_NOT_FOUND;
    }
}