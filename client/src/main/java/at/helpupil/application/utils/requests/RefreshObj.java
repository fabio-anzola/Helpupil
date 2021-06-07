package at.helpupil.application.utils.requests;

public class RefreshObj {
    private String refreshToken;

    public RefreshObj(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
