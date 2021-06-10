package at.helpupil.application.views.error;

import at.helpupil.application.utils.Auth;
import at.helpupil.application.utils.OpenView;
import at.helpupil.application.views.login.LoginView;
import at.helpupil.application.views.main.MainView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.*;

import javax.servlet.http.HttpServletResponse;

@Route(value = "error", layout = MainView.class)
@PageTitle("404 Page")
@CssImport("./views/moderator/moderator-view.css")
public class ErrorView extends OpenView {
    public ErrorView() {
        Div errorDiv = new Div();
        errorDiv.addClassName("error-div");

        H1 errorHeading = new H1("404 Page not found");

        Button button = new Button("Back to login");
        button.addClickListener(e -> UI.getCurrent().getPage().executeJs("window.location = \"" + Auth.getURL() + "/login\""));

        errorDiv.add(errorHeading, button);

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