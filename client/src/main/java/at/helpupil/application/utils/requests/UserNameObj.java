package at.helpupil.application.utils.requests;

/**
 * Request object to patch a username
 */
public class UserNameObj {
    /**
     * new username
     */
    private String name;

    /**
     * @param name new username
     */
    public UserNameObj(String name) {
        this.name = name;
    }

    /**
     * @return new username
     */
    public String getName() {
        return name;
    }

    /**
     * @param name set new username
     */
    public void setName(String name) {
        this.name = name;
    }
}
