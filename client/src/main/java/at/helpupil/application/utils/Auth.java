package at.helpupil.application.utils;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;

import javax.servlet.http.HttpServletRequest;

public class Auth {

    public static void redirectIfNotValid() {
        if (SessionStorage.isNull()) {
            HttpServletRequest httpServletRequest = ((VaadinServletRequest) VaadinService.getCurrentRequest()).getHttpServletRequest();
            String[] requestUrl = httpServletRequest.getRequestURL().toString().split("/");
            String req = requestUrl[0] + "//" + requestUrl[2];
            UI.getCurrent().getPage().executeJs("window.location = \"" + req + "/login\"");
        }
    }

    public static void redirectIfValid() {
        if (!SessionStorage.isNull()) {
            HttpServletRequest httpServletRequest = ((VaadinServletRequest) VaadinService.getCurrentRequest()).getHttpServletRequest();
            String[] requestUrl = httpServletRequest.getRequestURL().toString().split("/");
            String req = requestUrl[0] + "//" + requestUrl[2];
            UI.getCurrent().getPage().executeJs("window.location = \"" + req + "/documents\"");
        }
    }

}
