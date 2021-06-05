package at.helpupil.application.utils;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

public class StarObj extends Icon {
    private final boolean state;

    public StarObj(boolean state) {
        super(state ? VaadinIcon.STAR : VaadinIcon.STAR_O);
        this.state = state;
    }

    public boolean getState() {
        return state;
    }
}
