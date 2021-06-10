package at.helpupil.application.utils.responses;

public class LeaderboardObj {
    String username;
    int wallet;

    public LeaderboardObj(String username, int wallet) {
        this.username = username;
        this.wallet = wallet;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getWallet() {
        return wallet;
    }

    public void setWallet(int wallet) {
        this.wallet = wallet;
    }
}
