package at.helpupil.application.utils.responses;

public class LeaderboardObj {
    private String name;
    private int wallet;

    public LeaderboardObj(String name, int wallet) {
        this.name = name;
        this.wallet = wallet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWallet() {
        return wallet;
    }

    public void setWallet(int wallet) {
        this.wallet = wallet;
    }
}
