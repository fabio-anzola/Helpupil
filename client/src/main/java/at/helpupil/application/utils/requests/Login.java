package at.helpupil.application.utils.requests;

/**
 * Request Object of Login
 */
public class Login {
    /**
     * email of user
     */
    private String email;
    /**
     * password of user
     */
    private String password;

    /**
     * @param email of user
     * @param password of user
     */
    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * @return email of user who logs into his account
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email of user who logs into his account
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return password of user who logs into his account
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password of user who logs into his account
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
