package at.helpupil.application.utils.responses;

/**
 * Response object for document
 */
public class Document {
    /**
     * array of users who reviewed a document
     */
    private String[] reviewer;
    /**
     * name of document
     */
    private String name;
    /**
     * type of document (for example: homework)
     */
    private String type;
    /**
     * user id who uploaded this document
     */
    private String user;
    /**
     * rating of this document
     */
    private double rating;
    /**
     * file data
     */
    private File file;
    /**
     * status of approval (for example: declined)
     */
    private String status;
    /**
     * subject id of document
     */
    private String subject;
    /**
     * teacher id of document
     */
    private String teacher;
    /**
     * price of document
     */
    private int price;
    /**
     * id of document
     */
    private String _id;
    /**
     * literal username, not the id
     */
    private String uname;
    /**
     * literal subject name, not the id
     */
    private String subject_sn;
    /**
     * literal teacher name, not the id
     */
    private String teacher_sn;

    /**
     * @param reviewer of document
     * @param name of document
     * @param type of document
     * @param user id who uploaded this document
     * @param rating of document
     * @param file data of document
     * @param status of document
     * @param subject id of document
     * @param teacher id of document
     * @param price of document
     * @param _id of document
     * @param uname as string of the user who uploaded this document
     * @param subject_sn as string of the subject
     * @param teacher_sn as string of the teacher
     */
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

    /**
     * @return array of user who reviewed this document
     */
    public String[] getReviewer() {
        return reviewer;
    }

    /**
     * @param reviewer of document
     */
    public void setReviewer(String[] reviewer) {
        this.reviewer = reviewer;
    }

    /**
     * @return name of document
     */
    public String getName() {
        return name;
    }

    /**
     * @param name of document
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return type of document
     */
    public String getType() {
        return type;
    }

    /**
     * @param type of document
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return user id who uploaded this document
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user id who uploaded this document
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return rating of document
     */
    public double getRating() {
        return rating;
    }

    /**
     * @param rating of document
     */
    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     * @return file data
     */
    public File getFile() {
        return file;
    }

    /**
     * @param file data
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * @return status of document
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status of document
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return subject id of document
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject id of document
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return teacher id of document
     */
    public String getTeacher() {
        return teacher;
    }

    /**
     * @param teacher id of document
     */
    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    /**
     * @return price of document
     */
    public int getPrice() {
        return price;
    }

    /**
     * @param price of document
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * @return id of document
     */
    public String getId() {
        return _id;
    }

    /**
     * @param _id of document
     */
    public void setId(String _id) {
        this._id = _id;
    }

    /**
     * @return literal username
     */
    public String getUname() {
        return uname;
    }

    /**
     * @param uname literal username
     */
    public void setUname(String uname) {
        this.uname = uname;
    }

    /**
     * @return literal subject name
     */
    public String getSubject_sn() {
        return subject_sn;
    }

    /**
     * @param subject_sn literal subject name
     */
    public void setSubject_sn(String subject_sn) {
        this.subject_sn = subject_sn;
    }

    /**
     * @return literal teacher name
     */
    public String getTeacher_sn() {
        return teacher_sn;
    }

    /**
     * @param teacher_sn literal teacher name
     */
    public void setTeacher_sn(String teacher_sn) {
        this.teacher_sn = teacher_sn;
    }
}
