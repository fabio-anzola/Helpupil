package at.helpupil.application.views.moderator;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import at.helpupil.application.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;

@Route(value = "moderator", layout = MainView.class)
@PageTitle("Moderator")
@CssImport("./views/moderator/moderator-view.css")
public class ModeratorView extends Div {

    public ModeratorView() {
        addClassName("moderator-view");
        add(new Text("Content placeholder"));
    }

}
