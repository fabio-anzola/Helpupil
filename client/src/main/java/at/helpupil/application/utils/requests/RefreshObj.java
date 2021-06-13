package at.helpupil.application.utils.requests;

/**
 * Request Object of RefreshObj to refresh access token
 */
public class RefreshObj {
    /**
     * refresh token of user
     */
    private String refreshToken;

    /**
     * @param refreshToken of user
     */
    public RefreshObj(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     * @return refreshToken of user
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * @param refreshToken of user
     */
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
