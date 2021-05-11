package at.helpupil.application.utils.responses;

public class Error {
    private float code;
    private String message;

    public Error(float code, String message) {
        this.code = code;
        this.message = message;
    }

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
