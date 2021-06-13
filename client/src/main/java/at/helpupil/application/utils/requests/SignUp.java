package at.helpupil.application.utils.requests;

/**
 * Request object to sign up an account
 */
public class SignUp {
    /**
     * email of user
     */
    private String email;
    /**
     * name of user
     */
    private String name;
    /**
     * password of user
     */
    private String password;

    /**
     * @param email of user
     * @param name of user
     * @param password of user
     */
    public SignUp(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    /**
     * @return email of user
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email of user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return name of user
     */
    public String getName() {
        return name;
    }

    /**
     * @param name of user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return password of user
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password of user
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
