package at.helpupil.application.utils.requests;

/**
 * Object to create a new subject
 */
public class SubjectObj {
    /**
     * name of new subject
     */
    private String name;
    /**
     * shortname of new subject
     */
    private String shortname;
    /**
     * description of new subject
     */
    private String description;

    /**
     * @param name of new subject
     * @param shortname of new subject
     * @param description of new subject
     */
    public SubjectObj(String name, String shortname, String description) {
        this.name = name;
        this.shortname = shortname;
        this.description = description;
    }

    /**
     * @return name of new subject
     */
    public String getName() {
        return name;
    }

    /**
     * @param name of new subject
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return shortname of new subject
     */
    public String getShortname() {
        return shortname;
    }

    /**
     * @param shortname of new subject
     */
    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    /**
     * @return description of new subject
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description of new subject
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
