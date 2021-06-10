package at.helpupil.application.views.error;

import at.helpupil.application.utils.OpenView;
import at.helpupil.application.views.main.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.router.*;

import javax.servlet.http.HttpServletResponse;

@Route(value = "error", layout = MainView.class)
@PageTitle("404 Page")
@CssImport("./views/moderator/moderator-view.css")
public class ErrorView extends OpenView {
    public ErrorView() {
        Button button = new Button();
        add(button);
    }
}

class RouteNotFound extends RouteNotFoundError {
    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
        event.rerouteTo(ErrorView.class);
        return HttpServletResponse.SC_NOT_FOUND;
    }
}