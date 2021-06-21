package at.helpupil.application.utils;

import at.helpupil.application.utils.requests.Login;
import at.helpupil.application.utils.responses.Error;
import at.helpupil.application.utils.responses.Tokens;
import at.helpupil.application.utils.responses.User;
import at.helpupil.application.utils.responses.UserObj;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.VaadinSession;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import static at.helpupil.application.Application.BASE_URL;

/**
 * Class to manage session tokens
 */
public class SessionStorage {

    /**
     * @param value set user in session
     */
    public static void set(User value) {
        VaadinSession.getCurrent().setAttribute("User", value);
    }

    /**
     * @return user stored in session
     */
    public static User get() {
        return (User) VaadinSession.getCurrent().getAttribute("User");
    }

    /**
     * @param value update token in session
     */
    public static void update(Tokens value) {
        get().setTokens(value);
    }

    /**
     * @param value update user in session
     */
    public static void update(UserObj value) {
        get().setUser(value);
    }

    /**
     * @return true if session is null
     */
    public static boolean isNull() {
        return null == get();
    }

    /**
     * update current user data of storage from db
     */
    public static void updateUserFromDB() {
        if (SessionStorage.isNull()) {
            return;
        }

        HttpResponse<UserObj> userObj = Unirest.get(BASE_URL + "/users/" + SessionStorage.get().getUser().getId())
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .asObject(UserObj.class);

        Error error = userObj.mapError(Error.class);

        if (null == error) {
            SessionStorage.update(userObj.getBody());
        } else {
            Notification.show(error.getMessage());
        }
    }
}
