package at.helpupil.application.views.moderator;

import at.helpupil.application.utils.SecuredView;
import at.helpupil.application.utils.SessionStorage;
import at.helpupil.application.utils.requests.SubjectObj;
import at.helpupil.application.utils.requests.TeacherObj;
import at.helpupil.application.utils.responses.*;
import at.helpupil.application.utils.responses.Error;
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
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.io.ByteArrayInputStream;
import java.util.*;

import static at.helpupil.application.Application.BASE_URL;
import static at.helpupil.application.utils.Resolve.resolveTeacherById;

@Route(value = "moderator", layout = MainView.class)
@PageTitle("Moderator")
@CssImport("./views/moderator/moderator-view.css")
public class ModeratorView extends SecuredView {

    private Button addSubject = new Button("Add New Subject");
    private int currentPage = 1;
    private Grid<Document> documentGrid = new Grid<>(Document.class);
    private Grid<Teacher> teacherGrid = new Grid<>(Teacher.class);
    private Documents documents = getPendingDocuments(currentPage);
    private Teachers teachers = getTeachers(currentPage);
    private HorizontalLayout documentPagingMenuLayout;
    private HorizontalLayout teacherPagingMenuLayout;
    private boolean searchState = false;
    private final int[] limits = new int[]{10, 15, 25};
    private int limit = limits[0];
    private final ArrayList<String> foundIds = new ArrayList<>();

    private Div documentPage = new Div();
    Div teacherPage = new Div();

    public ModeratorView() {
        addClassName("moderator-view");

        if (documents.getTotalResults() == 0) {
            currentPage = 0;
        }

        Tab documentTab = new Tab("Documents");
        Tab teacherTab = new Tab("Teachers");
        Tab subjectTab = new Tab("Subjects");
        Tabs tabs = new Tabs(documentTab, teacherTab, subjectTab);
        tabs.addClassName("moderator-tabs");
        tabs.setFlexGrowForEnclosedTabs(1);

        documentPage.addClassName("document-page");
        documentPage.add(createDocumentGrid());
        documentPage.add(createDocumentPagingMenu(documents.getTotalPages()));

        teacherPage.setVisible(false);
        teacherPage.addClassName("teacher-page");
        teacherPage.add(createTeacherTopDiv());
        teacherPage.add(createTeacherGrid());
        teacherPage.add(createTeacherPagingMenu(teachers.getTotalPages()));

        Div subjectPage = new Div();
        subjectPage.setVisible(false);
        addSubject.addClassName("add-subject");
        Div addSubjectDiv = new Div(addSubject);
        addSubject.addClickListener(e -> showAddSubjectDialog());
        subjectPage.add(addSubjectDiv);

        Map<Tab, Component> tabsToPages = new HashMap<>();
        tabsToPages.put(documentTab, documentPage);
        tabsToPages.put(teacherTab, teacherPage);
        tabsToPages.put(subjectTab, subjectPage);

        Div pages = new Div(documentPage, teacherPage, subjectPage);

        tabs.addSelectedChangeListener(e -> {
            currentPage = 1;
            searchState = false;

            tabsToPages.values().forEach(page -> page.setVisible(false));
            Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
            selectedPage.setVisible(true);
        });

        add(tabs, pages);
    }

    private Grid<Document> createDocumentGrid() {
        List<Document> documentList = new ArrayList<>();

        for (Document document : documents.getResults()) {
            if (document.getType().length() > 0) {
                document.setType(document.getType().substring(0, 1).toUpperCase() + document.getType().substring(1));
            }
            document.setSubject(document.getSubject_sn());
            document.setTeacher(document.getTeacher_sn());
            document.setUser(document.getUname());
            documentList.add(document);
        }

        documentGrid.addClassName("moderator-grid");
        documentGrid.setItems(documentList);
        documentGrid.removeColumnByKey("reviewer");
        documentGrid.removeColumnByKey("file");
        documentGrid.removeColumnByKey("rating");
        documentGrid.removeColumnByKey("status");
        documentGrid.removeColumnByKey("id");
        documentGrid.setColumns("name", "type", "subject", "teacher", "user", "price");

        documentGrid.addItemClickListener(item -> showDocumentDialog(item.getItem()));

        return documentGrid;
    }

    private Grid<Teacher> createTeacherGrid() {
        List<Teacher> teacherList = new ArrayList<>(Arrays.asList(teachers.getResults()));

        teacherGrid.addClassName("moderator-grid");
        teacherGrid.setItems(teacherList);
        teacherGrid.removeColumnByKey("user");
        teacherGrid.removeColumnByKey("id");
        teacherGrid.setColumns("name", "shortname", "description");

        teacherGrid.addItemClickListener(item -> showTeacherDialog(item.getItem()));

        return teacherGrid;
    }

