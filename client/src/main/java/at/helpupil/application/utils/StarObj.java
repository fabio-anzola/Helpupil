package at.helpupil.application.utils;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

/**
 * Star icons for rating
 */
public class StarObj extends Icon {
    /**
     * star is filled, if state is true
     */
    private final boolean state;

    /**
     * @param state of star, true equals filled
     */
    public StarObj(boolean state) {
        super(state ? VaadinIcon.STAR : VaadinIcon.STAR_O);
        this.state = state;
    }

    /**
     * @return true if star is filled
     */
    public boolean getState() {
        return state;
    }
}
