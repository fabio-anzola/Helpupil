package at.helpupil.application.views.documents;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import at.helpupil.application.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;

@Route(value = "documents", layout = MainView.class)
@PageTitle("Documents")
@CssImport("./views/documents/documents-view.css")
public class DocumentsView extends Div {

    public DocumentsView() {
        addClassName("documents-view");
        add(new Text("Content placeholder"));
    }

}
