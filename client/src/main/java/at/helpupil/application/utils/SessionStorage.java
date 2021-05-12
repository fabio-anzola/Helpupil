package at.helpupil.application.utils;

import at.helpupil.application.utils.responses.User;
import com.vaadin.flow.server.VaadinSession;

public class SessionStorage {

    public static void set(User value) {
        VaadinSession.getCurrent().setAttribute("User", value);
    }

    public static User get() {
        return (User) VaadinSession.getCurrent().getAttribute("User");
    }
}
