package at.helpupil.application.views.documents;

import at.helpupil.application.utils.*;
import at.helpupil.application.utils.responses.Error;
import at.helpupil.application.utils.responses.*;
import at.helpupil.application.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import kong.unirest.HttpResponse;
import kong.unirest.MultipartBody;
import kong.unirest.Unirest;

import java.io.ByteArrayInputStream;
import java.util.*;

import static at.helpupil.application.Application.BASE_URL;
import static at.helpupil.application.utils.Resolve.*;
import static at.helpupil.application.utils.ResponsiveUI.getLayoutMode;

/**
 * Here the users can see all approved documents
 * Users who aren't logged in are redirected to login
 */
@Route(value = "documents", layout = MainView.class)
@PageTitle("Documents")
@CssImport("./views/documents/documents-view.css")
public class DocumentsView extends SecuredView implements HasUrlParameter<String> {
    /**
     * grid of documents
     */
    private Grid<Document> documentGrid = new Grid<>(Document.class);
    /**
     * layout for paging menu
     */
    private HorizontalLayout pagingMenuLayout = new HorizontalLayout();

    /**
     * used to filter documents
     */
    private String[] filter;
    /**
     * true if user searches
     */
    private boolean searchState = false;
    /**
     * list of found ids
     */
    private final ArrayList<String> foundIds = new ArrayList<>();
    /**
     * limits for user to choose for paging
     */
    private final int[] limits = new int[]{10, 15, 25};
    /**
     * default value for paging
     */
    private int limit = limits[0];
    /**
     * current page for paging
     */
    private int currentPage = 1;
    /**
     * all approved documents
     */
    private Documents documents = getDocuments(currentPage);

    /**
     * initializes View
     * checks if darkmode is stored in session
     */
    public DocumentsView() {
        ThemeHelper.onLoad();

        addClassName("documents-view");

        add(createTopDiv());
        add(createDocumentGrid());
        add(createPagingMenu(documents.getTotalPages()));
    }

    /**
     * @return top div for button to upload document
     *      + searchbox to filter documents
     */
    private Component createTopDiv() {
        Div topDiv = new Div();
        topDiv.addClassName("top-div-doc");


        Div emptyDiv = new Div();


        Button addDocument = new Button("Add New Document");
        addDocument.addClassName("add-document");
        Div addDocumentDiv = new Div(addDocument);
        addDocumentDiv.addClassName("add-document-div");
        addDocument.addClickListener(e -> showUploadDialog());


        Div innerDiv = new Div();
        innerDiv.addClassName("search-inner-div");

        TextField searchBox = new TextField();
        searchBox.setPlaceholder("Search");
        searchBox.setClearButtonVisible(true);
        searchBox.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);
        searchBox.addKeyDownListener(Key.ESCAPE, e -> searchBox.blur());
        searchBox.addKeyDownListener(Key.ENTER, e -> makeSearchRequest(searchBox.getValue()));

        Icon searchIcon = new Icon(VaadinIcon.SEARCH);
        searchIcon.addClickListener(e -> makeSearchRequest(searchBox.getValue()));

        Icon exitSearchState = new Icon(VaadinIcon.CLOSE_BIG);
        exitSearchState.addClickListener(e -> {
            searchBox.clear();
            if (searchState) {
                searchState = false;
                currentPage = 1;
                if (filter == null) {
                    documents = getDocuments(currentPage);
                } else {
                    String[] resolvedFilter = resolveFilter();
                    documents = getDocuments(currentPage, resolvedFilter[0], resolvedFilter[1]);
                }
                updateDocumentPage();
            }
        });

        innerDiv.add(searchIcon, searchBox, exitSearchState);


