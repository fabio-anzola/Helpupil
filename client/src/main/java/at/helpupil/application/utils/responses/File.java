package at.helpupil.application.utils.responses;

/**
 * Response object of file
 */
public class File {
    /**
     * fieldname
     */
    private String fieldname;
    /**
     * original name of file
     */
    private String originalname;
    /**
     * encoding of file
     */
    private String encoding;
    /**
     * mimetype of file
     */
    private String mimetype;
    /**
     * destination where file is stored
     */
    private String destination;
    /**
     * hash name of file
     */
    private String filename;
    /**
     * path where file is stored
     */
    private String path;
    /**
     * size of file
     */
    private long size;

    /**
     * @param fieldname of file
     * @param originalname of file
     * @param encoding of file
     * @param mimetype of file
     * @param destination where file is stored
     * @param filename of file
     * @param path where file is stored
     * @param size of file
     */
    public File(String fieldname, String originalname, String encoding, String mimetype, String destination, String filename, String path, long size) {
        this.fieldname = fieldname;
        this.originalname = originalname;
        this.encoding = encoding;
        this.mimetype = mimetype;
        this.destination = destination;
        this.filename = filename;
        this.path = path;
        this.size = size;
    }

    /**
     * @return fieldname of file
     */
    public String getFieldname() {
        return fieldname;
    }

    /**
     * @param fieldname of file
     */
    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }

    /**
     * @return original name of file
     */
    public String getOriginalname() {
        return originalname;
    }

    /**
     * @param originalname of file
     */
    public void setOriginalname(String originalname) {
        this.originalname = originalname;
    }

    /**
     * @return encoding of file
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * @param encoding of file
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * @return mimetype of file
     */
    public String getMimetype() {
        return mimetype;
    }

    /**
     * @param mimetype of file
     */
    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    /**
     * @return destination where file is stored
     */
    public String getDestination() {
        return destination;
    }

    /**
     * @param destination where file is stored
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * @return filename of file
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename of file
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return path where file is stored
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path where file is stored
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return size of file
     */
    public long getSize() {
        return size;
    }

    /**
     * @param size of file
     */
    public void setSize(long size) {
        this.size = size;
    }
}
