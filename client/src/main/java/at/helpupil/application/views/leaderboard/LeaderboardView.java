package at.helpupil.application.views.leaderboard;

import at.helpupil.application.utils.SecuredView;
import at.helpupil.application.utils.SessionStorage;
import at.helpupil.application.utils.ThemeHelper;
import at.helpupil.application.utils.responses.Error;
import at.helpupil.application.utils.responses.LeaderboardObj;
import at.helpupil.application.utils.responses.LeaderboardObjs;
import at.helpupil.application.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static at.helpupil.application.Application.BASE_URL;

/**
 * This view shows the 10 users with the most points on a leaderboard
 */
@Route(value = "leaderboard", layout = MainView.class)
@PageTitle("Leaderboard")
@CssImport("./views/leaderboard/leaderboard-view.css")
public class LeaderboardView extends SecuredView {

    /**
     * initializes Leaderboard
     */
    public LeaderboardView() {
        ThemeHelper.onLoad();

        addClassName("leaderboard-view");

        LeaderboardObjs leaderboardObjs = makeLeaderboardRequest();
        if (leaderboardObjs == null) return;
        List<LeaderboardObj> leaderboardObjList = new ArrayList<>(Arrays.asList(leaderboardObjs.getResults()));
        for (int i = 0; i < leaderboardObjList.size(); i++) {
            leaderboardObjList.get(i).setPlace(i+1);
        }

        Grid<LeaderboardObj> topUserGrid = new Grid<>(LeaderboardObj.class);
        topUserGrid.setColumns("place", "name", "wallet");
        topUserGrid.setItems(leaderboardObjList);


        add(topUserGrid);
    }

    /**
     * makes request to api to get the 10 users with the most points
     * @return 10 users with the most points
     */
    private LeaderboardObjs makeLeaderboardRequest() {
        HttpResponse<LeaderboardObjs> topUsers = Unirest.get(BASE_URL + "/users/public/leaderboard")
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .asObject(LeaderboardObjs.class);

        Error error = topUsers.mapError(Error.class);

        if (null == error) {
            return topUsers.getBody();
        } else {
            if (new Error(error.getCode(), error.getMessage()).continueCheck()) {
                return makeLeaderboardRequest();
            }
            return null;
        }
    }
}
