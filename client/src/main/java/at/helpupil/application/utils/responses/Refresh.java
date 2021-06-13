package at.helpupil.application.utils.responses;

/**
 * Response object of Refresh
 */
public class Refresh {
    /**
     * refresh token to get new access token
     */
    private String token;
    /**
     * date when refresh token expires
     */
    private String expires;

    /**
     * @param token refresh token
     * @param expires date when token expires
     */
    public Refresh(String token, String expires) {
        this.token = token;
        this.expires = expires;
    }

    /**
     * @return refresh token
     */
    public String getToken() {
        return token;
    }

    /**
     * @return date when token expires
     */
    public String getExpires() {
        return expires;
    }

    /**
     * @param token refresh token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @param expires date when refresh token expires
     */
    public void setExpires(String expires) {
        this.expires = expires;
    }
}