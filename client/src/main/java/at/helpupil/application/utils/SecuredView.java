package at.helpupil.application.utils;

import com.vaadin.flow.component.html.Div;

/**
 * Only logged in users can see this view
 */
public class SecuredView extends Div {
    /**
     * redirects users to login if they aren't logged in
     */
    public SecuredView() {
        super();
        Auth.redirectIfNotValid();
    }
}
