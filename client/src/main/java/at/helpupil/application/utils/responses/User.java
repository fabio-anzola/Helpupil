package at.helpupil.application.utils.responses;

import java.util.ArrayList;

public class User {
    UserObj UserObject;
    Tokens TokensObject;


    // Getter Methods

    public UserObj getUser() {
        return UserObject;
    }

    public Tokens getTokens() {
        return TokensObject;
    }

    // Setter Methods

    public void setUser(UserObj userObject) {
        this.UserObject = userObject;
    }

    public void setTokens(Tokens tokensObject) {
        this.TokensObject = tokensObject;
    }
}

class Refresh {
    private String token;
    private String expires;


    // Getter Methods

    public String getToken() {
        return token;
    }

    public String getExpires() {
        return expires;
    }

    // Setter Methods

    public void setToken(String token) {
        this.token = token;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }
}

class UserObj {
    private String role;
    private boolean isEmailVerified;
    private float wallet;
    ArrayList< Object > purchasedDocuments = new ArrayList < Object > ();
    private String name;
    private String email;
    private String id;


    // Getter Methods

    public String getRole() {
        return role;
    }

    public boolean getIsEmailVerified() {
        return isEmailVerified;
    }

    public float getWallet() {
        return wallet;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    // Setter Methods

    public void setRole(String role) {
        this.role = role;
    }

    public void setIsEmailVerified(boolean isEmailVerified) {
        this.isEmailVerified = isEmailVerified;
    }

    public void setWallet(float wallet) {
        this.wallet = wallet;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }
}