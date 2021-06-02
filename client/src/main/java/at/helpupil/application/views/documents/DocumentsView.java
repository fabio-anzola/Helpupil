package at.helpupil.application.views.documents;

import at.helpupil.application.utils.SecuredView;
import at.helpupil.application.utils.SessionStorage;
import at.helpupil.application.utils.responses.*;
import at.helpupil.application.utils.responses.Error;
import at.helpupil.application.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static at.helpupil.application.Application.BASE_URL;

@Route(value = "documents", layout = MainView.class)
@PageTitle("Documents")
@CssImport("./views/documents/documents-view.css")
public class DocumentsView extends SecuredView {

    private Button addDocument = new Button("Add New Document");

    private Grid<Document> documentGrid = new Grid<>(Document.class);
    private HorizontalLayout pagingMenuLayout = new HorizontalLayout();

    private int currentPage = 1;
    private Documents documents = getDocuments(currentPage);

    private Button previousPage = new Button("Previous");
    private Label currentPageText = new Label();
    private Button nextPage = new Button("Next");

    public DocumentsView() {
        addClassName("documents-view");

        addDocument.addClassName("add-document");
        Div addDocumentDiv = new Div(addDocument);
        add(addDocumentDiv);

        add(createDocumentGrid());

        add(createPagingMenu(documents.getTotalPages()));

        previousPage.addClickListener(e -> {
            if (currentPage > 1) {
                documents = getDocuments(currentPage - 1);
                currentPage = documents.getPage();
                updateSubjectPage();
            }
        });

        nextPage.addClickListener(e -> {
            if (currentPage < documents.getTotalPages()) {
                documents = getDocuments(currentPage + 1);
                currentPage = documents.getPage();
                updateSubjectPage();
            }
        });

    }

    private Grid<Document> createDocumentGrid() {
        List<Document> documentsList = new ArrayList<>();

        for (Document document : documents.getResults()) {
            document.setSubject(resolveSubjectById(document.getSubject()));
            document.setTeacher(resolveTeacherById(document.getTeacher()));
            document.setUser(resolveUserById(document.getUser()));
            documentsList.add(document);
        }

        documentGrid.setItems(documentsList);

        documentGrid.removeColumnByKey("reviewer");
        documentGrid.removeColumnByKey("file");
        documentGrid.removeColumnByKey("status");
        documentGrid.removeColumnByKey("id");
        documentGrid.setColumns("name", "type", "subject", "teacher", "rating", "user", "price");

        documentGrid.addComponentColumn(this::createBuyButton);

        documentGrid.addItemClickListener(item -> showDocumentDialog(item.getItem()));

        return documentGrid;
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
            //dialog.close();
            showBuyDialog(document);
        });

        cancelButton.addClickListener(e -> {
            dialog.close();
        });

        dialogButtonLayout.add(confirmRate, buyButton, cancelButton);

        dialogLayout.add(dialogHeading, dialogButtonLayout);

        dialog.add(dialogLayout);
        dialog.open();
    }

    private void showBuyDialog(Document document) {
        Dialog dialog = new Dialog();
        dialog.setWidth("25vw");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.addClassName("dialog-layout");

        Label dialogHeading = new Label("Purchase " + document.getName() + "?");

        Button buyButton = new Button("Buy");
        Button cancelButton = new Button("Cancel");

        HorizontalLayout dialogButtonLayout = new HorizontalLayout();

        buyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buyButton.addClickListener(e -> {
            makeBuyRequest(document);
        });

        cancelButton.addClickListener(e -> {
            dialog.close();
        });

        dialogButtonLayout.add(buyButton, cancelButton);

        dialogLayout.add(dialogHeading, dialogButtonLayout);

        dialog.add(dialogLayout);
        dialog.open();
    }

    private void makeBuyRequest(Document document) {
        byte[] bytes = Unirest.get(BASE_URL + "/content/" + document.getFile().getFilename())
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .asBytes()
                .getBody();


        final StreamResource streamResource= new StreamResource(document.getFile().getOriginalname(), () -> new ByteArrayInputStream(bytes));
        streamResource.setContentType(document.getFile().getMimetype());
        streamResource.setCacheTime(0);

        final StreamRegistration registration = UI.getCurrent()
                .getSession()
                .getResourceRegistry()
                .registerResource(streamResource);

        UI.getCurrent().getPage().open(
                String.valueOf(registration.getResourceUri()), "_blank"
        );

    }

    private Button createBuyButton(Document document) {
        return new Button("Buy", clickEvent -> showBuyDialog(document));
    }

    private void updateSubjectPage() {
        remove(documentGrid);
        remove(pagingMenuLayout);
        documentGrid = new Grid<>(Document.class);
        pagingMenuLayout = new HorizontalLayout();
        add(createDocumentGrid());
        add(createPagingMenu(documents.getTotalPages()));
    }

    private Component createPagingMenu(int totalPages) {
        pagingMenuLayout.addClassName("paging-layout");

        currentPageText.setText(currentPage + " / " + totalPages);

        pagingMenuLayout.add(previousPage, currentPageText, nextPage);

        return pagingMenuLayout;
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
        HttpResponse<UserPublicObj> user = Unirest.get(BASE_URL + "/users/public/" + id)
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .asObject(UserPublicObj.class);

        Error error = user.mapError(Error.class);

        if (null == error) {
            return user.getBody().getName();
        } else {
            return id;
        }
    }
}
