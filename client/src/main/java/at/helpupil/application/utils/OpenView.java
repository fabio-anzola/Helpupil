package at.helpupil.application.utils;

import com.vaadin.flow.component.html.Div;

/**
 * every user can see this view
 */
public class OpenView extends Div {
    /**
     * redirects user to documents view if he is logged in
     */
    public OpenView() {
        super();
        Auth.redirectIfValid();
    }
}
