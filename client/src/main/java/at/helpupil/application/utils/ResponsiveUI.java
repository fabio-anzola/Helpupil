package at.helpupil.application.utils;

import com.vaadin.flow.component.UI;

public class ResponsiveUI {
    public enum LayoutMode {
        SMALL, DESKTOP
    }

    public static LayoutMode getLayoutMode() {
        int[] windowWidth = new int[1];
        UI.getCurrent().getPage().retrieveExtendedClientDetails(details -> windowWidth[0] = details.getWindowInnerWidth());
        if (windowWidth[0] < 900) {
            return LayoutMode.SMALL;
        } else {
            return LayoutMode.DESKTOP;
        }
    }
}
