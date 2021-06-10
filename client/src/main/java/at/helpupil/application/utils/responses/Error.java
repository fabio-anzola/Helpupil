package at.helpupil.application.utils.responses;

import at.helpupil.application.utils.Auth;
import at.helpupil.application.utils.SessionStorage;
import at.helpupil.application.utils.requests.Login;
import at.helpupil.application.utils.requests.RefreshObj;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import static at.helpupil.application.Application.BASE_URL;

public class Error {
    private float code;
    private String message;

    public Error(float code, String message) {
        this.code = code;
        this.message = message;
        checkForRefresh();
    }

    private void checkForRefresh() {
        if (this.code == 401 && this.message.equals("Please authenticate")) {
            refreshToken();
        }
    }

    private void refreshToken() {
        HttpResponse<Tokens> token = Unirest.post(BASE_URL + "/auth/refresh-tokens")
                .contentType("application/json")
                .body(new RefreshObj(SessionStorage.get().getTokens().getRefresh().getToken()))
                .asObject(Tokens.class);

        Error error = token.mapError(Error.class);

        if (null == error) {
            SessionStorage.update(token.getBody());
        } else {
            Notification.show(error.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);;
        }
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
