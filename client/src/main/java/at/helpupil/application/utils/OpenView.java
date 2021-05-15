package at.helpupil.application.utils;

import com.vaadin.flow.component.html.Div;

public class OpenView extends Div {
    public OpenView() {
        super();
        Auth.redirectIfValid();
    }
}
