package at.helpupil.application.utils.responses;

/**
 * Response object of teacher
 */
public class Teacher {
    /**
     * name of teacher
     */
    private String name;
    /**
     * shortname of teacher
     */
    private String shortname;
    /**
     * description of teacher
     */
    private String description;
    /**
     * user id who created this teacher
     */
    private String user;
    /**
     * id of teacher
     */
    private String id;

    /**
     * @param name of teacher
     * @param shortname of teacher
     * @param description of teacher
     * @param user id of user who created this teacher
     * @param id of teacher
     */
    public Teacher(String name, String shortname, String description, String user, String id) {
        this.name = name;
        this.shortname = shortname;
        this.description = description;
        this.user = user;
        this.id = id;
    }

    /**
     * @return name of teacher
     */
    public String getName() {
        return name;
    }

    /**
     * @param name of teacher
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return shortname of teacher
     */
    public String getShortname() {
        return shortname;
    }

    /**
     * @param shortname of teacher
     */
    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    /**
     * @return description of teacher
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description of teacher
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return user id of user who created this teacher
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user id of user who created this teacher
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return id of teacher
     */
    public String getId() {
        return id;
    }

    /**
     * @param id of teacher
     */
    public void setId(String id) {
        this.id = id;
    }
}
