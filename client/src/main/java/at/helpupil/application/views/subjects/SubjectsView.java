package at.helpupil.application.views.subjects;

import at.helpupil.application.utils.SecuredView;
import at.helpupil.application.views.main.MainView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "subjects", layout = MainView.class)
@PageTitle("Subjects")
@CssImport("./views/subjects/subjects-view.css")
public class SubjectsView extends SecuredView {

    public SubjectsView() {
        addClassName("subjects-view");
        add(new Text("Content placeholder"));
    }

}
