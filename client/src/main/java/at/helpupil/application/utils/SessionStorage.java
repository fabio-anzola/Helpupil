package at.helpupil.application.utils;

import at.helpupil.application.utils.responses.Tokens;
import at.helpupil.application.utils.responses.User;
import at.helpupil.application.utils.responses.UserObj;
import com.vaadin.flow.server.VaadinSession;

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
}
