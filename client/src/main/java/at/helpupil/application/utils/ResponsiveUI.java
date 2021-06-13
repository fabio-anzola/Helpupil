package at.helpupil.application.utils;

import com.vaadin.flow.component.UI;

public class ResponsiveUI {
    public enum LayoutMode {
        SMALL, DESKTOP
    }

    private static int windowWidth;

    public static LayoutMode getLayoutMode() {
        UI.getCurrent().getPage().retrieveExtendedClientDetails(details -> windowWidth = details.getWindowInnerWidth());
        if (windowWidth < 900) {
            return LayoutMode.SMALL;
        } else {
            return LayoutMode.DESKTOP;
        }
    }
}
