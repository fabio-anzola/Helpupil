package at.helpupil.application.views.teachers;

import at.helpupil.application.utils.Auth;
import at.helpupil.application.utils.SecuredView;
import at.helpupil.application.utils.SessionStorage;
import at.helpupil.application.utils.requests.TeacherObj;
import at.helpupil.application.utils.responses.Error;
import at.helpupil.application.utils.responses.Teacher;
import at.helpupil.application.utils.responses.Teachers;
import at.helpupil.application.views.main.MainView;
import com.github.appreciated.card.Card;
import com.github.appreciated.card.label.PrimaryLabel;
import com.github.appreciated.card.label.SecondaryLabel;
import com.github.appreciated.card.label.TitleLabel;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import static at.helpupil.application.Application.BASE_URL;

@Route(value = "teachers", layout = MainView.class)
@PageTitle("Teachers")
@CssImport("./views/teachers/teachers-view.css")
public class TeachersView extends SecuredView {

    private Button addTeacher = new Button("Add New Teacher");

    private HorizontalLayout teacherLayout = new HorizontalLayout();
    private HorizontalLayout pagingMenuLayout = new HorizontalLayout();

    private int currentPage = 1;
    private Teachers teacher = getTeachers(currentPage);

    private Button previousPage = new Button("Previous");
    private Label currentPageText = new Label();
    private Button nextPage = new Button("Next");


    public TeachersView() {
        addClassName("teachers-view");

        addTeacher.addClassName("addTeacher-button");

        add(addTeacher);
        add(createTeacherCards(teacher));
        add(createPagingMenu(teacher.getTotalPages()));

        addTeacher.addClickListener(e -> showAddTeacherDialog());

        previousPage.addClickListener(e -> {
            if (currentPage > 1) {
                teacher = getTeachers(currentPage - 1);
                currentPage = teacher.getPage();
                updateTeacherPage();
            }
        });

        nextPage.addClickListener(e -> {
            if (currentPage < teacher.getTotalPages()) {
                teacher = getTeachers(currentPage + 1);
                currentPage = teacher.getPage();
                updateTeacherPage();
            }
        });
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

    private void updateTeacherPage() {
        remove(teacherLayout);
        remove(pagingMenuLayout);
        teacherLayout = new HorizontalLayout();
        pagingMenuLayout = new HorizontalLayout();
        add(createTeacherCards(teacher));
        add(createPagingMenu(teacher.getTotalPages()));
    }

    private Component createTeacherCards(Teachers teacher) {
        teacherLayout.getThemeList().remove("spacing");
        teacherLayout.addClassName("teacher-layout");

        for (Teacher result : teacher.getResults()) {
            Card card = new Card(
                    new TitleLabel(result.getName()),
                    new PrimaryLabel(result.getShortname()),
                    new SecondaryLabel(result.getDescription())
            );
            teacherLayout.add(card);
            card.addClickListener(e -> {
                UI.getCurrent().getPage().executeJs("window.location = \"" + Auth.getURL() + "/documents/teacher/" + result.getShortname() + "\"");
            });
        }

        return teacherLayout;
    }

    private Component createPagingMenu(int totalPages) {
        pagingMenuLayout.addClassName("paging-layout");

        currentPageText.setText(currentPage + " / " + totalPages);

        pagingMenuLayout.add(previousPage, currentPageText, nextPage);

        return pagingMenuLayout;
    }

    private Teachers getTeachers(int page) {
        HttpResponse<Teachers> teachers = Unirest.get(BASE_URL + "/teacher")
                .queryString("page", page)
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .asObject(Teachers.class);

        Error error = teachers.mapError(Error.class);

        if (null == error) {
            return teachers.getBody();
        } else {
            Notification.show(error.getMessage());
        }
        return null;
    }

}
