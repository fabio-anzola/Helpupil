package at.helpupil.application.utils;

import com.vaadin.flow.component.UI;

/**
 * Class for responsive user interface
 */
public class ResponsiveUI {
    /**
     * enum for small devices and desktops
     */
    public enum LayoutMode {
        SMALL, DESKTOP
    }

    /**
     * it is a array because then it is atomic
     * @return Desktop if width greater than 900
     */
    public static LayoutMode getLayoutMode() {
        int[] windowWidth = new int[1];
        UI.getCurrent().getPage().retrieveExtendedClientDetails(details -> windowWidth[0] = details.getWindowInnerWidth());

        if (windowWidth[0] != 0 && windowWidth[0] < 900) {
            return LayoutMode.SMALL;
        } else {
            return LayoutMode.DESKTOP;
        }
    }
}
