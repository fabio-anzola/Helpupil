package at.helpupil.application.utils;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * static methods to redirect the user
 */
public class Auth {

    /**
     * redirects user to login if session is null
     */
    public static void redirectIfNotValid() {
        if (SessionStorage.isNull()) {
            UI.getCurrent().getPage().executeJs("window.location = \"" + getURL() + "/login\"");
        }
    }

    /**
     * redirects user to document view if user is valid
     */
    public static void redirectIfValid() {
        if (!SessionStorage.isNull()) {
            UI.getCurrent().getPage().executeJs("window.location = \"" + getURL() + "/documents\"");
        }
    }

    /**
     * redirects user to login after register
     */
    public static void redirectOnRegister() {
        UI.getCurrent().getPage().executeJs("window.location = \"" + getURL() + "/login\"");
    }

    /**
     * @return current url
     */
    public static String getURL() {
        HttpServletRequest httpServletRequest = ((VaadinServletRequest) VaadinService.getCurrentRequest()).getHttpServletRequest();
        String[] requestUrl = httpServletRequest.getRequestURL().toString().split("/");
        return requestUrl[0] + "//" + requestUrl[2];
    }

}
