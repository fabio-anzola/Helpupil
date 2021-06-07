package at.helpupil.application.views.documents;

import at.helpupil.application.utils.SecuredView;
import at.helpupil.application.utils.SessionStorage;
import at.helpupil.application.utils.StarObj;
import at.helpupil.application.utils.responses.Error;
import at.helpupil.application.utils.responses.*;
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
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static at.helpupil.application.Application.BASE_URL;
import static at.helpupil.application.utils.Resolve.*;

@Route(value = "documents", layout = MainView.class)
@PageTitle("Documents")
@CssImport("./views/documents/documents-view.css")
public class DocumentsView extends SecuredView implements HasUrlParameter<String> {

    private Button addDocument = new Button("Add New Document");

    private Grid<Document> documentGrid = new Grid<>(Document.class);
    private HorizontalLayout pagingMenuLayout = new HorizontalLayout();

    private final int[] limits = new int[]{10, 15, 25};
    private int limit = limits[0];
    private int currentPage = 1;
    private Documents documents = getDocuments(limit, currentPage);

    public DocumentsView() {
        addClassName("documents-view");

        addDocument.addClassName("add-document");
        Div addDocumentDiv = new Div(addDocument);
        add(addDocumentDiv);

        add(createDocumentGrid());

        add(createPagingMenu(documents.getTotalPages()));

        addDocument.addClickListener(e -> {
//            System.out.println("type = " + type);
//            System.out.println("value = " + value);
//            documents = getDocuments(limit, currentPage);
//            UI.getCurrent().getPage().reload();
            //showBuyDialog();
        });
    }

    private Grid<Document> createDocumentGrid() {
        List<Document> documentsList = new ArrayList<>();

        for (Document document : documents.getResults()) {
            if (document.getType().length() > 0) {
                document.setType(document.getType().substring(0, 1).toUpperCase() + document.getType().substring(1));
            }
            document.setSubject(document.getSubject_sn());
            document.setTeacher(document.getTeacher_sn());
            document.setUser(document.getUname());
            documentsList.add(document);
        }

        documentGrid.setItems(documentsList);

        documentGrid.removeColumnByKey("reviewer");
        documentGrid.removeColumnByKey("file");
        documentGrid.removeColumnByKey("status");
        documentGrid.removeColumnByKey("id");
        documentGrid.setColumns("name", "type", "subject", "teacher", "rating", "user", "price");

        documentGrid.addComponentColumn(this::createBuyOrShowButton);

        documentGrid.addItemClickListener(item -> showDocumentDialog(item.getItem()));

        return documentGrid;
    }

    private void showUploadDialog() {
        Dialog dialog = new Dialog();
        dialog.setWidth("40vw");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.addClassName("dialog-layout");

        Select<Teacher> teacherSelect = new Select<>();
        Select<Subject> subjectSelect = new Select<>();
        Select<?> typeSelect = new Select<>();

        Label dialogHeading = new Label("Upload document");

        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");

        HorizontalLayout dialogButtonLayout = new HorizontalLayout();

        confirmButton.addClickListener(e -> {
            Notification.show("You uploaded a document");
        });

        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        cancelButton.addClickListener(e -> {
            dialog.close();
        });

        dialogButtonLayout.add(confirmButton, cancelButton);

        dialogLayout.add(dialogHeading, dialogButtonLayout);

        dialog.add(dialogLayout);
        dialog.open();
    }

    private void showDocumentDialog(Document document) {
        Dialog dialog = new Dialog();
        dialog.setWidth("40vw");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.addClassName("dialog-layout");

        Label dialogHeading = new Label(document.getName());
        dialogLayout.add(dialogHeading);

        Button buyOrShowButton = createBuyOrShowButton(document);
        Button cancelButton = new Button("Cancel");


        if (Arrays.stream(document.getReviewer()).noneMatch(n -> n.equals(SessionStorage.get().getUser().getId()))) {
            HorizontalLayout ratingLayout = new HorizontalLayout();
            Div stars = new Div();
            replaceStars(stars, 0);
            Button confirmRate = new Button("Confirm");
            confirmRate.addClickListener(e -> {
                makeRatingRequest(document.getId(), getCurrentRate(stars));
                dialog.close();
            });
            ratingLayout.add(stars, confirmRate);
            dialogLayout.add(ratingLayout);
        }


        HorizontalLayout dialogButtonLayout = new HorizontalLayout();

        buyOrShowButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        cancelButton.addClickListener(e -> {
            dialog.close();
        });

        dialogButtonLayout.add(buyOrShowButton, cancelButton);
        dialogLayout.add(dialogButtonLayout);


        dialog.add(dialogLayout);
        dialog.open();
    }

    private int getCurrentRate(Div starDiv) {
        int[] currentRate = new int[1];
        starDiv.getChildren().forEach(n -> {
            if (((StarObj) n).getState()) {
                currentRate[0]++;
            }
        });

        return currentRate[0];
    }

    private void makeRatingRequest(String documentId, int rating) {
        HttpResponse<Document> document = Unirest.patch(BASE_URL + "/rating/" + documentId)
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .queryString("rating", rating)
                .asObject(Document.class);

        Error error = document.mapError(Error.class);

        if (null == error) {
            Notification.show("Rated document. Thank you!");

            documents = getDocuments(limit, currentPage);
            updateDocumentPage();
//            UI.getCurrent().getPage().reload();
        } else {
            Notification.show(error.getMessage());
        }
    }

