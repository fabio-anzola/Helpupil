package at.helpupil.application.utils.responses;

public class Access {
    private String token;
    private String expires;

    public Access(String token, String expires) {
        this.token = token;
        this.expires = expires;
    }

    public String getToken() {
        return token;
    }

    public String getExpires() {
        return expires;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }
}
