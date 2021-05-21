package at.helpupil.application.views.subjects;

import at.helpupil.application.utils.Auth;
import at.helpupil.application.utils.SecuredView;
import at.helpupil.application.utils.SessionStorage;
import at.helpupil.application.utils.responses.Error;
import at.helpupil.application.utils.responses.Subject;
import at.helpupil.application.utils.responses.Subjects;
import at.helpupil.application.views.main.MainView;
import com.github.appreciated.card.Card;
import com.github.appreciated.card.label.PrimaryLabel;
import com.github.appreciated.card.label.SecondaryLabel;
import com.github.appreciated.card.label.TitleLabel;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.awt.*;

import static at.helpupil.application.Application.BASE_URL;

@Route(value = "subjects", layout = MainView.class)
@PageTitle("Subjects")
@CssImport("./views/subjects/subjects-view.css")
public class SubjectsView extends SecuredView {
    private HorizontalLayout subjectLayout = new HorizontalLayout();
    private HorizontalLayout pagingMenuLayout = new HorizontalLayout();

    private int currentPage = 1;
    private Subjects subject = getSubjects(currentPage);

    private Button previousPage = new Button("Previous");
    private Label currentPageText = new Label();
    private Button nextPage = new Button("Next");


    public SubjectsView() {
        addClassName("subjects-view");

        add(createSubjectCards(subject));
        add(createPagingMenu(subject.getTotalPages()));

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