    private void showDocumentDialog(Document document) {
        Dialog dialog = new Dialog();
        dialog.setWidth("40vw");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.addClassName("dialog-layout");

        Label dialogHeading = new Label(document.getName());
        dialogLayout.add(dialogHeading);


        TextField declineMessage = new TextField("Decline Message");
        declineMessage.addClassName("decline-text-field");

        Button approveButton = new Button("Approve");
        approveButton.addClickListener(e -> {
            dialog.close();
            makeApproveRequest(document.getId());
        });
        approveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

        Button declineButton = new Button("Decline");
        declineButton.addClickListener(e -> {
            dialog.close();
            makeDeclineRequest(document.getId(), declineMessage.getValue());
        });
        declineButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

        HorizontalLayout approveDeclineButtonLayout = new HorizontalLayout();
        approveDeclineButtonLayout.add(approveButton, declineButton);


        Button showButton = new Button("Show");
        showButton.addClickListener(e -> makeShowRequest(document));
        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(e -> dialog.close());

        HorizontalLayout dialogButtonLayout = new HorizontalLayout();
        dialogButtonLayout.add(showButton, cancelButton);


        dialogLayout.add(declineMessage, approveDeclineButtonLayout, dialogButtonLayout);


        dialog.add(dialogLayout);
        dialog.open();
    }

    private void showTeacherDialog(Teacher teacher) {
        Dialog dialog = new Dialog();
        dialog.setWidth("40vw");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.addClassName("dialog-layout");

        Label dialogHeading = new Label(teacher.getName());
        dialogLayout.add(dialogHeading);


        TextField nameField = new TextField("Name");
        TextField shortnameField = new TextField("Shortname");
        TextField descriptionField = new TextField("Description");


        Button confirmButton = new Button("Confirm");
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//        confirmButton.addClickListener(e -> );
        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(e -> dialog.close());

        HorizontalLayout dialogButtonLayout = new HorizontalLayout();
        dialogButtonLayout.add(confirmButton, cancelButton);


        dialogLayout.add(nameField, shortnameField, descriptionField, dialogButtonLayout);


        dialog.add(dialogLayout);
        dialog.open();
    }

    private Component createTeacherTopDiv() {
        Div topDiv = new Div();
        topDiv.addClassName("top-div-doc");


        Div emptyDiv = new Div();


        Button addTeacher = new Button("Add New Teacher");
        addTeacher.addClassName("add-teacher");
        Div addTeacherDiv = new Div(addTeacher);
        addTeacherDiv.addClassName("add-teacher-div");
        addTeacher.addClickListener(e -> showAddTeacherDialog());


        Div innerDiv = new Div();
        innerDiv.addClassName("search-inner-div");

        TextField searchBox = new TextField();
        searchBox.setPlaceholder("Search");
        searchBox.setClearButtonVisible(true);
        searchBox.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);
        searchBox.addKeyDownListener(Key.ESCAPE, e -> searchBox.blur());
        searchBox.addKeyDownListener(Key.ENTER, e -> makeTeacherSearchRequest(searchBox.getValue()));

        Icon searchIcon = new Icon(VaadinIcon.SEARCH);
        searchIcon.addClickListener(e -> makeTeacherSearchRequest(searchBox.getValue()));

        Icon exitSearchState = new Icon(VaadinIcon.CLOSE_BIG);
        exitSearchState.addClickListener(e -> {
            if (searchState) {
                searchBox.clear();
                searchState = false;
                currentPage = 1;
                teachers = getTeachers(currentPage);
                updateTeacherPage();
            }
        });

        innerDiv.add(searchIcon, searchBox, exitSearchState);


