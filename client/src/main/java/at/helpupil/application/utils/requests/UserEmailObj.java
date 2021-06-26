package at.helpupil.application.utils.requests;

/**
 * Request object to patch an email
 */
public class UserEmailObj {
    /**
     * new email
     */
    private String email;

    /**
     * @param email new email
     */
    public UserEmailObj(String email) {
        this.email = email;
    }

    /**
     * @return new email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email set new email
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
