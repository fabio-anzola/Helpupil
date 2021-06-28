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
     * current password
     */
    private String currentPassword;

    /**
     * @param password new password
     */
    public UserPasswordObj(String password, String currentPassword) {
        this.password = password;
        this.currentPassword = currentPassword;
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
