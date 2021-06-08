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
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.util.ArrayList;
import java.util.Arrays;

import static at.helpupil.application.Application.BASE_URL;
import static at.helpupil.application.utils.Resolve.resolveTeacherById;

@Route(value = "teachers", layout = MainView.class)
@PageTitle("Teachers")
@CssImport("./views/teachers/teachers-view.css")
public class TeachersView extends SecuredView {

    private Div teacherLayoutDiv = new Div();
    private HorizontalLayout pagingMenuLayout = new HorizontalLayout();

    private boolean searchState = false;
    private final ArrayList<String> foundIds = new ArrayList<>();
    private final int[] limits = new int[]{10, 15, 25};
    private int limit = limits[0];
    private int currentPage = 1;
    private Teachers teacher = getTeachers(currentPage);

    public TeachersView() {
        addClassName("teachers-view");

        add(createSearchBox());
        add(createTeacherCards());
        add(createPagingMenu(teacher.getTotalPages()));
    }

    private Component createSearchBox() {
        Div searchDiv = new Div();
        searchDiv.addClassName("search-div");

        Div innerDiv = new Div();

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
            if (searchState) {
                searchBox.clear();
                searchState = false;
                currentPage = 1;
                teacher = getTeachers(currentPage);
                updateTeacherPage();
            }
        });

        innerDiv.add(searchBox, searchIcon, exitSearchState);


        searchDiv.add(innerDiv);

        return searchDiv;
    }

    private void makeSearchRequest(String searchText) {
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
        teacher = getTeachers(currentPage);
        updateTeacherPage();
    }

    private void updateTeacherPage() {
        remove(teacherLayoutDiv);
        remove(pagingMenuLayout);
        teacherLayoutDiv = new Div();
        pagingMenuLayout = new HorizontalLayout();
        add(createTeacherCards());
        add(createPagingMenu(teacher.getTotalPages()));
    }

    private Component createTeacherCards() {
        teacherLayoutDiv = new Div();
        teacherLayoutDiv.addClassName("teacher-layout-div");

        HorizontalLayout teacherLayout = new HorizontalLayout();
        teacherLayout.getThemeList().remove("spacing");
        teacherLayout.addClassName("teacher-layout");

        for (Teacher result : teacher.getResults()) {
            Card card = createTeacherCard(result);
            teacherLayout.add(card);
        }

        teacherLayoutDiv.add(teacherLayout);

        return teacherLayoutDiv;
    }

    private Card createTeacherCard(Teacher oneTeacher) {
        Card card = new Card(
                new TitleLabel(oneTeacher.getName()),
                new PrimaryLabel(oneTeacher.getShortname()),
                new SecondaryLabel(oneTeacher.getDescription())
        );
        card.addClickListener(e -> {
            UI.getCurrent().getPage().executeJs("window.location = \"" + Auth.getURL() + "/documents/teacher/" + oneTeacher.getShortname() + "\"");
        });
        return card;
    }

    private Component createPagingMenu(int totalPages) {
        pagingMenuLayout.addClassName("paging-layout");


        Button previousPage = new Button("Previous");
        previousPage.addClickListener(e -> {
            if (currentPage > 1) {
                teacher = getTeachers(currentPage - 1);
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
            teacher = getTeachers(currentPage);
            currentPage = teacher.getPage();
            updateTeacherPage();
        });

        Button nextPage = new Button("Next");
        nextPage.addClickListener(e -> {
            if (currentPage < teacher.getTotalPages()) {
                teacher = getTeachers(currentPage + 1);
                currentPage = teacher.getPage();
                updateTeacherPage();
            }
        });

        Label currentPageText = new Label();
        currentPageText.setText(currentPage + " / " + totalPages);


        pagingMenuLayout.add(previousPage, currentPageText, nextPage, itemsPerPageSelect);

        return pagingMenuLayout;
    }

    private Teachers getTeachers(int page) {
        if (searchState) {
            int itemsVisible = Math.min(limit, foundIds.size() - ((page - 1) * limit));
            Teacher[] teacherAr = new Teacher[itemsVisible];
            int subjectArCounter = 0;
            for (int i = limit * (page - 1); i < ((page - 1) * limit) + itemsVisible; i++) {
                teacherAr[subjectArCounter] = resolveTeacherById(foundIds.get(i));
                subjectArCounter++;
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
}
