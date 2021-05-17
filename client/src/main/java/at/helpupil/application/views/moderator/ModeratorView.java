package at.helpupil.application.views.moderator;

import at.helpupil.application.utils.SecuredView;
import at.helpupil.application.views.main.MainView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "moderator", layout = MainView.class)
@PageTitle("Moderator")
@CssImport("./views/moderator/moderator-view.css")
public class ModeratorView extends SecuredView {

    public ModeratorView() {
        addClassName("moderator-view");
        add(new Text("Content placeholder"));
    }

}
