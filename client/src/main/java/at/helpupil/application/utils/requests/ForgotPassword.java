package at.helpupil.application.utils.requests;

/**
 * Request object of ForgotPassword
 */
public class ForgotPassword {
    /**
     * email of user who forgot his password
     */
    private String email;

    /**
     * @param email of user who forgot his password
     */
    public ForgotPassword(String email) {
        this.email = email;
    }

    /**
     * @return email of user who forgot his password
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email of user who forgot his password
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
