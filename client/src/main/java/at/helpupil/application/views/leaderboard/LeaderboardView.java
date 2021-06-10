package at.helpupil.application.views.leaderboard;

import at.helpupil.application.utils.SecuredView;
import at.helpupil.application.utils.SessionStorage;
import at.helpupil.application.utils.responses.Error;
import at.helpupil.application.utils.responses.LeaderboardObj;
import at.helpupil.application.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import static at.helpupil.application.Application.BASE_URL;

@Route(value = "leaderboard", layout = MainView.class)
@PageTitle("Leaderboard")
@CssImport("./views/leaderboard/leaderboard-view.css")
public class LeaderboardView extends SecuredView {
    public LeaderboardView() {
        addClassName("leaderboard-view");

    }

    private LeaderboardObj makeLeaderboardRequest() {
        HttpResponse<LeaderboardObj> topUsers = Unirest.get(BASE_URL + "/leaderboard")
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .asObject(LeaderboardObj.class);

        Error error = topUsers.mapError(Error.class);

        if (null == error) {
            return topUsers.getBody();
        } else {
            Notification.show(error.getMessage());
        }
        return null;
    }
}
