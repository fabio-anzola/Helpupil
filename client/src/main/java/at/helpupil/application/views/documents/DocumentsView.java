package at.helpupil.application.views.documents;

import at.helpupil.application.utils.SecuredView;
import at.helpupil.application.utils.SessionStorage;
import at.helpupil.application.utils.responses.Document;
import at.helpupil.application.utils.responses.Documents;
import at.helpupil.application.utils.responses.Error;
import at.helpupil.application.utils.responses.Subjects;
import at.helpupil.application.views.main.MainView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static at.helpupil.application.Application.BASE_URL;

@Route(value = "documents", layout = MainView.class)
@PageTitle("Documents")
@CssImport("./views/documents/documents-view.css")
public class DocumentsView extends SecuredView {

    private Button addDocument = new Button("Add New Document");

    private HorizontalLayout pagingMenuLayout = new HorizontalLayout();

    private int currentPage = 1;
    private Documents documents = getDocuments(currentPage);

    private Button previousPage = new Button("Previous");
    private Label currentPageText = new Label();
    private Button nextPage = new Button("Next");

    public DocumentsView() {
        addClassName("documents-view");

        add(addDocument);

        add(createDocumentGrid());
    }

    private Grid<Document> createDocumentGrid() {
        Grid<Document> grid = new Grid<>(Document.class);

        List<Document> doc = new ArrayList<>(
                Arrays.asList(documents.getResults())
        );

        grid.setItems(doc);

        grid.removeColumnByKey("reviewer");
        grid.removeColumnByKey("file");
        grid.removeColumnByKey("status");
        grid.removeColumnByKey("id");
        grid.setColumns("name", "type", "user", "rating", "subject", "teacher");

        return grid;
    }

    private Documents getDocuments(int page) {
        HttpResponse<Documents> documents = Unirest.get(BASE_URL + "/documents")
                .queryString("page", page)
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .asObject(Documents.class);

        Error error = documents.mapError(Error.class);

        if (null == error) {
            return documents.getBody();
        } else {
            Notification.show(error.getMessage());
        }
        return null;
    }
}
