package at.helpupil.application.utils.responses;

public class Document {
    private String[] reviewer;
    private String name;
    private String type;
    private String user;
    private double rating;
    private File file;
    private String status;
    private String subject;
    private String teacher;
    private String id;

    public Document(String[] reviewer, String name, String type, String user, double rating, File file, String status, String subject, String teacher, String id) {
        this.reviewer = reviewer;
        this.name = name;
        this.type = type;
        this.user = user;
        this.rating = rating;
        this.file = file;
        this.status = status;
        this.subject = subject;
        this.teacher = teacher;
        this.id = id;
    }

    public String[] getReviewer() {
        return reviewer;
    }

    public void setReviewer(String[] reviewer) {
        this.reviewer = reviewer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
