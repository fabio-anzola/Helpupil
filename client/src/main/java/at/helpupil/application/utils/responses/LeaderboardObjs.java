package at.helpupil.application.utils.responses;

/**
 * ResponseObj for all users who appear on the leaderboard
 */
public class LeaderboardObjs {
    /**
     * max 10 users who have the highest wallet
     */
    private LeaderboardObj[] results;

    /**
     * @param results users who have the highest wallet
     */
    public LeaderboardObjs(LeaderboardObj[] results) {
        this.results = results;
    }

    /**
     * @return users who have the highest wallet
     */
    public LeaderboardObj[] getResults() {
        return results;
    }

    /**
     * @param results users who have the highest wallet
     */
    public void setResults(LeaderboardObj[] results) {
        this.results = results;
    }
}
