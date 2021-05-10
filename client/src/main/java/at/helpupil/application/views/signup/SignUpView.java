package at.helpupil.application.views.signup;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import at.helpupil.application.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;

@Route(value = "signup", layout = MainView.class)
@PageTitle("Sign Up")
@CssImport("./views/signup/sign-up-view.css")
public class SignUpView extends Div {

    public SignUpView() {
        addClassName("sign-up-view");
        add(new Text("Content placeholder"));
    }

}
