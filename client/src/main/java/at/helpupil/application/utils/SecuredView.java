package at.helpupil.application.utils;

import com.vaadin.flow.component.html.Div;

public class SecuredView extends Div {
    public SecuredView() {
        super();
        Auth.redirectIfNotValid();
    }
}
