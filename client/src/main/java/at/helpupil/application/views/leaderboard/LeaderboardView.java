package at.helpupil.application.views.leaderboard;

import at.helpupil.application.utils.SecuredView;
import at.helpupil.application.utils.SessionStorage;
import at.helpupil.application.utils.responses.Document;
import at.helpupil.application.utils.responses.Error;
import at.helpupil.application.utils.responses.LeaderboardObj;
import at.helpupil.application.utils.responses.LeaderboardObjs;
import at.helpupil.application.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static at.helpupil.application.Application.BASE_URL;

@Route(value = "leaderboard", layout = MainView.class)
@PageTitle("Leaderboard")
@CssImport("./views/leaderboard/leaderboard-view.css")
public class LeaderboardView extends SecuredView {
    private Grid<LeaderboardObj> topUserGrid = new Grid<>(LeaderboardObj.class);
    private LeaderboardObjs leaderboardObjs = makeLeaderboardRequest();

    public LeaderboardView() {
        addClassName("leaderboard-view");


        List<LeaderboardObj> leaderboardObjList = new ArrayList<>(Arrays.asList(leaderboardObjs.getResults()));
        for (int i = 0; i < leaderboardObjList.size(); i++) {
            leaderboardObjList.get(i).setPlace(i+1);
        }

        topUserGrid.setColumns("place", "name", "wallet");
        topUserGrid.setItems(leaderboardObjList);


        add(topUserGrid);
    }

    private LeaderboardObjs makeLeaderboardRequest() {
        HttpResponse<LeaderboardObjs> topUsers = Unirest.get(BASE_URL + "/users/public/leaderboard")
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .asObject(LeaderboardObjs.class);

        Error error = topUsers.mapError(Error.class);

        if (null == error) {
            return topUsers.getBody();
        } else {
            Notification.show(error.getMessage());
        }
        return null;
    }
}
