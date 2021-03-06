package at.helpupil.application.views.moderator;

import at.helpupil.application.utils.SecuredView;
import at.helpupil.application.utils.SessionStorage;
import at.helpupil.application.utils.ThemeHelper;
import at.helpupil.application.utils.requests.SubjectObj;
import at.helpupil.application.utils.requests.TeacherObj;
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
import static at.helpupil.application.utils.Resolve.resolveSubjectById;
import static at.helpupil.application.utils.Resolve.resolveTeacherById;

/**
 * This view is only for moderators
 * It shows 3 Tabs so the moderators can manage documents, teachers and subjects
 */
@Route(value = "moderator", layout = MainView.class)
@PageTitle("Moderator")
@CssImport(value = "./views/responsive-dialog.css", themeFor = "vaadin-dialog-overlay")
@CssImport("./views/moderator/moderator-view.css")
@CssImport(value = "./views/moderator/moderator-grid.css", themeFor = "vaadin-grid")
public class ModeratorView extends SecuredView {
    /**
     * current document page
     */
    private int currentDocumentPage = 1;
    /**
     * current teacher page
     */
    private int currentTeacherPage = 1;
    /**
     * current subject page
     */
    private int currentSubjectPage = 1;
    /**
     * grid of documents
     */
    private Grid<Document> documentGrid = new Grid<>(Document.class);
    /**
     * grid of teachers
     */
    private Grid<Teacher> teacherGrid = new Grid<>(Teacher.class);
    /**
     * grid of subjects
     */
    private Grid<Subject> subjectGrid = new Grid<>(Subject.class);
    /**
     * all pending documents for current page
     */
    private Documents documents = getPendingDocuments(currentDocumentPage);
    /**
     * all teachers for current page
     */
    private Teachers teachers = getTeachers(currentTeacherPage);
    /**
     * all subjects for current page
     */
    private Subjects subjects = getSubjects(currentSubjectPage);
    /**
     * layout for document paging menu
     */
    private HorizontalLayout documentPagingMenuLayout;
    /**
     * layout for teacher paging menu
     */
    private HorizontalLayout teacherPagingMenuLayout;
    /**
     * layout for subject paging menu
     */
    private HorizontalLayout subjectPagingMenuLayout;
    /**
     * true if user searches teachers
     */
    private boolean teacherSearchState = false;
    /**
     * true if user searches subjects
     */
    private boolean subjectSearchState = false;
    /**
     * limits for maximum elements per page
     */
    private final int[] limits = new int[]{10, 15, 25};
    /**
     * default value for elements per page
     */
    private int limit = limits[0];
    /**
     * list of found teacher ids
     */
    private final ArrayList<String> foundTeacherIds = new ArrayList<>();
    /**
     * list of found subject ids
     */
    private final ArrayList<String> foundSubjectIds = new ArrayList<>();

    /**
     * div for document page
     */
    private final Div documentPage = new Div();
    /**
     * div for teacher page
     */
    private final Div teacherPage = new Div();
    /**
     * div for subject page
     */
    private final Div subjectPage = new Div();

    /**
     * initializes Moderator View
     */
    public ModeratorView() {
        ThemeHelper.onLoad();

        addClassName("moderator-view");

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

        subjectPage.setVisible(false);
        subjectPage.addClassName("subject-page");
        subjectPage.add(createSubjectTopDiv());
        subjectPage.add(createSubjectGrid());
        subjectPage.add(createSubjectPagingMenu(subjects.getTotalPages()));


        Map<Tab, Component> tabsToPages = new HashMap<>();
        tabsToPages.put(documentTab, documentPage);
        tabsToPages.put(teacherTab, teacherPage);
        tabsToPages.put(subjectTab, subjectPage);

        Div pages = new Div(documentPage, teacherPage, subjectPage);

        tabs.addSelectedChangeListener(e -> {
            tabsToPages.values().forEach(page -> page.setVisible(false));
            Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
            selectedPage.setVisible(true);
        });

        add(tabs, pages);
    }

    /**
     * @return grid of documents and with readable columns and values
     */
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

    /**
     * @return grid of teachers and with readable columns and values
     */
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

    /**
     * @return grid of subjects and with readable columns and values
     */
    private Grid<Subject> createSubjectGrid() {
        List<Subject> subjectList = new ArrayList<>(Arrays.asList(subjects.getResults()));

        subjectGrid.addClassName("moderator-grid");
        subjectGrid.setItems(subjectList);
        subjectGrid.removeColumnByKey("user");
        subjectGrid.removeColumnByKey("id");
        subjectGrid.setColumns("name", "shortname", "description");

        subjectGrid.addItemClickListener(item -> showSubjectDialog(item.getItem()));

        return subjectGrid;
    }

