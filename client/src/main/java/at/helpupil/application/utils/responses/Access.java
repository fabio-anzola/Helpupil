package at.helpupil.application.utils.responses;

/**
 * Response object when logged into an account
 */
public class Access {
    /**
     * Access token of user
     */
    private String token;
    /**
     * date when token expires
     */
    private String expires;

    /**
     * @param token of user to access site
     * @param expires date when token expires
     */
    public Access(String token, String expires) {
        this.token = token;
        this.expires = expires;
    }

    /**
     * @return token of user
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
     * @param token of user
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @param expires date of token
     */
    public void setExpires(String expires) {
        this.expires = expires;
    }
}
