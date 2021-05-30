package at.helpupil.application.views.documents;

import at.helpupil.application.utils.SecuredView;
import at.helpupil.application.utils.SessionStorage;
import at.helpupil.application.utils.responses.*;
import at.helpupil.application.utils.responses.Error;
import at.helpupil.application.views.main.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.util.ArrayList;
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

        List<Document> doc = new ArrayList<>();

        for (Document document: documents.getResults()) {
            document.setSubject(resolveSubjectById(document.getSubject()));
            document.setTeacher(resolveTeacherById(document.getTeacher()));
            document.setUser(resolveUserById(document.getUser()));
            doc.add(document);
        }

        grid.setItems(doc);

        grid.removeColumnByKey("reviewer");
        grid.removeColumnByKey("file");
        grid.removeColumnByKey("status");
        grid.removeColumnByKey("id");
        grid.setColumns("name", "type", "subject", "teacher", "rating", "user");

        grid.addComponentColumn(item -> createRateButton());
        grid.addComponentColumn(item -> createBuyButton());

        grid.addItemClickListener(item -> showDocumentDialog(item.getItem()));

        return grid;
    }

    private void showDocumentDialog(Document document) {
        Dialog dialog = new Dialog();
        dialog.setWidth("40vw");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.addClassName("dialog-layout");

        Label dialogHeading = new Label(document.getName());

        Button confirmRate = new Button("Confirm");
        Button buyButton = new Button("Buy");
        Button cancelButton = new Button("Cancel");

        HorizontalLayout dialogButtonLayout = new HorizontalLayout();

        confirmRate.addClickListener(e -> {
            //rate document()
            Notification.show("You rated a document");
        });

        buyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buyButton.addClickListener(e -> {
            //buy document()
            Notification.show("You purchased a document");
        });

        cancelButton.addClickListener(e -> {
            dialog.close();
        });

        dialogButtonLayout.add(confirmRate, buyButton, cancelButton);

        dialogLayout.add(dialogHeading, dialogButtonLayout);

        dialog.add(dialogLayout);
        dialog.open();
    }

    private Button createBuyButton() {
        return new Button("Buy", clickEvent -> Notification.show("You purchased a document"));
    }

    private Button createRateButton() {
        return new Button("Rate", clickEvent -> Notification.show("You rated a document"));
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

    private String resolveSubjectById(String id) {
        HttpResponse<Subject> subject = Unirest.get(BASE_URL + "/subject/" + id)
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .asObject(Subject.class);

        Error error = subject.mapError(Error.class);

        if (null == error) {
            return subject.getBody().getShortname();
        } else {
            return id;
        }
    }

    private String resolveTeacherById(String id) {
        HttpResponse<Teacher> teacher = Unirest.get(BASE_URL + "/teacher/" + id)
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .asObject(Teacher.class);

        Error error = teacher.mapError(Error.class);

        if (null == error) {
            return teacher.getBody().getShortname();
        } else {
            return id;
        }
    }

    private String resolveUserById(String id) {
        HttpResponse<UserObj> user = Unirest.get(BASE_URL + "/users/" + id)
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .asObject(UserObj.class);

        Error error = user.mapError(Error.class);

        if (null == error) {
            return user.getBody().getName();
        } else {
            return id;
        }
    }
}
