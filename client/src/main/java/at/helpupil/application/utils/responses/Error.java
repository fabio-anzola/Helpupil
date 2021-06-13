package at.helpupil.application.utils.responses;

import at.helpupil.application.utils.SessionStorage;
import at.helpupil.application.utils.requests.RefreshObj;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import static at.helpupil.application.Application.BASE_URL;

/**
 * Response object if an error occurs
 */
public class Error {
    /**
     * number of error (for example: 404)
     */
    private float code;
    /**
     * message of error (for example: not found)
     */
    private String message;

    /**
     * @param code of error
     * @param message of error
     */
    public Error(float code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return true and sends a refresh request to api if user is unauthorized
     */
    public boolean continueCheck() {
        if (checkForRefresh()) {
            refreshToken();
            return true;
        }
        Notification.show("Sorry something went wrong: " + this.message).addThemeVariants(NotificationVariant.LUMO_ERROR);
        return false;
    }

    /**
     * @return true if you aren't authorized
     */
    private boolean checkForRefresh() {
        return this.code == 401 && this.message.equals("Please authenticate");
    }

    /**
     * sends http request to api to refresh access token
     */
    private void refreshToken() {
        HttpResponse<Tokens> token = Unirest.post(BASE_URL + "/auth/refresh-tokens")
                .contentType("application/json")
                .body(new RefreshObj(SessionStorage.get().getTokens().getRefresh().getToken()))
                .asObject(Tokens.class);

        Error error = token.mapError(Error.class);

        if (null == error) {
            SessionStorage.update(token.getBody());
        } else {
            Notification.show(error.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    /**
     * @return code of error
     */
    public float getCode() {
        return code;
    }

    /**
     * @return message of error
     */
    public String getMessage() {
        return message;
    }

    // Setter Methods

    /**
     * @param code of error
     */
    public void setCode(float code) {
        this.code = code;
    }

    /**
     * @param message of error
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
