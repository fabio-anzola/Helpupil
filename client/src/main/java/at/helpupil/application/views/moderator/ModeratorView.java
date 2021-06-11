package at.helpupil.application.views.moderator;

import at.helpupil.application.utils.SecuredView;
import at.helpupil.application.utils.SessionStorage;
import at.helpupil.application.utils.requests.SubjectObj;
import at.helpupil.application.utils.requests.TeacherObj;
import at.helpupil.application.utils.responses.Error;
import at.helpupil.application.utils.responses.Subject;
import at.helpupil.application.utils.responses.Teacher;
import at.helpupil.application.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.util.HashMap;
import java.util.Map;

import static at.helpupil.application.Application.BASE_URL;

@Route(value = "moderator", layout = MainView.class)
@PageTitle("Moderator")
@CssImport("./views/moderator/moderator-view.css")
public class ModeratorView extends SecuredView {

    private Button addTeacher = new Button("Add New Teacher");
    private Button addSubject = new Button("Add New Subject");

    public ModeratorView() {
        addClassName("moderator-view");

        addTeacher.addClassName("add-teacher");
        Div addTeacherDiv = new Div(addTeacher);
        addTeacher.addClickListener(e -> showAddTeacherDialog());

        addSubject.addClassName("add-subject");
        Div addSubjectDiv = new Div(addSubject);
        addSubject.addClickListener(e -> showAddSubjectDialog());

        Tab documentTab = new Tab("Documents");
        Tab teacherTab = new Tab("Teachers");
        Tab subjectTab = new Tab("Subjects");
        Tabs tabs = new Tabs(documentTab, teacherTab, subjectTab);
        tabs.setFlexGrowForEnclosedTabs(1);

        Div documentPage = new Div();
        documentPage.setText("Documents");

        Div teacherPage = new Div();
        teacherPage.setText("Teachers");
        teacherPage.setVisible(false);
        teacherPage.add(addTeacherDiv);

        Div subjectPage = new Div();
        subjectPage.setText("Subjects");
        subjectPage.setVisible(false);
        subjectPage.add(addSubjectDiv);

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
        cancelButton.addClickListener(e -> {
            dialog.close();
        });

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
        cancelButton.addClickListener(e -> {
            dialog.close();
        });

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

}