        topDiv.add(emptyDiv, addDocumentDiv, innerDiv);
        return topDiv;
    }

    /**
     * @return resolved filter: objects instead of shortnames
     */
    private String[] resolveFilter() {
        String k = filter[0];
        String v = filter[1];

        if (null != k && null != v) {
            if (k.equals("subject")) {
                v = resolveSubjectByShortname(v);
            }
            if (k.equals("teacher")) {
                v = resolveTeacherByShortname(v);
            }
        }
        return new String[]{k, v};
    }

    /**
     * now only documents which apply the filter are shown
     * @param searchText to filter for
     */
    private void makeSearchRequest(String searchText) {
        searchText = searchText.toLowerCase();
        foundIds.clear();

        String[] resolvedFilter = new String[2];
        if (filter != null) {
            resolvedFilter = resolveFilter();
        }

        int pageIndex = 0;
        int pages = 1;
        do {
            pageIndex++;
            HttpResponse<Documents> documents;

            if (filter == null) {
                documents = Unirest.get(BASE_URL + "/documents")
                        .queryString("page", pageIndex)
                        .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                        .asObject(Documents.class);
            } else {
                documents = Unirest.get(BASE_URL + "/documents")
                        .queryString("page", pageIndex)
                        .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                        .queryString(resolvedFilter[0], resolvedFilter[1])
                        .asObject(Documents.class);
            }

            Error error = documents.mapError(Error.class);

            if (null == error) {
                if (pages < documents.getBody().getTotalPages()) {
                    pages = documents.getBody().getTotalPages();
                }

                String finalSearchText = searchText;
                Arrays.stream(documents.getBody().getResults()).forEach(n -> {
                    if (n.getName().toLowerCase().contains(finalSearchText)) {
                        foundIds.add(n.getId());
                    }
                });
            } else {
                pageIndex--;
                new Error(error.getCode(), error.getMessage()).continueCheck();
            }
        } while (pageIndex != pages);


        searchState = true;
        currentPage = 1;
        documents = getDocuments(currentPage);
        updateDocumentPage();
    }

    /**
     * @return grid of documents
     */
    private Grid<Document> createDocumentGrid() {
        List<Document> documentsList = new ArrayList<>();

        for (Document document : documents.getResults()) {
            if (document.getType().length() > 0) {
                document.setType(document.getType().substring(0, 1).toUpperCase() + document.getType().substring(1));
            }
            documentsList.add(document);
        }

        documentGrid.setItems(documentsList);

        documentGrid.removeColumnByKey("reviewer");
        documentGrid.removeColumnByKey("file");
        documentGrid.removeColumnByKey("status");
        documentGrid.removeColumnByKey("id");

        documentGrid.setColumns("name", "type", "subject_sn", "teacher_sn", "rating", "uname", "price");

        documentGrid.getColumnByKey("uname").setHeader("User");
        documentGrid.getColumnByKey("subject_sn").setHeader("Subject");
        documentGrid.getColumnByKey("teacher_sn").setHeader("Teacher");

        documentGrid.addItemClickListener(item -> showDocumentDialog(item.getItem()));
        documentGrid.addComponentColumn(this::createBuyOrShowButton);

        return documentGrid;
    }

    /**
     * display upload dialog to upload a document
     */
    private void showUploadDialog() {
        Dialog dialog = new Dialog();
        if (getLayoutMode() == ResponsiveUI.LayoutMode.SMALL) {
            dialog.setWidth("100vw");
        } else {
            dialog.setWidth("40vw");
        }

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.addClassName("dialog-layout");

        TextField name = new TextField("Name");

        Select<String> teacherSelect = new Select<>();
        Select<String> subjectSelect = new Select<>();
        Select<String> typeSelect = new Select<>();

        Teachers teachers = getTeachers();
        if (teachers == null) return;
        Map<String, String> teacherMap = new HashMap<>();
        Arrays.stream(teachers.getResults()).forEach(n -> teacherMap.put(n.getShortname(), n.getId()));
        teacherSelect.setItems(teacherMap.keySet());
        teacherSelect.setLabel("Teacher");

        Subjects subjects = getSubjects();
        if (subjects == null) return;
        Map<String, String> subjectMap = new HashMap<>();
        Arrays.stream(subjects.getResults()).forEach(n -> subjectMap.put(n.getShortname(), n.getId()));
        subjectSelect.setItems(subjectMap.keySet());
        subjectSelect.setLabel("Subject");


        Types types = getTypes();
        if (types == null) return;
        Map<String, String> typeMap = new HashMap<>();
        for (int i = 0; i < types.getFriendly_values().length; i++) {
            typeMap.put(types.getFriendly_values()[i], types.getValues()[i]);
        }
        typeSelect.setItems(typeMap.keySet());
        typeSelect.setLabel("Type");

        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setMaxFileSize(200 * 1_000_000);
        upload.setMaxFiles(1);
        upload.setAcceptedFileTypes("application/pdf", "image/jpeg", "image/png");
        upload.addFileRejectedListener(error -> Notification.show(error.getErrorMessage()));

        Label dialogHeading = new Label("Upload document");

        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");

        HorizontalLayout dialogButtonLayout = new HorizontalLayout();

        confirmButton.addClickListener(e -> {
            if (teacherSelect.getValue().trim().isEmpty()
                    && subjectSelect.getValue().trim().isEmpty()
                    && typeSelect.getValue().trim().isEmpty()
                    && name.getValue().trim().isEmpty()) {
                Notification.show("Check your input");
            }
            int code = makeDocumentUploadRequest(name.getValue(), subjectMap.get(subjectSelect.getValue()), typeMap.get(typeSelect.getValue()), teacherMap.get(teacherSelect.getValue()), buffer);
            if (code == 0) {
                dialog.close();
            }
        });

        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        cancelButton.addClickListener(e -> dialog.close());

        dialogButtonLayout.add(confirmButton, cancelButton);

        dialogLayout.add(dialogHeading, name, teacherSelect, subjectSelect, typeSelect, upload, dialogButtonLayout);

        dialog.add(dialogLayout);
        dialog.open();
    }

    /**
     * here the user can rate or buy a document
     * @param document to which the dialog is displayed
     */
    private void showDocumentDialog(Document document) {
        Dialog dialog = new Dialog();
        if (getLayoutMode() == ResponsiveUI.LayoutMode.SMALL) {
            dialog.setWidth("100vw");
        } else {
            dialog.setWidth("40vw");
        }

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.addClassName("dialog-layout");

        Label dialogHeading = new Label(document.getName());
        dialogLayout.add(dialogHeading);

        Button buyOrShowButton = createBuyOrShowButton(document);
        buyOrShowButton.addClickListener(e -> dialog.close());
        Button cancelButton = new Button("Cancel");


        if (Arrays.stream(document.getReviewer()).noneMatch(n -> n.equals(SessionStorage.get().getUser().getId()))
                && SessionStorage.get().getUser().getPurchasedDocuments().contains(document.getId())) {
            HorizontalLayout ratingLayout = new HorizontalLayout();
            ratingLayout.addClassName("rating-layout");
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


        if (document.getUser().equals(SessionStorage.get().getUser().getId())) {
            Button deleteButton = new Button("Delete");
            deleteButton.addClassName("document-delete-decline-button");
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.addClickListener(e -> {
                dialog.close();
                showDeleteDialog(document);
            });
            dialogLayout.add(deleteButton);
        } else if (SessionStorage.get().getUser().getRole().equals("moderator")) {
            Button declineButton = new Button("Decline");
            declineButton.addClassName("document-delete-decline-button");
            declineButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            declineButton.addClickListener(e -> {
                dialog.close();
                showDeclineDialog(document);
            });
            dialogLayout.add(declineButton);
        }


        HorizontalLayout dialogButtonLayout = new HorizontalLayout();

        buyOrShowButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        cancelButton.addClickListener(e -> dialog.close());

        dialogButtonLayout.add(buyOrShowButton, cancelButton);
        dialogLayout.add(dialogButtonLayout);


        dialog.add(dialogLayout);
        dialog.open();
    }

    /**
     * uploads a file to the database
     * @param name of new document
     * @param subject of new document
     * @param type of new document
     * @param teacher of new document
     * @param buffer file data of new document
     * @return 0 if upload was successful
     */
    private int makeDocumentUploadRequest(String name, String subject, String type, String teacher, MemoryBuffer buffer) {
        MultipartBody body = Unirest.post(BASE_URL + "/documents/base64/")
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .field("name", name)
                .field("type", type)
                .field("subject", subject)
                .field("teacher", teacher)
                .field("file", buffer.getInputStream(), buffer.getFileName());
        HttpResponse<at.helpupil.application.utils.responses.File> files = body.asObject(File.class);

        Error error = files.mapError(Error.class);

        if (null == error) {
            Notification.show("Thanks for uploading. Your document will be reviewed shortly.");
            return 0;
        } else {
            if (new Error(error.getCode(), error.getMessage()).continueCheck()) {
                return makeDocumentUploadRequest(name, subject, type, teacher, buffer);
            }
            return 1;
        }
    }

    /**
     * @param starDiv here are all stars
     * @return current rating
     */
    private int getCurrentRate(Div starDiv) {
        int[] currentRate = new int[1];
        starDiv.getChildren().forEach(n -> {
            if (((StarObj) n).getState()) {
                currentRate[0]++;
            }
        });

        return currentRate[0];
    }

    /**
     * makes request to api to update rating
     * @param documentId of document
     * @param rating of document
     */
    private void makeRatingRequest(String documentId, int rating) {
        HttpResponse<Document> document = Unirest.patch(BASE_URL + "/rating/" + documentId)
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .queryString("rating", rating)
                .asObject(Document.class);

        Error error = document.mapError(Error.class);

        if (null == error) {
            Notification.show("Rated document. Thank you!");

            documents = getDocuments(currentPage);
            updateDocumentPage();
        } else {
            if (new Error(error.getCode(), error.getMessage()).continueCheck()) {
                makeRatingRequest(documentId, rating);
            }
        }
    }

    /**
     * @param starDiv here are all stars
     * @param starIndex how many stars are rated
     */
    private void replaceStars(Div starDiv, int starIndex) {
        starDiv.removeAll();
        ArrayList<StarObj> starList = generateStars(starDiv, starIndex);
        starList.forEach(starDiv::add);
    }

    /**
     * @param starDiv here are all stars
     * @param starIndex how many stars the user decided to rate the document
     * @return list of stars which are filled and empty
     */
    private ArrayList<StarObj> generateStars(Div starDiv, int starIndex) {
        ArrayList<StarObj> newStarList = new ArrayList<>();

        for (int i = 0; i < starIndex; i++) {
            StarObj filledStar = new StarObj(true);
            int newIndex = i + 1;
            filledStar.addClickListener(e -> replaceStars(starDiv, newIndex));
            newStarList.add(filledStar);
        }

        for (int i = starIndex; i < 5; i++) {
            StarObj emptyStar = new StarObj(false);
            int newIndex = i + 1;
            emptyStar.addClickListener(e -> replaceStars(starDiv, newIndex));
            newStarList.add(emptyStar);
        }

        return newStarList;
    }

    /**
     * shows buy dialog to user so he can buy this document
     * @param document to show buy dialog
     */
    private void showBuyDialog(Document document) {
        Dialog dialog = new Dialog();
        if (getLayoutMode() == ResponsiveUI.LayoutMode.SMALL) {
            dialog.setWidth("100vw");
        } else {
            dialog.setWidth("25vw");
        }

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.addClassName("dialog-layout");

        Label dialogHeading = new Label("Purchase " + document.getName() + "?");

        Button buyButton = new Button("Buy");
        Button cancelButton = new Button("Cancel");

        HorizontalLayout dialogButtonLayout = new HorizontalLayout();

        buyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buyButton.addClickListener(e -> {
            makeBuyRequest(document);
            dialog.close();
        });

        cancelButton.addClickListener(e -> {
            dialog.close();
            showDocumentDialog(document);
        });

        dialogButtonLayout.add(buyButton, cancelButton);

        dialogLayout.add(dialogHeading, dialogButtonLayout);

        dialog.add(dialogLayout);
        dialog.open();
    }

    /**
     * show delete dialog to moderator so he can delete this document
     * @param document to show delete dialog
     */
    private void showDeleteDialog(Document document) {
        Dialog dialog = new Dialog();
        if (getLayoutMode() == ResponsiveUI.LayoutMode.SMALL) {
            dialog.setWidth("100vw");
        } else {
            dialog.setWidth("25vw");
        }

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.addClassName("dialog-layout");

        Label dialogHeading = new Label("Delete " + document.getName() + "?");

        Button deleteButton = new Button("Delete");
        Button cancelButton = new Button("Cancel");

        HorizontalLayout dialogButtonLayout = new HorizontalLayout();

        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(e -> {
            dialog.close();
            makeDeleteRequest(document.getId());
        });

        cancelButton.addClickListener(e -> {
            dialog.close();
            showDocumentDialog(document);
        });

        dialogButtonLayout.add(deleteButton, cancelButton);

        dialogLayout.add(dialogHeading, dialogButtonLayout);

        dialog.add(dialogLayout);
        dialog.open();
    }

    /**
     * shows decline dialog to moderator so he can decline this document
     * @param document to show decline dialog
     */
    private void showDeclineDialog(Document document) {
        Dialog dialog = new Dialog();
        if (getLayoutMode() == ResponsiveUI.LayoutMode.SMALL) {
            dialog.setWidth("100vw");
        } else {
            dialog.setWidth("25vw");
        }

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.addClassName("dialog-layout");

        Label dialogHeading = new Label("Decline " + document.getName() + "?");

        TextField declineMessage = new TextField("Decline Message");
        declineMessage.addClassName("decline-text-field");

        Button declineButton = new Button("Decline");
        Button cancelButton = new Button("Cancel");

        HorizontalLayout dialogButtonLayout = new HorizontalLayout();

        declineButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        declineButton.addClickListener(e -> {
            dialog.close();
            makeDeclineRequest(document.getId(), declineMessage.getValue());
        });

        cancelButton.addClickListener(e -> {
            dialog.close();
            showDocumentDialog(document);
        });

        dialogButtonLayout.add(declineButton, cancelButton);

        dialogLayout.add(dialogHeading, declineMessage, dialogButtonLayout);

        dialog.add(dialogLayout);
        dialog.open();
    }

    /**
     * makes request to api to delete the document from the database
     * @param documentId id of document
     */
    private void makeDeleteRequest(String documentId) {
        HttpResponse<Document> document = Unirest.delete(BASE_URL + "/documents/" + documentId)
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .asObject(Document.class);

        Error error = document.mapError(Error.class);

        if (null == error) {
            Notification.show("Document has been deleted!");
            documents = getDocuments(currentPage);
            if (documents == null) return;
            if (documents.getTotalResults() == 0) {
                currentPage = 0;
            }
            updateDocumentPage();
        } else {
            Notification.show(error.getMessage());
        }
    }

    /**
     * makes request to the api to decline the document
     * @param documentId id of document
     * @param declineMessage will be sent to the uploader so he knows why his document wasn't approved
     */
    private void makeDeclineRequest(String documentId, String declineMessage) {
        HttpResponse<Document> document = Unirest.patch(BASE_URL + "/mod/decline/" + documentId)
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .queryString("message", declineMessage)
                .asObject(Document.class);

        Error error = document.mapError(Error.class);

        if (null == error) {
            Notification.show("Document has been declined!");
            documents = getDocuments(currentPage);
            if (documents == null) return;
            if (documents.getTotalResults() == 0) {
                currentPage = 0;
            }
            updateDocumentPage();
        } else {
            Notification.show(error.getMessage());
        }
    }

    /**
     * makes request to api to buy the document
     * @param document which may be bought
     */
    private void makeBuyRequest(Document document) {
        if (!isBuyRequestAllowed(document)) {
            return;
        }

        byte[] bytes = Unirest.get(BASE_URL + "/content/" + document.getFile().getFilename())
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .asBytes()
                .ifFailure(e -> {
                    Error error = e.mapError(Error.class);
                    if (null != error) {
                        if (new Error(error.getCode(), error.getMessage()).continueCheck()) {
                            makeBuyRequest(document);
                        }
                    }
                })
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

        // update user obj - purchase documents
        fetchUserData(SessionStorage.get().getUser().getId());
    }

    /**
     * makes request to api to get user by id
     * @param id of user
     */
    private void fetchUserData(String id) {
        HttpResponse<UserObj> user = Unirest.get(BASE_URL + "/users/" + id)
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .asObject(UserObj.class);

        Error error = user.mapError(Error.class);

        if (null == error) {
            SessionStorage.update(user.getBody());
            updateDocumentPage();
        } else {
            if (new Error(error.getCode(), error.getMessage()).continueCheck()) {
                fetchUserData(id);
            }
        }
    }

    /**
     * asks api if buy request is allowed
     * @param document which may be bought
     * @return true if buy request is allowed
     */
    private boolean isBuyRequestAllowed(Document document) {
        Error error = Unirest.get(BASE_URL + "/content/" + document.getFile().getFilename())
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .asObject(Error.class)
                .getBody();

        if (null == error) {
            return true;
        } else {
            if (new Error(error.getCode(), error.getMessage()).continueCheck()) {
                return isBuyRequestAllowed(document);
            }
            return false;
        }
    }

    /**
     * @param document where the button may be buy or show
     * @return show button in the document grid if document has already been bought
     */
    private Button createBuyOrShowButton(Document document) {
        if (SessionStorage.get().getUser().getPurchasedDocuments().contains(document.getId())) {
            return new Button("Show", clickEvent -> makeBuyRequest(document));
        } else {
            return new Button("Buy", clickEvent -> showBuyDialog(document));
        }
    }

    /**
     * updates grid for paging
     */
    private void updateDocumentPage() {
        remove(documentGrid);
        remove(pagingMenuLayout);
        documentGrid = new Grid<>(Document.class);
        pagingMenuLayout = new HorizontalLayout();
        add(createDocumentGrid());
        add(createPagingMenu(documents.getTotalPages()));
    }

    /**
     * @param totalPages number of pages
     * @return paging menu so user can choose which page he wants to see
     */
    private Component createPagingMenu(int totalPages) {
        pagingMenuLayout.addClassName("paging-layout");


        Button previousPage = new Button("Previous");
        previousPage.addClickListener(e -> {
            if (currentPage > 1) {
                documents = getDocuments(currentPage - 1);
                if (documents == null) return;
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
            documents = getDocuments(currentPage);
            updateDocumentPage();
        });

        Button nextPage = new Button("Next");
        nextPage.addClickListener(e -> {
            if (currentPage < documents.getTotalPages()) {
                documents = getDocuments(currentPage + 1);
                if (documents == null) return;
                currentPage = documents.getPage();
                updateDocumentPage();
            }
        });

        Label currentPageText = new Label();
        currentPageText.setText(currentPage + " / " + totalPages);


        pagingMenuLayout.add(previousPage, currentPageText, nextPage, itemsPerPageSelect);

        return pagingMenuLayout;
    }

    /**
     * @param limit number of documents per page
     * @param k search key
     * @param v search value
     * @return documents which match search criteria
     */
    private Documents getDocuments(int limit, String k, String v) {
        HttpResponse<Documents> documents = null;
        if (null != k && null != v) {
            documents = Unirest.get(BASE_URL + "/documents")
                    .queryString("limit", limit)
                    .queryString("page", currentPage)
                    .queryString(k, v)
                    .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                    .asObject(Documents.class)
                    .ifFailure(e -> {
                        Error error = e.mapError(Error.class);
                        if (null != error) {
                            if (new Error(error.getCode(), error.getMessage()).continueCheck()) {
                                getDocuments(limit, k, v);
                            }
                        }
                    });
        } else {
            getDocuments(currentPage);
        }
        
        if (documents == null) return null;

        Error error = documents.mapError(Error.class);

        if (null == error) {
            return documents.getBody();
        } else {
            if (new Error(error.getCode(), error.getMessage()).continueCheck()) {
                return getDocuments(limit, k, v);
            }
            return null;
        }
    }

    /**
     * @return all teachers
     */
    private Teachers getTeachers() {
        HttpResponse<Teachers> teachers = Unirest.get(BASE_URL + "/teacher")
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .queryString("limit", Integer.MAX_VALUE)
                .asObject(Teachers.class);

        Error error = teachers.mapError(Error.class);

        if (null == error) {
            return teachers.getBody();
        } else {
            if (new Error(error.getCode(), error.getMessage()).continueCheck()) {
                return getTeachers();
            }
            return null;
        }
    }

    /**
     * @return all subjects
     */
    private Subjects getSubjects() {
        HttpResponse<Subjects> subjects = Unirest.get(BASE_URL + "/subject")
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .queryString("limit", Integer.MAX_VALUE)
                .asObject(Subjects.class);

        Error error = subjects.mapError(Error.class);

        if (null == error) {
            return subjects.getBody();
        } else {
            if (new Error(error.getCode(), error.getMessage()).continueCheck()) {
                return getSubjects();
            }
            return null;
        }
    }

    /**
     * @param page current page
     * @return documents for the current page
     */
    private Documents getDocuments(int page) {
        if (searchState) {
            int itemsVisible = Math.min(limit, foundIds.size() - ((page - 1) * limit));
            Document[] documentAr = new Document[itemsVisible];
            int documentArCounter = 0;
            for (int i = limit * (page - 1); i < ((page - 1) * limit) + itemsVisible; i++) {
                documentAr[documentArCounter] = resolveDocumentById(foundIds.get(i));
                documentArCounter++;
            }
            if (documentAr.length == 0) {
                currentPage = 0;
            }
            return new Documents(documentAr, page, limit, (int) Math.ceil((float) foundIds.size() / limit), foundIds.size());
        } else {
            HttpResponse<Documents> documents = Unirest.get(BASE_URL + "/documents")
                    .queryString("limit", limit)
                    .queryString("page", page)
                    .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                    .asObject(Documents.class);

            Error error = documents.mapError(Error.class);

            if (null == error) {
                return documents.getBody();
            } else {
                if (new Error(error.getCode(), error.getMessage()).continueCheck()) {
                    return getDocuments(page);
                }
                return null;
            }
        }
    }

    /**
     * @return available types
     */
    private Types getTypes() {
        HttpResponse<Types> types = Unirest.get(BASE_URL + "/documents/types")
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .queryString("limit", Integer.MAX_VALUE)
                .asObject(Types.class);

        Error error = types.mapError(Error.class);

        if (null == error) {
            return types.getBody();
        } else {
            if (new Error(error.getCode(), error.getMessage()).continueCheck()) {
                return getTypes();
            }
            return null;
        }
    }

    /**
     * when a parameter is in the url this method is called
     * it sets the filter up so only filtered documents are shown
     * @param beforeEvent event before this method was called
     * @param s parameter for filter
     */
    @Override
    public void setParameter(BeforeEvent beforeEvent, @WildcardParameter String s) {
        if (!s.isEmpty() && s.split("/").length == 2) {
            filter = s.split("/");
            currentPage = 1;
            String[] resolvedFilter = resolveFilter();
            this.documents = getDocuments(this.limit, resolvedFilter[0], resolvedFilter[1]);
        } else {
            this.documents = getDocuments(1);
        }
        updateDocumentPage();
    }
}
