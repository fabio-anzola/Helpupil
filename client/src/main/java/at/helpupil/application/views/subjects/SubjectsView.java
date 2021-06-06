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

@Route(value = "subjects", layout = MainView.class)
@PageTitle("Subjects")
@CssImport("./views/subjects/subjects-view.css")
public class SubjectsView extends SecuredView {

    private Div subjectLayoutDiv = new Div();
    private HorizontalLayout pagingMenuLayout = new HorizontalLayout();

    private boolean searchState = false;
    private final ArrayList<String> foundIds = new ArrayList<>();
    private final int[] limits = new int[]{10, 15, 25};
    private int limit = limits[0];
    private int currentPage = 1;
    private Subjects subject = getSubjects(limit, currentPage);

    public SubjectsView() {
        addClassName("subjects-view");

        add(createSearchBox());
        add(createSubjectCards(subject));
        add(createPagingMenu(subject.getTotalPages()));
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
                subject = getSubjects(limit, currentPage);
                updateSubjectPage();
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
        subject = getSubjects(limit, currentPage);
        updateSubjectPage();
    }

    private void updateSubjectPage() {
        remove(subjectLayoutDiv);
        remove(pagingMenuLayout);
        subjectLayoutDiv = new Div();
        pagingMenuLayout = new HorizontalLayout();
        add(createSubjectCards(subject));
        add(createPagingMenu(subject.getTotalPages()));
    }

    private Component createSubjectCards(Subjects subject) {
        subjectLayoutDiv = new Div();
        subjectLayoutDiv.addClassName("subject-layout-div");

        HorizontalLayout subjectLayout = new HorizontalLayout();
        subjectLayout.getThemeList().remove("spacing");
        subjectLayout.addClassName("subject-layout");

        for (Subject result : subject.getResults()) {
            Card card = createSubjectCard(result);
            subjectLayout.add(card);
        }

        subjectLayoutDiv.add(subjectLayout);

        return subjectLayoutDiv;
    }

    private Card createSubjectCard(Subject subject) {
        Card card = new Card(
                new TitleLabel(subject.getName()),
                new PrimaryLabel(subject.getShortname()),
                new SecondaryLabel(subject.getDescription())
        );
        card.addClickListener(e -> {
            UI.getCurrent().getPage().executeJs("window.location = \"" + Auth.getURL() + "/documents/subject/" + subject.getShortname() + "\"");
        });
        return card;
    }

    private Component createPagingMenu(int totalPages) {
        pagingMenuLayout.addClassName("paging-layout");


        Button previousPage = new Button("Previous");
        previousPage.addClickListener(e -> {
            if (currentPage > 1) {
                subject = getSubjects(limit, currentPage - 1);
                currentPage = subject.getPage();
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
            currentPage = 1;
            subject = getSubjects(limit, currentPage);
            currentPage = subject.getPage();
            updateSubjectPage();
        });

        Button nextPage = new Button("Next");
        nextPage.addClickListener(e -> {
            if (currentPage < subject.getTotalPages()) {
                subject = getSubjects(limit, currentPage + 1);
                currentPage = subject.getPage();
                updateSubjectPage();
            }
        });

        Label currentPageText = new Label();
        currentPageText.setText(currentPage + " / " + totalPages);


        pagingMenuLayout.add(previousPage, currentPageText, nextPage, itemsPerPageSelect);

        return pagingMenuLayout;
    }

    private Subjects getSubjects(int limit, int page) {
        if (searchState) {
            int itemsVisible = Math.min(limit, foundIds.size() - ((page - 1) * limit));
            Subject[] subjectAr = new Subject[itemsVisible];
            int subjectArCounter = 0;
            for (int i = limit * (page - 1); i < ((page - 1) * limit) + itemsVisible; i++) {
                subjectAr[subjectArCounter] = resolveSubjectById(foundIds.get(i));
                subjectArCounter++;
            }
            if (subjectAr.length == 0) {
                currentPage = 0;
            }
            return new Subjects(subjectAr, page, limit, (int) Math.ceil((float) foundIds.size() / limit), foundIds.size());
        } else {
            HttpResponse<Subjects> subjects = Unirest.get(BASE_URL + "/subject")
                    .queryString("limit", limit)
                    .queryString("page", page)
                    .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                    .asObject(Subjects.class);

            Error error = subjects.mapError(Error.class);

            if (null == error) {
                if (subjects.getBody().getResults().length == 0) {
                    currentPage = 0;
                }
                return subjects.getBody();
            } else {
                Notification.show(error.getMessage());
            }
            return null;
        }
    }

    private Subject resolveSubjectById(String id) {
        HttpResponse<Subject> subject = Unirest.get(BASE_URL + "/subject/" + id)
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .asObject(Subject.class);

        Error error = subject.mapError(Error.class);

        if (null == error) {
            return subject.getBody();
        } else {
            return null;
        }
    }

}
