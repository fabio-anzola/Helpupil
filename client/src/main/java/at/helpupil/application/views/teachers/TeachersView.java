package at.helpupil.application.views.teachers;

import at.helpupil.application.utils.SecuredView;
import at.helpupil.application.views.main.MainView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "teachers", layout = MainView.class)
@PageTitle("Teachers")
@CssImport("./views/teachers/teachers-view.css")
public class TeachersView extends SecuredView {

    public TeachersView() {
        addClassName("teachers-view");
        add(new Text("Content placeholder"));
    }

}
