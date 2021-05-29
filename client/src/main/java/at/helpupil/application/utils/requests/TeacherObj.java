package at.helpupil.application.utils.requests;

public class TeacherObj {
    private String name;
    private String shortname;
    private String description;

    public TeacherObj(String name, String shortname, String description) {
        this.name = name;
        this.shortname = shortname;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
