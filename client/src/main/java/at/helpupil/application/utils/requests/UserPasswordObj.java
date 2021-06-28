package at.helpupil.application.utils.requests;

/**
 * Request object to patch a password
 */
public class UserPasswordObj {
    /**
     * new password
     */
    private String password;

    /**
     * @param password new password
     */
    public UserPasswordObj(String password) {
        this.password = password;
    }

    /**
     * @return new password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password set new password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
