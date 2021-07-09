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
     * current password
     */
    private String currentPassword;

    /**
     * @param email new email
     */
    public UserEmailObj(String email, String currentPassword) {
        this.email = email;
        this.currentPassword = currentPassword;
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

    /**
     * @return current password
     */
    public String getCurrentPassword() {
        return currentPassword;
    }

    /**
     * @param currentPassword set current password
     */
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }
}
