package at.helpupil.application.views.subjects;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import at.helpupil.application.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;

@Route(value = "subjects", layout = MainView.class)
@PageTitle("Subjects")
@CssImport("./views/subjects/subjects-view.css")
public class SubjectsView extends Div {

    public SubjectsView() {
        addClassName("subjects-view");
        add(new Text("Content placeholder"));
    }

}
