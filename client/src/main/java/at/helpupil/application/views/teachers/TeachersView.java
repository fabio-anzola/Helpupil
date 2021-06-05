package at.helpupil.application.views.teachers;

import at.helpupil.application.utils.Auth;
import at.helpupil.application.utils.SecuredView;
import at.helpupil.application.utils.SessionStorage;
import at.helpupil.application.utils.responses.Error;
import at.helpupil.application.utils.responses.Teacher;
import at.helpupil.application.utils.responses.Teachers;
import at.helpupil.application.views.main.MainView;
import com.github.appreciated.card.Card;
import com.github.appreciated.card.label.PrimaryLabel;
import com.github.appreciated.card.label.SecondaryLabel;
import com.github.appreciated.card.label.TitleLabel;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.util.Arrays;

import static at.helpupil.application.Application.BASE_URL;

@Route(value = "teachers", layout = MainView.class)
@PageTitle("Teachers")
@CssImport("./views/teachers/teachers-view.css")
public class TeachersView extends SecuredView {

    private Div teacherLayoutDiv = new Div();
    private HorizontalLayout pagingMenuLayout = new HorizontalLayout();

    private final int[] limits = new int[]{10, 15, 25};
    private int limit = limits[0];
    private int currentPage = 1;
    private Teachers teacher = getTeachers(limit, currentPage);

    public TeachersView() {
        addClassName("teachers-view");

        add(createTeacherCards(teacher));
        add(createPagingMenu(teacher.getTotalPages()));
    }

    private void updateTeacherPage() {
        remove(teacherLayoutDiv);
        remove(pagingMenuLayout);
        teacherLayoutDiv = new Div();
        pagingMenuLayout = new HorizontalLayout();
        add(createTeacherCards(teacher));
        add(createPagingMenu(teacher.getTotalPages()));
    }

    private Component createTeacherCards(Teachers teacher) {
        teacherLayoutDiv = new Div();
        teacherLayoutDiv.addClassName("teacher-layout-div");

        HorizontalLayout teacherLayout = new HorizontalLayout();
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

        teacherLayoutDiv.add(teacherLayout);

        return teacherLayoutDiv;
    }

    private Component createPagingMenu(int totalPages) {
        pagingMenuLayout.addClassName("paging-layout");


        Button previousPage = new Button("Previous");
        previousPage.addClickListener(e -> {
            if (currentPage > 1) {
                teacher = getTeachers(limit, currentPage - 1);
                currentPage = teacher.getPage();
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
            teacher = getTeachers(limit, currentPage);
            currentPage = teacher.getPage();
            updateTeacherPage();
        });

        Button nextPage = new Button("Next");
        nextPage.addClickListener(e -> {
            if (currentPage < teacher.getTotalPages()) {
                teacher = getTeachers(limit, currentPage + 1);
                currentPage = teacher.getPage();
                updateTeacherPage();
            }
        });

        Label currentPageText = new Label();
        currentPageText.setText(currentPage + " / " + totalPages);


        pagingMenuLayout.add(previousPage, currentPageText, nextPage, itemsPerPageSelect);

        return pagingMenuLayout;
    }

    private Teachers getTeachers(int limit, int page) {
        HttpResponse<Teachers> teachers = Unirest.get(BASE_URL + "/teacher")
                .queryString("limit", limit)
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
