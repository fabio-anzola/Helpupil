package at.helpupil.application.utils;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.Lumo;

/**
 * Helper class to switch between light and dark mode
 */
public class ThemeHelper {

    /**
     * reads from session if dark mode is preferred
     */
    public static void onLoad() {
        ThemeList themeList = UI.getCurrent().getElement().getThemeList();
        if (readFromSession()) {
            themeList.add(Lumo.DARK);
        }
    }

    /**
     * activates/deactivates darkmode and saves it to session
     */
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

    /**
     * @param state saves state to session
     */
    public static void setToSession(boolean state) {
        VaadinSession.getCurrent().setAttribute("darkmode", state);
    }

    /**
     * @return true if darkmode is set to true in session
     */
    public static boolean readFromSession() {
        try {
            return (boolean) VaadinSession.getCurrent().getAttribute("darkmode");
        } catch (Exception e) {
            return false;
        }
    }
}
