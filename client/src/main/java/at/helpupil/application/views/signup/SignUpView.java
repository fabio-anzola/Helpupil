package at.helpupil.application.views.signup;

import at.helpupil.application.utils.OpenView;
import at.helpupil.application.views.main.MainView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "signup", layout = MainView.class)
@PageTitle("Sign Up")
@CssImport("./views/signup/sign-up-view.css")
public class SignUpView extends OpenView {

    public SignUpView() {
        addClassName("sign-up-view");
        add(new Text("Content placeholder"));
    }

}
