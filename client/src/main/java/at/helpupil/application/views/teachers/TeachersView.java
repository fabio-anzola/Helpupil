package at.helpupil.application.views.teachers;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import at.helpupil.application.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;

@Route(value = "teachers", layout = MainView.class)
@PageTitle("Teachers")
@CssImport("./views/teachers/teachers-view.css")
public class TeachersView extends Div {

    public TeachersView() {
        addClassName("teachers-view");
        add(new Text("Content placeholder"));
    }

}
