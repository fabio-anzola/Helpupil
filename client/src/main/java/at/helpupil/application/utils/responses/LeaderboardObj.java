package at.helpupil.application.utils.responses;

/**
 * Response object for user who appears in the leaderboard
 */
public class LeaderboardObj {
    /**
     * name of user who appears in the leaderboard
     */
    private String name;
    /**
     * wallet of user
     */
    private int wallet;
    /**
     * place on the leaderboard sorted by the highest wallet
     */
    private int place;

    /**
     * @param name of user
     * @param wallet of user
     */
    public LeaderboardObj(String name, int wallet) {
        this.name = name;
        this.wallet = wallet;
    }

    /**
     * @return name of user
     */
    public String getName() {
        return name;
    }

    /**
     * @param name of user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return wallet of user
     */
    public int getWallet() {
        return wallet;
    }

    /**
     * @param wallet of user
     */
    public void setWallet(int wallet) {
        this.wallet = wallet;
    }

    /**
     * @return place of user
     */
    public int getPlace() {
        return place;
    }

    /**
     * @param place of user
     */
    public void setPlace(int place) {
        this.place = place;
    }
}
