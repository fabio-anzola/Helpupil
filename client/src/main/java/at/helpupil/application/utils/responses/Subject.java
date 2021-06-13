package at.helpupil.application.utils.responses;

/**
 * Response object of a subject
 */
public class Subject {
    /**
     * name of a subject
     */
    private String name;
    /**
     * shortname of a subject
     */
    private String shortname;
    /**
     * description of a subject
     */
    private String description;
    /**
     * creator id of subject
     */
    private String user;
    /**
     * id of subject
     */
    private String id;

    /**
     * @param name of subject
     * @param shortname of subject
     * @param description of subject
     * @param user id who created subject
     * @param id of subject
     */
    public Subject(String name, String shortname, String description, String user, String id) {
        this.name = name;
        this.shortname = shortname;
        this.description = description;
        this.user = user;
        this.id = id;
    }

    /**
     * @return name of subject
     */
    public String getName() {
        return name;
    }

    /**
     * @param name of subject
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return shortname of subject
     */
    public String getShortname() {
        return shortname;
    }

    /**
     * @param shortname of subject
     */
    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    /**
     * @return description of subject
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description of subject
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return user id of user who created this subject
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user id of user who created this subject
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return id of subject
     */
    public String getId() {
        return id;
    }

    /**
     * @param id of subject
     */
    public void setId(String id) {
        this.id = id;
    }
}
