package at.helpupil.application.utils.responses;

public class Error {
    private float code;
    private String message;


    // Getter Methods

    public float getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    // Setter Methods

    public void setCode(float code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
