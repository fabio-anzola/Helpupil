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
    private int price;
    private String _id;
    private String uname;
    private String subject_sn;
    private String teacher_sn;

    public Document(String[] reviewer, String name, String type, String user, double rating, File file, String status, String subject, String teacher, int price, String _id, String uname, String subject_sn, String teacher_sn) {
        this.reviewer = reviewer;
        this.name = name;
        this.type = type;
        this.user = user;
        this.rating = rating;
        this.file = file;
        this.status = status;
        this.subject = subject;
        this.teacher = teacher;
        this.price = price;
        this._id = _id;
        this.uname = uname;
        this.subject_sn = subject_sn;
        this.teacher_sn = teacher_sn;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getSubject_sn() {
        return subject_sn;
    }

    public void setSubject_sn(String subject_sn) {
        this.subject_sn = subject_sn;
    }

    public String getTeacher_sn() {
        return teacher_sn;
    }

    public void setTeacher_sn(String teacher_sn) {
        this.teacher_sn = teacher_sn;
    }
}
