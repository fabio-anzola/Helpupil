package at.helpupil.application.utils;

import com.vaadin.flow.component.UI;

public class Auth {

    public static void redirectIfNotValid() {
        if (SessionStorage.isNull()) {
            UI.getCurrent().navigate("login");
            UI.getCurrent().getPage().reload();
        }
    }

    public static void redirectIfValid() {
        if (!SessionStorage.isNull()) {
            UI.getCurrent().navigate("documents");
            UI.getCurrent().getPage().reload();
        }
    }

}
