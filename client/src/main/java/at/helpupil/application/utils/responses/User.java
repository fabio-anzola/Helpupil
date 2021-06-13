package at.helpupil.application.utils.responses;

/**
 * Response object of user
 */
public class User {
    /**
     * user object without tokens
     */
    private UserObj user;
    /**
     * access and refresh tokens
     */
    private Tokens tokens;

    /**
     * @param userObject without tokens
     * @param tokensObject access and refresh tokens
     */
    public User(UserObj userObject, Tokens tokensObject) {
        user = userObject;
        tokens = tokensObject;
    }

    /**
     * @return user
     */
    public UserObj getUser() {
        return user;
    }

    /**
     * @return tokens
     */
    public Tokens getTokens() {
        return tokens;
    }

    /**
     * @param userObject without tokens
     */
    public void setUser(UserObj userObject) {
        this.user = userObject;
    }

    /**
     * @param tokensObject access and refresh tokens
     */
    public void setTokens(Tokens tokensObject) {
        this.tokens = tokensObject;
    }
}
