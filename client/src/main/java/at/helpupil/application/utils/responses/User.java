package at.helpupil.application.utils.responses;

public class User {
    private UserObj user;
    private Tokens tokens;

    public User(UserObj userObject, Tokens tokensObject) {
        user = userObject;
        tokens = tokensObject;
    }

    public UserObj getUser() {
        return user;
    }

    public Tokens getTokens() {
        return tokens;
    }

    public void setUser(UserObj userObject) {
        this.user = userObject;
    }

    public void setTokens(Tokens tokensObject) {
        this.tokens = tokensObject;
    }
}
