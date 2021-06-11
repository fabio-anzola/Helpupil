package at.helpupil.application.utils;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.Lumo;

public class ThemeHelper {

    public static void onLoad() {
        ThemeList themeList = UI.getCurrent().getElement().getThemeList();
        if (!readFromSession()) {
            themeList.add(Lumo.DARK);
        }
    }

    public static void onClick() {
        ThemeList themeList = UI.getCurrent().getElement().getThemeList();

        if (themeList.contains(Lumo.DARK)) {
            themeList.remove(Lumo.DARK);
            setToSession(false);
        } else {
            themeList.add(Lumo.DARK);
            setToSession(true);
        }
    }

    public static void setToSession(boolean state) {
        VaadinSession.getCurrent().setAttribute("darkmode", state);
    }

    public static boolean readFromSession() {
        try {
            return (boolean) VaadinSession.getCurrent().getAttribute("darkmode");
        } catch (Exception e) {
            return false;
        }
    }
}
