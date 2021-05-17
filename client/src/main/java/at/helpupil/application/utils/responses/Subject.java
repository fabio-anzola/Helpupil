package at.helpupil.application.utils.responses;

public class Subject {
    private String name;
    private String shortname;
    private String description;
    private String user;
    private String id;

    public Subject(String name, String shortname, String description, String user, String id) {
        this.name = name;
        this.shortname = shortname;
        this.description = description;
        this.user = user;
        this.id = id;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
