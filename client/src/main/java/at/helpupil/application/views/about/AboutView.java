package at.helpupil.application.views.about;

import at.helpupil.application.utils.ThemeHelper;
import at.helpupil.application.views.main.MainView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

@Route(value = "about", layout = MainView.class)
@PageTitle("About")
@CssImport("./views/about/about-view.css")
public class AboutView extends Div {

    public AboutView() {
        ThemeHelper.onLoad();

        addClassName("about-view");

        try {
            URL url = new URL("https://raw.githubusercontent.com/fabio-anzola/Helpupil/master/README.md");
            URLConnection con = url.openConnection();
            InputStream in = con.getInputStream();
            String body = new String(in.readAllBytes());

            Parser parser = Parser.builder().build();
            Node document = parser.parse(body);
            HtmlRenderer renderer = HtmlRenderer.builder().build();

            Div div = new Div();
            div.getElement().setProperty("innerHTML", renderer.render(document));

            add(div);
        } catch (IOException e) {
            add(new Text("Readme could not be loaded"));
        }
    }

}
