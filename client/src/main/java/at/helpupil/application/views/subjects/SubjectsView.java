package at.helpupil.application.views.subjects;

import at.helpupil.application.utils.Auth;
import at.helpupil.application.utils.SecuredView;
import at.helpupil.application.utils.SessionStorage;
import at.helpupil.application.utils.requests.SignUp;
import at.helpupil.application.utils.requests.SubjectObj;
import at.helpupil.application.utils.responses.Error;
import at.helpupil.application.utils.responses.Subject;
import at.helpupil.application.utils.responses.Subjects;
import at.helpupil.application.utils.responses.User;
import at.helpupil.application.views.main.MainView;
import com.github.appreciated.card.Card;
import com.github.appreciated.card.label.PrimaryLabel;
import com.github.appreciated.card.label.SecondaryLabel;
import com.github.appreciated.card.label.TitleLabel;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import static at.helpupil.application.Application.BASE_URL;

@Route(value = "subjects", layout = MainView.class)
@PageTitle("Subjects")
@CssImport("./views/subjects/subjects-view.css")
public class SubjectsView extends SecuredView {

    private Button addSubject = new Button("Add New Subject");

    private HorizontalLayout subjectLayout = new HorizontalLayout();
    private HorizontalLayout pagingMenuLayout = new HorizontalLayout();

    private int currentPage = 1;
    private Subjects subject = getSubjects(currentPage);

    private Button previousPage = new Button("Previous");
    private Label currentPageText = new Label();
    private Button nextPage = new Button("Next");


    public SubjectsView() {
        addClassName("subjects-view");

        add(addSubject);
        add(createSubjectCards(subject));
        add(createPagingMenu(subject.getTotalPages()));

        addSubject.addClickListener(e -> showAddSubjectDialog());

        previousPage.addClickListener(e -> {
            if (currentPage > 1) {
                subject = getSubjects(currentPage - 1);
                currentPage = subject.getPage();
                updateSubjectPage();
            }
        });

        nextPage.addClickListener(e -> {
            if (currentPage < subject.getTotalPages()) {
                subject = getSubjects(currentPage + 1);
                currentPage = subject.getPage();
                updateSubjectPage();
            }
        });
    }

    private void showAddSubjectDialog() {
        Dialog dialog = new Dialog();
        dialog.add(new Text("Add new Subject"));
        Span message = new Span();

        VerticalLayout formLayout = new VerticalLayout();

        TextField name = new TextField("Name");
        TextField shortname = new TextField("Shortname");
        TextField description = new TextField("Description");

        formLayout.add(name, shortname, description);

        Button confirmButton = new Button("Confirm", event -> {
            if (!name.getValue().trim().isEmpty() && !shortname.getValue().trim().isEmpty() && !description.getValue().trim().isEmpty()) {
                makeSubjectCreateRequest(name.getValue(), shortname.getValue(), description.getValue());
            }
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancel", event -> {
            dialog.close();
        });

        dialog.add(new Div(formLayout, confirmButton, cancelButton));
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

    private void updateSubjectPage() {
        remove(subjectLayout);
        remove(pagingMenuLayout);
        subjectLayout = new HorizontalLayout();
        pagingMenuLayout = new HorizontalLayout();
        add(createSubjectCards(subject));
        add(createPagingMenu(subject.getTotalPages()));
    }

    private Component createSubjectCards(Subjects subject) {
        subjectLayout.getThemeList().remove("spacing");
        subjectLayout.addClassName("subject-layout");

        for (Subject result : subject.getResults()) {
            Card card = new Card(
                    new TitleLabel(result.getName()),
                    new PrimaryLabel(result.getShortname()),
                    new SecondaryLabel(result.getDescription())
            );
            subjectLayout.add(card);
            card.addClickListener(e -> {
                UI.getCurrent().getPage().executeJs("window.location = \"" + Auth.getURL() + "/documents/subject/" + result.getShortname() + "\"");
            });
        }

        return subjectLayout;
    }

    private Component createPagingMenu(int totalPages) {
        pagingMenuLayout.addClassName("paging-layout");

        currentPageText.setText(currentPage + " / " + totalPages);

        pagingMenuLayout.add(previousPage, currentPageText, nextPage);

        return pagingMenuLayout;
    }

    private Subjects getSubjects(int page) {
        HttpResponse<Subjects> subjects = Unirest.get(BASE_URL + "/subject")
                .queryString("page", page)
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .asObject(Subjects.class);

        Error error = subjects.mapError(Error.class);

        if (null == error) {
            return subjects.getBody();
        } else {
            Notification.show(error.getMessage());
        }
        return null;
    }

}
