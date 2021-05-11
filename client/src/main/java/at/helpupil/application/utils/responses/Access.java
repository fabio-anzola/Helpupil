package at.helpupil.application.utils.responses;

public class Access {
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
