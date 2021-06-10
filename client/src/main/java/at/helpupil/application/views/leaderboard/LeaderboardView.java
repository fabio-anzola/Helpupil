package at.helpupil.application.views.leaderboard;

import at.helpupil.application.utils.SecuredView;
import at.helpupil.application.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "leaderboard", layout = MainView.class)
@PageTitle("Leaderboard")
@CssImport("./views/leaderboard/leaderboard-view.css")
public class LeaderboardView extends SecuredView {
    public LeaderboardView() {
        addClassName("leaderboard-view");
        
    }
}
