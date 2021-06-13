package at.helpupil.application.utils.requests;

/**
 * Object to create a new teacher
 */
public class TeacherObj {
    /**
     * name of new teacher
     */
    private String name;
    /**
     * shortname of new teacher
     */
    private String shortname;
    /**
     * description of new teacher
     */
    private String description;

    /**
     * @param name of new teacher
     * @param shortname of new teacher
     * @param description of new teacher
     */
    public TeacherObj(String name, String shortname, String description) {
        this.name = name;
        this.shortname = shortname;
        this.description = description;
    }

    /**
     * @return name of new teacher
     */
    public String getName() {
        return name;
    }

    /**
     * @param name of new teacher
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return shortname of new teacher
     */
    public String getShortname() {
        return shortname;
    }

    /**
     * @param shortname of new teacher
     */
    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    /**
     * @return description of new teacher
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description of new teacher
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