    /**
     * dialog will be shown for a specific document when clicked
     *
     * @param document specific document
     */
    private void showDocumentDialog(Document document) {
        Dialog dialog = new Dialog();
            dialog.setMinWidth("40vw");

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

    /**
     * dialog will be shown for a specific teacher when clicked
     *
     * @param teacher specific teacher
     */
    private void showTeacherDialog(Teacher teacher) {
        Dialog dialog = new Dialog();
            dialog.setMinWidth("40vw");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.addClassName("dialog-layout");

        Label dialogHeading = new Label(teacher.getName());
        dialogLayout.add(dialogHeading);


        TextField nameField = new TextField("Name");
        nameField.setValue(teacher.getName());
        TextField shortnameField = new TextField("Shortname");
        shortnameField.setValue(teacher.getShortname());
        TextField descriptionField = new TextField("Description");
        descriptionField.setValue(teacher.getDescription());


        Button updateButton = new Button("Update");
        updateButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        updateButton.addClickListener(e -> {
            makeTeacherUpdateRequest(teacher.getId(), nameField.getValue(), shortnameField.getValue(), descriptionField.getValue());
            dialog.close();
        });
        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(e -> dialog.close());

        HorizontalLayout dialogButtonLayout = new HorizontalLayout();
        dialogButtonLayout.add(updateButton, cancelButton);


        dialogLayout.add(nameField, shortnameField, descriptionField, dialogButtonLayout);


        dialog.add(dialogLayout);
        dialog.open();
    }


    /**
     * dialog will be shown for a specific subject when clicked
     *
     * @param subject specific subject
     */
    private void showSubjectDialog(Subject subject) {
        Dialog dialog = new Dialog();
            dialog.setMinWidth("40vw");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.addClassName("dialog-layout");

        Label dialogHeading = new Label(subject.getName());
        dialogLayout.add(dialogHeading);


        TextField nameField = new TextField("Name");
        nameField.setValue(subject.getName());
        TextField shortnameField = new TextField("Shortname");
        shortnameField.setValue(subject.getShortname());
        TextField descriptionField = new TextField("Description");
        descriptionField.setValue(subject.getDescription());


        Button updateButton = new Button("Update");
        updateButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        updateButton.addClickListener(e -> {
            makeSubjectUpdateRequest(subject.getId(), nameField.getValue(), shortnameField.getValue(), descriptionField.getValue());
            dialog.close();
        });
        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(e -> dialog.close());

        HorizontalLayout dialogButtonLayout = new HorizontalLayout();
        dialogButtonLayout.add(updateButton, cancelButton);


        dialogLayout.add(nameField, shortnameField, descriptionField, dialogButtonLayout);


        dialog.add(dialogLayout);
        dialog.open();
    }

    /**
     * @return div for the top of the page (includes button to add a new teacher)
     */
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
            searchBox.clear();
            if (teacherSearchState) {
                teacherSearchState = false;
                currentTeacherPage = 1;
                teachers = getTeachers(currentTeacherPage);
                updateTeacherPage();
            }
        });

        innerDiv.add(searchIcon, searchBox, exitSearchState);


        topDiv.add(emptyDiv, addTeacherDiv, innerDiv);
        return topDiv;
    }

    /**
     * @return div for the top of the page (includes button to add a new subject)
     */
    private Component createSubjectTopDiv() {
        Div topDiv = new Div();
        topDiv.addClassName("top-div-doc");


        Div emptyDiv = new Div();


        Button addSubject = new Button("Add New Subject");
        addSubject.addClassName("add-subject");
        Div addTeacherDiv = new Div(addSubject);
        addTeacherDiv.addClassName("add-subject-div");
        addSubject.addClickListener(e -> showAddSubjectDialog());


        Div innerDiv = new Div();
        innerDiv.addClassName("search-inner-div");

        TextField searchBox = new TextField();
        searchBox.setPlaceholder("Search");
        searchBox.setClearButtonVisible(true);
        searchBox.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);
        searchBox.addKeyDownListener(Key.ESCAPE, e -> searchBox.blur());
        searchBox.addKeyDownListener(Key.ENTER, e -> makeSubjectSearchRequest(searchBox.getValue()));

        Icon searchIcon = new Icon(VaadinIcon.SEARCH);
        searchIcon.addClickListener(e -> makeSubjectSearchRequest(searchBox.getValue()));

        Icon exitSearchState = new Icon(VaadinIcon.CLOSE_BIG);
        exitSearchState.addClickListener(e -> {
            searchBox.clear();
            if (subjectSearchState) {
                subjectSearchState = false;
                currentSubjectPage = 1;
                subjects = getSubjects(currentSubjectPage);
                updateSubjectPage();
            }
        });

        innerDiv.add(searchIcon, searchBox, exitSearchState);


        topDiv.add(emptyDiv, addTeacherDiv, innerDiv);
        return topDiv;
    }

    /**
     * makes api request to approve a document
     *
     * @param documentId of document
     */
    private void makeApproveRequest(String documentId) {
        HttpResponse<Document> document = Unirest.patch(BASE_URL + "/mod/approve/" + documentId)
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .asObject(Document.class);

        Error error = document.mapError(Error.class);

        if (null == error) {
            Notification.show("Document has been approved!");
            documents = getPendingDocuments(currentDocumentPage);
            updateDocumentPage();
        } else {
            Notification.show(error.getMessage());
        }
    }

    /**
     * makes api request to decline a document
     *
     * @param documentId     of document
     * @param declineMessage to send to uploader to tell him why his document wasn't published
     */
    private void makeDeclineRequest(String documentId, String declineMessage) {
        HttpResponse<Document> document = Unirest.patch(BASE_URL + "/mod/decline/" + documentId)
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .queryString("message", declineMessage)
                .asObject(Document.class);

        Error error = document.mapError(Error.class);

        if (null == error) {
            Notification.show("Document has been declined!");
            documents = getPendingDocuments(currentDocumentPage);
            updateDocumentPage();
        } else {
            Notification.show(error.getMessage());
        }
    }

    /**
     * @param page current page
     * @return all pending documents for current page
     */
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

    /**
     * @param page current page
     * @return all teachers for current page
     */
    private Teachers getTeachers(int page) {
        if (teacherSearchState) {
            if (foundTeacherIds == null) return null;
            int itemsVisible = Math.min(limit, foundTeacherIds.size() - ((page - 1) * limit));
            Teacher[] teacherAr = new Teacher[itemsVisible];
            int teacherArCounter = 0;
            for (int i = limit * (page - 1); i < ((page - 1) * limit) + itemsVisible; i++) {
                teacherAr[teacherArCounter] = resolveTeacherById(foundTeacherIds.get(i));
                teacherArCounter++;
            }
            return new Teachers(teacherAr, page, limit, (int) Math.ceil((float) foundTeacherIds.size() / limit), foundTeacherIds.size());
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
                if (new Error(error.getCode(), error.getMessage()).continueCheck()) {
                    return getTeachers(page);
                }
                return null;
            }
        }
    }

    /**
     * @param page current page
     * @return all subjects for current page
     */
    private Subjects getSubjects(int page) {
        if (subjectSearchState) {
            if (foundSubjectIds == null) return null;
            int itemsVisible = Math.min(limit, foundSubjectIds.size() - ((page - 1) * limit));
            Subject[] subjectAr = new Subject[itemsVisible];
            int subjectArCounter = 0;
            for (int i = limit * (page - 1); i < ((page - 1) * limit) + itemsVisible; i++) {
                subjectAr[subjectArCounter] = resolveSubjectById(foundSubjectIds.get(i));
                subjectArCounter++;
            }
            return new Subjects(subjectAr, page, limit, (int) Math.ceil((float) foundSubjectIds.size() / limit), foundSubjectIds.size());
        } else {
            HttpResponse<Subjects> subjects = Unirest.get(BASE_URL + "/subject")
                    .queryString("limit", limit)
                    .queryString("page", page)
                    .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                    .asObject(Subjects.class);

            Error error = subjects.mapError(Error.class);

            if (null == error) {
                return subjects.getBody();
            } else {
                if (new Error(error.getCode(), error.getMessage()).continueCheck()) {
                    return getSubjects(page);
                }
                return null;
            }
        }
    }

    /**
     * shows user a dialog to add a teacher
     */
    private void showAddTeacherDialog() {
        Dialog dialog = new Dialog();
            dialog.setMinWidth("40vw");

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
            if (!name.getValue().trim().isEmpty() && !shortname.getValue().trim().isEmpty()) {
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

    /**
     * send api request to create a new teacher
     *
     * @param teacher     name of teacher
     * @param shortname   of teacher
     * @param description of teacher
     */
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

    /**
     * makes api request to update a teacher object
     *
     * @param teacherId   of teacher
     * @param name        of teacher
     * @param shortname   of teacher
     * @param description of teacher
     */
    private void makeTeacherUpdateRequest(String teacherId, String name, String shortname, String description) {
        HttpResponse<Teacher> teacher = Unirest.patch(BASE_URL + "/teacher/" + teacherId)
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .contentType("application/json")
                .body(new TeacherObj(name, shortname, description))
                .asObject(Teacher.class);

        Error error = teacher.mapError(Error.class);

        if (null == error) {
            Notification.show("Teacher has been updated!");
            teachers = getTeachers(currentTeacherPage);
            updateTeacherPage();
        } else {
            Notification.show(error.getMessage());
        }
    }

    /**
     * makes api request to update a subject
     *
     * @param subjectId   of subject
     * @param name        of subject
     * @param shortname   of subject
     * @param description of subject
     */
    private void makeSubjectUpdateRequest(String subjectId, String name, String shortname, String description) {
        HttpResponse<Subject> subject = Unirest.patch(BASE_URL + "/subject/" + subjectId)
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .contentType("application/json")
                .body(new SubjectObj(name, shortname, description))
                .asObject(Subject.class);

        Error error = subject.mapError(Error.class);

        if (null == error) {
            Notification.show("Subject has been updated!");
            subjects = getSubjects(currentSubjectPage);
            updateSubjectPage();
        } else {
            Notification.show(error.getMessage());
        }
    }

    /**
     * shows user a dialog to add a subject
     */
    private void showAddSubjectDialog() {
        Dialog dialog = new Dialog();
            dialog.setMinWidth("40vw");

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
            if (!name.getValue().trim().isEmpty() && !shortname.getValue().trim().isEmpty()) {
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

    /**
     * makes api request to create a new subject
     *
     * @param subject     name of subject
     * @param shortname   of subject
     * @param description of subject
     */
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

    /**
     * @param totalPages number of pages
     * @return paging menu for documents
     */
    private Component createDocumentPagingMenu(int totalPages) {
        documentPagingMenuLayout = new HorizontalLayout();
        documentPagingMenuLayout.addClassName("paging-layout");


        Button previousPage = new Button("Previous");
        previousPage.addClickListener(e -> {
            if (currentDocumentPage > 1) {
                documents = getPendingDocuments(currentDocumentPage - 1);
                if (documents == null) return;
                currentDocumentPage = documents.getPage();
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
            currentDocumentPage = 1;
            documents = getPendingDocuments(currentDocumentPage);
            updateDocumentPage();
        });

        Button nextPage = new Button("Next");
        nextPage.addClickListener(e -> {
            if (currentDocumentPage < documents.getTotalPages()) {
                documents = getPendingDocuments(currentDocumentPage + 1);
                if (documents == null) return;
                currentDocumentPage = documents.getPage();
                updateDocumentPage();
            }
        });

        Label currentPageText = new Label(((totalPages == 0) ? 0 : currentDocumentPage) + " / " + totalPages);


        documentPagingMenuLayout.add(previousPage, currentPageText, nextPage, itemsPerPageSelect);

        return documentPagingMenuLayout;
    }

    /**
     * @param totalPages number of pages
     * @return paging menu for teachers
     */
    private Component createTeacherPagingMenu(int totalPages) {
        teacherPagingMenuLayout = new HorizontalLayout();
        teacherPagingMenuLayout.addClassName("paging-layout");


        Button previousPage = new Button("Previous");
        previousPage.addClickListener(e -> {
            if (currentTeacherPage > 1) {
                teachers = getTeachers(currentTeacherPage - 1);
                if (teachers == null) return;
                currentTeacherPage = teachers.getPage();
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
            currentTeacherPage = 1;
            teachers = getTeachers(currentTeacherPage);
            updateTeacherPage();
        });

        Button nextPage = new Button("Next");
        nextPage.addClickListener(e -> {
            if (currentTeacherPage < teachers.getTotalPages()) {
                teachers = getTeachers(currentTeacherPage + 1);
                if (teachers == null) return;
                currentTeacherPage = teachers.getPage();
                updateTeacherPage();
            }
        });

        Label currentPageText = new Label(((totalPages == 0) ? 0 : currentTeacherPage) + " / " + totalPages);


        teacherPagingMenuLayout.add(previousPage, currentPageText, nextPage, itemsPerPageSelect);

        return teacherPagingMenuLayout;
    }

    /**
     * @param totalPages number of pages
     * @return paging menu for subjects
     */
    private Component createSubjectPagingMenu(int totalPages) {
        subjectPagingMenuLayout = new HorizontalLayout();
        subjectPagingMenuLayout.addClassName("paging-layout");


        Button previousPage = new Button("Previous");
        previousPage.addClickListener(e -> {
            if (currentSubjectPage > 1) {
                subjects = getSubjects(currentSubjectPage - 1);
                if (subjects == null) return;
                currentSubjectPage = subjects.getPage();
                updateSubjectPage();
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
            currentSubjectPage = 1;
            subjects = getSubjects(currentSubjectPage);
            updateSubjectPage();
        });

        Button nextPage = new Button("Next");
        nextPage.addClickListener(e -> {
            if (currentSubjectPage < subjects.getTotalPages()) {
                subjects = getSubjects(currentSubjectPage + 1);
                if (subjects == null) return;
                currentSubjectPage = subjects.getPage();
                updateSubjectPage();
            }
        });

        Label currentPageText = new Label(((totalPages == 0) ? 0 : currentSubjectPage) + " / " + totalPages);


        subjectPagingMenuLayout.add(previousPage, currentPageText, nextPage, itemsPerPageSelect);

        return subjectPagingMenuLayout;
    }

    /**
     * update document page
     */
    private void updateDocumentPage() {
        documentPage.remove(documentGrid);
        documentPage.remove(documentPagingMenuLayout);
        documentGrid = new Grid<>(Document.class);
        documentPagingMenuLayout = new HorizontalLayout();
        documentPage.add(createDocumentGrid());
        documentPage.add(createDocumentPagingMenu(documents.getTotalPages()));
    }

    /**
     * update teacher page
     */
    private void updateTeacherPage() {
        teacherPage.remove(teacherGrid);
        teacherPage.remove(teacherPagingMenuLayout);
        teacherGrid = new Grid<>(Teacher.class);
        teacherPagingMenuLayout = new HorizontalLayout();
        teacherPage.add(createTeacherGrid());
        teacherPage.add(createTeacherPagingMenu(teachers.getTotalPages()));
    }

    /**
     * update subject page
     */
    private void updateSubjectPage() {
        subjectPage.remove(subjectGrid);
        subjectPage.remove(subjectPagingMenuLayout);
        subjectGrid = new Grid<>(Subject.class);
        subjectPagingMenuLayout = new HorizontalLayout();
        subjectPage.add(createSubjectGrid());
        subjectPage.add(createSubjectPagingMenu(subjects.getTotalPages()));
    }

    /**
     * makes show request for specific document to api so moderator can check the document -> approve/decline/still pending
     *
     * @param document specific document
     */
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

    /**
     * makes request to api to fetch user data for a specific user
     *
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
            Notification.show(error.getMessage());
        }
    }

    /**
     * make api request to filter teachers
     *
     * @param searchText to search for
     */
    private void makeTeacherSearchRequest(String searchText) {
        searchText = searchText.toLowerCase();
        foundTeacherIds.clear();

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
                        foundTeacherIds.add(n.getId());
                    }
                });
            } else {
                Notification.show(error.getMessage());
                return;
            }
        } while (pageIndex != pages);


        teacherSearchState = true;
        currentTeacherPage = 1;
        teachers = getTeachers(currentTeacherPage);
        updateTeacherPage();
    }

    /**
     * make api request to filter for subjects
     *
     * @param searchText to search for
     */
    private void makeSubjectSearchRequest(String searchText) {
        searchText = searchText.toLowerCase();
        foundSubjectIds.clear();

        int pageIndex = 0;
        int pages = 1;
        do {
            pageIndex++;
            HttpResponse<Subjects> subjects = Unirest.get(BASE_URL + "/subject")
                    .queryString("page", pageIndex)
                    .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                    .asObject(Subjects.class);

            Error error = subjects.mapError(Error.class);

            if (null == error) {
                if (pages < subjects.getBody().getTotalPages()) {
                    pages = subjects.getBody().getTotalPages();
                }

                String finalSearchText = searchText;
                Arrays.stream(subjects.getBody().getResults()).forEach(n -> {
                    if (n.getShortname().toLowerCase().contains(finalSearchText)
                            || n.getName().toLowerCase().contains(finalSearchText)
                            || n.getDescription().toLowerCase().contains(finalSearchText)) {
                        foundSubjectIds.add(n.getId());
                    }
                });
            } else {
                Notification.show(error.getMessage());
                return;
            }
        } while (pageIndex != pages);


        subjectSearchState = true;
        currentSubjectPage = 1;
        subjects = getSubjects(currentSubjectPage);
        updateSubjectPage();
    }
}
