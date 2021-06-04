package at.helpupil.application.utils;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

public class StarObj extends Icon {
    private int index;

    public StarObj(VaadinIcon icon, int index) {
        super(icon);
        this.index = index;
    }

    public StarObj(String icon, int index) {
        super(icon);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