    private void replaceStars(Div starDiv, int starIndex) {
        starDiv.removeAll();
        ArrayList<StarObj> starList = generateStars(starDiv, starIndex);
        starList.forEach(starDiv::add);
    }

    private ArrayList<StarObj> generateStars(Div starDiv, int starIndex) {
        ArrayList<StarObj> newStarList = new ArrayList<>();

        for (int i = 0; i < starIndex; i++) {
            StarObj filledStar = new StarObj(true);
            int newIndex = i + 1;
            filledStar.addClickListener(e -> {
                replaceStars(starDiv, newIndex);
            });
            newStarList.add(filledStar);
        }

        for (int i = starIndex; i < 5; i++) {
            StarObj emptyStar = new StarObj(false);
            int newIndex = i + 1;
            emptyStar.addClickListener(e -> {
                replaceStars(starDiv, newIndex);
            });
            newStarList.add(emptyStar);
        }

        return newStarList;
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
        if (!isBuyRequestAllowed(document)) {
            return;
        }

        byte[] bytes = Unirest.get(BASE_URL + "/content/" + document.getFile().getFilename())
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .asBytes()
                .getBody();


        final StreamResource streamResource = new StreamResource(document.getFile().getOriginalname(), () -> new ByteArrayInputStream(bytes));
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

    private boolean isBuyRequestAllowed(Document document) {
        Error error = Unirest.get(BASE_URL + "/content/" + document.getFile().getFilename())
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .asObject(Error.class)
                .getBody();

        if (null == error) {
            return true;
        } else {
            Notification.show(error.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }
    }

    private Button createBuyOrShowButton(Document document) {
        if (SessionStorage.get().getUser().getPurchasedDocuments().contains(document.getId())) {
            return new Button("Show", clickEvent -> makeBuyRequest(document));
        } else {
            return new Button("Buy", clickEvent -> showBuyDialog(document));
        }
    }

    private void updateDocumentPage() {
        remove(documentGrid);
        remove(pagingMenuLayout);
        documentGrid = new Grid<>(Document.class);
        pagingMenuLayout = new HorizontalLayout();
        add(createDocumentGrid());
        add(createPagingMenu(documents.getTotalPages()));
    }

    private Component createPagingMenu(int totalPages) {
        pagingMenuLayout.addClassName("paging-layout");


        Button previousPage = new Button("Previous");
        previousPage.addClickListener(e -> {
            if (currentPage > 1) {
                documents = getDocuments(limit, currentPage - 1);
                currentPage = documents.getPage();
                updateDocumentPage();
            }
        });

        Select<String> itemsPerPageSelect = new Select<>();
        itemsPerPageSelect.addClassName("paging-items-per-page-select");
        itemsPerPageSelect.setItems(Arrays.stream(limits)
                .mapToObj(String::valueOf)
                .toArray(String[]::new));
        itemsPerPageSelect.setValue(String.valueOf(limit));
        itemsPerPageSelect.addValueChangeListener(e -> {
            limit = Integer.parseInt(e.getValue());
            currentPage = 1;
            documents = getDocuments(limit, currentPage);
            currentPage = documents.getPage();
            updateDocumentPage();
        });

        Button nextPage = new Button("Next");
        nextPage.addClickListener(e -> {
            if (currentPage < documents.getTotalPages()) {
                documents = getDocuments(limit, currentPage + 1);
                currentPage = documents.getPage();
                updateDocumentPage();
            }
        });

        Label currentPageText = new Label();
        currentPageText.setText(currentPage + " / " + totalPages);


        pagingMenuLayout.add(previousPage, currentPageText, nextPage, itemsPerPageSelect);

        return pagingMenuLayout;
    }

    private Documents getDocuments(int limit, int page, String k, String v) {
        HttpResponse<Documents> documents = null;
        if (null != k && null != v) {
            if (k.equals("subject")) {
                v = resolveSubjectByShortname(v);
            }
            if (k.equals("teacher")) {
                v = resolveTeacherByShortname(v);
            }

            documents = Unirest.get(BASE_URL + "/documents")
                    .queryString("limit", limit)
                    .queryString("page", page)
                    .queryString(k, v)
                    .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                    .asObject(Documents.class);
        } else {
            getDocuments(limit, page);
        }

        Error error = documents.mapError(Error.class);

        if (null == error) {
            return documents.getBody();
        } else {
            Notification.show(error.getMessage());
        }
        return null;
    }

    private Documents getDocuments(int limit, int page) {
        HttpResponse<Documents> documents = Unirest.get(BASE_URL + "/documents")
                .queryString("limit", limit)
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

    @Override
    public void setParameter(BeforeEvent beforeEvent, @WildcardParameter String s) {
        if (!s.isEmpty() && s.split("/").length == 2) {
            this.documents = getDocuments(this.limit, 1, s.split("/")[0], s.split("/")[1]);
            updateDocumentPage();
        } else {
            this.documents = getDocuments(this.limit, 1);
            updateDocumentPage();
        }
    }
}
