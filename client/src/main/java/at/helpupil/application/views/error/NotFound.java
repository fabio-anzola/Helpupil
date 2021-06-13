package at.helpupil.application.views.error;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * redirects User
 */
@Controller
public class NotFound implements ErrorController {

    /**
     * path of error
     */
    private static final String PATH = "/error";

    /**
     * @return redirect string
     */
    @RequestMapping(value = PATH)
    public String error() {
        return "redirect:/errorpage";
    }

    /**
     * @return path
     */
    @Override
    public String getErrorPath() {
        return PATH;
    }
}