        topDiv.add(emptyDiv, addTeacherDiv, innerDiv);
        return topDiv;
    }

    private void makeApproveRequest(String documentId) {
        HttpResponse<Document> document = Unirest.patch(BASE_URL + "/mod/approve/" + documentId)
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .asObject(Document.class);

        Error error = document.mapError(Error.class);

        if (null == error) {
            Notification.show("Document has been approved!");
            documents = getPendingDocuments(currentPage);
            if (documents.getTotalResults() == 0) {
                currentPage = 0;
            }
            updateDocumentPage();
        } else {
            Notification.show(error.getMessage());
        }
    }

    private void makeDeclineRequest(String documentId, String declineMessage) {
        HttpResponse<Document> document = Unirest.patch(BASE_URL + "/mod/decline/" + documentId)
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .queryString("message", declineMessage)
                .asObject(Document.class);

        Error error = document.mapError(Error.class);

        if (null == error) {
            Notification.show("Document has been declined!");
            documents = getPendingDocuments(currentPage);
            if (documents.getTotalResults() == 0) {
                currentPage = 0;
            }
            updateDocumentPage();
        } else {
            Notification.show(error.getMessage());
        }
    }

    private Documents getPendingDocuments(int page) {
        HttpResponse<Documents> documents = Unirest.get(BASE_URL + "/mod/pending")
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

    private Teachers getTeachers(int page) {
        if (searchState) {
            int itemsVisible = Math.min(limit, foundIds.size() - ((page - 1) * limit));
            Teacher[] teacherAr = new Teacher[itemsVisible];
            int teacherArCounter = 0;
            for (int i = limit * (page - 1); i < ((page - 1) * limit) + itemsVisible; i++) {
                teacherAr[teacherArCounter] = resolveTeacherById(foundIds.get(i));
                teacherArCounter++;
            }
            if (teacherAr.length == 0) {
                currentPage = 0;
            }
            return new Teachers(teacherAr, page, limit, (int) Math.ceil((float) foundIds.size() / limit), foundIds.size());
        } else {
            HttpResponse<Teachers> teachers = Unirest.get(BASE_URL + "/teacher")
                    .queryString("limit", limit)
                    .queryString("page", page)
                    .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                    .asObject(Teachers.class);

            Error error = teachers.mapError(Error.class);

            if (null == error) {
                return teachers.getBody();
            } else {
                new Error(error.getCode(), error.getMessage());
                return getTeachers(page);
            }
        }
    }

    private void showAddTeacherDialog() {
        Dialog dialog = new Dialog();
        dialog.setWidth("40vw");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.addClassName("dialog-layout");

        Label dialogHeading = new Label("Add new Teacher");

        TextField name = new TextField("Name");
        TextField shortname = new TextField("Shortname");
        TextField description = new TextField("Description");


        HorizontalLayout dialogButtonLayout = new HorizontalLayout();

        Button confirmButton = new Button("Confirm");
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmButton.addClickShortcut(Key.ENTER);
        confirmButton.addClickListener(e -> {
            if (!name.getValue().trim().isEmpty() && !shortname.getValue().trim().isEmpty() && !description.getValue().trim().isEmpty()) {
                makeTeacherCreateRequest(name.getValue(), shortname.getValue(), description.getValue());
                dialog.close();
            } else {
                Notification.show("Check your input");
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(e -> dialog.close());

        dialogButtonLayout.add(confirmButton, cancelButton);


        dialogLayout.add(dialogHeading, name, shortname, description, dialogButtonLayout);


        dialog.add(dialogLayout);
        dialog.open();
    }

    private void makeTeacherCreateRequest(String teacher, String shortname, String description) {
        HttpResponse<Teacher> createTeacher = Unirest.post(BASE_URL + "/teacher")
                .contentType("application/json")
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .body(new TeacherObj(teacher, shortname, description))
                .asObject(Teacher.class);

        Error error = createTeacher.mapError(Error.class);

        if (null == error) {
            Notification.show(shortname + " successfully created");
        } else {
            Notification.show(error.getMessage());
        }
    }


    private void showAddSubjectDialog() {
        Dialog dialog = new Dialog();
        dialog.setWidth("40vw");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.addClassName("dialog-layout");

        Label dialogHeading = new Label("Add new Subject");

        TextField name = new TextField("Name");
        TextField shortname = new TextField("Shortname");
        TextField description = new TextField("Description");


        HorizontalLayout dialogButtonLayout = new HorizontalLayout();

        Button confirmButton = new Button("Confirm");
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmButton.addClickShortcut(Key.ENTER);
        confirmButton.addClickListener(e -> {
            if (!name.getValue().trim().isEmpty() && !shortname.getValue().trim().isEmpty() && !description.getValue().trim().isEmpty()) {
                makeSubjectCreateRequest(name.getValue(), shortname.getValue(), description.getValue());
                dialog.close();
            } else {
                Notification.show("Check your input");
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(e -> dialog.close());

        dialogButtonLayout.add(confirmButton, cancelButton);


        dialogLayout.add(dialogHeading, name, shortname, description, dialogButtonLayout);


        dialog.add(dialogLayout);
        dialog.open();
    }

    private void makeSubjectCreateRequest(String subject, String shortname, String description) {
        HttpResponse<Subject> createSubject = Unirest.post(BASE_URL + "/subject")
                .contentType("application/json")
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .body(new SubjectObj(subject, shortname, description))
                .asObject(Subject.class);

        Error error = createSubject.mapError(Error.class);

        if (null == error) {
            Notification.show(shortname + " successfully created");
        } else {
            Notification.show(error.getMessage());
        }
    }

    private Component createDocumentPagingMenu(int totalPages) {
//        pagingMenuLayout = new HorizontalLayout();
        documentPagingMenuLayout = new HorizontalLayout();
        documentPagingMenuLayout.addClassName("paging-layout");


        Button previousPage = new Button("Previous");
        previousPage.addClickListener(e -> {
            if (currentPage > 1) {
                documents = getPendingDocuments(currentPage - 1);
                currentPage = documents.getPage();
                if (documents.getTotalResults() == 0) {
                    currentPage = 0;
                }
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
            documents = getPendingDocuments(currentPage);
            if (documents.getTotalResults() == 0) {
                currentPage = 0;
            }
            updateDocumentPage();
        });

        Button nextPage = new Button("Next");
        nextPage.addClickListener(e -> {
            if (currentPage < documents.getTotalPages()) {
                documents = getPendingDocuments(currentPage + 1);
                currentPage = documents.getPage();
                if (documents.getTotalResults() == 0) {
                    currentPage = 0;
                }
                updateDocumentPage();
            }
        });

        Label currentPageText = new Label();
        currentPageText.setText(currentPage + " / " + totalPages);


        documentPagingMenuLayout.add(previousPage, currentPageText, nextPage, itemsPerPageSelect);

        return documentPagingMenuLayout;
    }

    private Component createTeacherPagingMenu(int totalPages) {
        teacherPagingMenuLayout = new HorizontalLayout();
        teacherPagingMenuLayout.addClassName("paging-layout");


        Button previousPage = new Button("Previous");
        previousPage.addClickListener(e -> {
            if (currentPage > 1) {
                teachers = getTeachers(currentPage - 1);
                currentPage = teachers.getPage();
                if (teachers.getTotalResults() == 0) {
                    currentPage = 0;
                }
                updateTeacherPage();
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
            teachers = getTeachers(currentPage);
            if (teachers.getTotalResults() == 0) {
                currentPage = 0;
            }
            updateTeacherPage();
        });

        Button nextPage = new Button("Next");
        nextPage.addClickListener(e -> {
            if (currentPage < teachers.getTotalPages()) {
                teachers = getTeachers(currentPage + 1);
                currentPage = teachers.getPage();
                if (teachers.getTotalResults() == 0) {
                    currentPage = 0;
                }
                updateTeacherPage();
            }
        });

        Label currentPageText = new Label();
        currentPageText.setText(currentPage + " / " + totalPages);


        teacherPagingMenuLayout.add(previousPage, currentPageText, nextPage, itemsPerPageSelect);

        return teacherPagingMenuLayout;
    }

    private void updateDocumentPage() {
        documentPage.remove(documentGrid);
        documentPage.remove(documentPagingMenuLayout);
        documentGrid = new Grid<>(Document.class);
        documentPagingMenuLayout = new HorizontalLayout();
        documentPage.add(createDocumentGrid());
        documentPage.add(createDocumentPagingMenu(documents.getTotalPages()));
    }

    private void updateTeacherPage() {
        teacherPage.remove(teacherGrid);
        teacherPage.remove(teacherPagingMenuLayout);
        teacherGrid = new Grid<>(Teacher.class);
        teacherPagingMenuLayout = new HorizontalLayout();
        teacherPage.add(createTeacherGrid());
        teacherPage.add(createTeacherPagingMenu(teachers.getTotalPages()));
    }


    private void makeShowRequest(Document document) {
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

        // update user obj - purchase documents
        fetchUserData(SessionStorage.get().getUser().getId());
    }

    private void fetchUserData(String id) {
        HttpResponse<UserObj> user = Unirest.get(BASE_URL + "/users/" + id)
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .asObject(UserObj.class);

        Error error = user.mapError(Error.class);

        if (null == error) {
            SessionStorage.update(user.getBody());
            updateDocumentPage();
        } else {
            Notification.show(error.getMessage());
        }
    }

    private void makeTeacherSearchRequest(String searchText) {
        searchText = searchText.toLowerCase();
        foundIds.clear();

        int pageIndex = 0;
        int pages = 1;
        do {
            pageIndex++;
            HttpResponse<Teachers> teachers = Unirest.get(BASE_URL + "/teacher")
                    .queryString("page", pageIndex)
                    .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                    .asObject(Teachers.class);

            Error error = teachers.mapError(Error.class);

            if (null == error) {
                if (pages < teachers.getBody().getTotalPages()) {
                    pages = teachers.getBody().getTotalPages();
                }

                String finalSearchText = searchText;
                Arrays.stream(teachers.getBody().getResults()).forEach(n -> {
                    if (n.getShortname().toLowerCase().contains(finalSearchText)
                            || n.getName().toLowerCase().contains(finalSearchText)
                            || n.getDescription().toLowerCase().contains(finalSearchText)) {
                        foundIds.add(n.getId());
                    }
                });
            } else {
                Notification.show(error.getMessage());
                return;
            }
        } while (pageIndex != pages);


        searchState = true;
        currentPage = 1;
        teachers = getTeachers(currentPage);
        updateTeacherPage();
    }
}
