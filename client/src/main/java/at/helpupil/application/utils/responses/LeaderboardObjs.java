package at.helpupil.application.utils.responses;

public class LeaderboardObjs {
    private LeaderboardObj[] results;

    public LeaderboardObjs(LeaderboardObj[] results) {
        this.results = results;
    }

    public LeaderboardObj[] getResults() {
        return results;
    }

    public void setResults(LeaderboardObj[] results) {
        this.results = results;
    }
}
