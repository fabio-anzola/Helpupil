package at.helpupil.application.utils;

import at.helpupil.application.utils.responses.Tokens;
import at.helpupil.application.utils.responses.User;
import com.vaadin.flow.server.VaadinSession;

public class SessionStorage {

    public static void set(User value) {
        VaadinSession.getCurrent().setAttribute("User", value);
    }


    public static User get() {
        return (User) VaadinSession.getCurrent().getAttribute("User");
    }


    public static void update(Tokens value) {
        get().setTokens(value);
    }


    public static boolean isNull() {
        return null == get();
    }
}
