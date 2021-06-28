package at.helpupil.application.views.main;

import at.helpupil.application.utils.Auth;
import at.helpupil.application.utils.SessionStorage;
import at.helpupil.application.utils.ThemeHelper;
import at.helpupil.application.utils.requests.SignUp;
import at.helpupil.application.utils.requests.UserEmailObj;
import at.helpupil.application.utils.requests.UserNameObj;
import at.helpupil.application.utils.requests.UserPasswordObj;
import at.helpupil.application.utils.responses.Error;
import at.helpupil.application.utils.responses.UserObj;
import at.helpupil.application.views.about.AboutView;
import at.helpupil.application.views.documents.DocumentsView;
import at.helpupil.application.views.leaderboard.LeaderboardView;
import at.helpupil.application.views.login.LoginView;
import at.helpupil.application.views.moderator.ModeratorView;
import at.helpupil.application.views.signup.SignUpView;
import at.helpupil.application.views.subjects.SubjectsView;
import at.helpupil.application.views.teachers.TeachersView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinSession;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.util.Optional;

import static at.helpupil.application.Application.BASE_URL;
import static at.helpupil.application.utils.Auth.getURL;

/**
 * The main view is a top-level placeholder for other views.
 */
@PWA(name = "Helpupil", shortName = "Helpupil", enableInstallPrompt = false)
@JsModule("./styles/shared-styles.js")
@CssImport("lumo-css-framework/all-classes.css")
@CssImport(value = "./views/responsive-dialog.css", themeFor = "vaadin-dialog-overlay")
@CssImport("./views/main/main-view.css")
public class MainView extends AppLayout {

    /**
     * menu to select the tabs
     */
    private final Tabs menu;
    /**
     * title of this view
     */
    private H1 viewTitle;

    /**
     * initializes main view
     */
    public MainView() {
        VaadinSession.getCurrent().getSession().setMaxInactiveInterval(30 * 24 * 60 * 60); // 30 days
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));
        //((Tab) menu.getChildren().findFirst().get()).setSelected(true);
    }

    /**
     * @return header content with avatarmenu and viewtitle
     */
    private Component createHeaderContent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        layout.getThemeList().set("dark", true);
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(new DrawerToggle());
        viewTitle = new H1();
        layout.add(viewTitle);
        layout.add(createAvatarMenu());
        return layout;
    }

    /**
     * @param menu where the tabs are shown
     * @return drawer content with a button to switch between light and dark mode
     */
    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.add(new Image("images/logo.png", "Helpupil logo"));
        logoLayout.add(new H1("Helpupil"));

        logoLayout.add(new Button(new Icon(VaadinIcon.LIGHTBULB), click -> ThemeHelper.onClick()));

        layout.add(logoLayout, menu);
        return layout;
    }

    /**
     * @return menu for tabs
     */
    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");
        tabs.add(createMenuItems());
        return tabs;
    }

    /**
     * menu is different when you are logged out, logged in or a moderator/admin
     *
     * @return menu with each view
     */
    private Component[] createMenuItems() {
        if (SessionStorage.isNull()) {
            return new Tab[]{
                    createTab("Login", LoginView.class),
                    createTab("Sign Up", SignUpView.class),
                    createTab("About", AboutView.class)
            };
        }
        if (SessionStorage.get().getUser().getRole().equals("moderator")
                || SessionStorage.get().getUser().getRole().equals("admin")) {
            return new Tab[]{
                    createTab("Documents", DocumentsView.class),
                    createTab("Teachers", TeachersView.class),
                    createTab("Subjects", SubjectsView.class),
                    createTab("Moderator", ModeratorView.class),
                    createTab("Leaderboard", LeaderboardView.class),
                    createTab("About", AboutView.class)};
        }
        return new Tab[]{
                createTab("Documents", DocumentsView.class),
                createTab("Teachers", TeachersView.class),
                createTab("Subjects", SubjectsView.class),
                createTab("Leaderboard", LeaderboardView.class),
                createTab("About", AboutView.class)};
    }

    /**
     * @param text             of tab
     * @param navigationTarget destination of target
     * @return new tab
     */
    private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
        final Tab tab = new Tab();
        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    /**
     * method is called after navigation
     * sets viewtitle to a new title
     */
    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
        viewTitle.setText(getCurrentPageTitle());
    }

    /**
     * @param component where a tab will be returned
     * @return tab for given component
     */
    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren().filter(tab -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }

    /**
     * @return current page title
     */
    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }


    /**
     * @return avatar menu for various user-specific interactions
     */
    private MenuBar createAvatarMenu() {
        MenuBar menuBar = new MenuBar();

        if (SessionStorage.isNull()) {
            menuBar.getStyle().set("display", "none");
            return menuBar;
        }

        menuBar.addClassName("avatar-menu");
        menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY);
        Avatar avatar = new Avatar();
        avatar.setName(SessionStorage.get().getUser().getName());
        MenuItem avatarItem = menuBar.addItem(avatar);


        Span userSpan = new Span(SessionStorage.get().getUser().getName());
        Icon userIcon = new Icon(VaadinIcon.USER);
        HorizontalLayout userLayout = new HorizontalLayout();
        userLayout.add(userIcon, userSpan);
        avatarItem.getSubMenu().addItem(userLayout,
                e -> showSettingsDialog());


        Span walletSpan = new Span(String.valueOf(SessionStorage.get().getUser().getWallet()));
        Icon walletIcon = new Icon(VaadinIcon.WALLET);
        HorizontalLayout walletLayout = new HorizontalLayout();
        walletLayout.add(walletIcon, walletSpan);
        avatarItem.getSubMenu().addItem(walletLayout);


        Span logoutSpan = new Span("Logout");
        Icon logoutIcon = new Icon(VaadinIcon.POWER_OFF);
        HorizontalLayout logoutLayout = new HorizontalLayout();
        logoutLayout.add(logoutIcon, logoutSpan);
        avatarItem.getSubMenu().addItem(logoutLayout,
                e -> {
                    SessionStorage.set(null);
                    Auth.redirectIfNotValid();
                });


        avatarItem.getSubMenu().getItems()
                .forEach(e -> e.getChildren().forEach(n -> {
                    n.getElement().getStyle().set("margin", "8px 0");
                    n.getChildren()
                            .filter(m -> m.getClass() == Icon.class).forEach(o -> ((Icon) o)
                            .getStyle().set("width", "20px").set("margin-right", "5px"));
                    n.getChildren()
                            .filter(m -> m.getClass() == Span.class).forEach(o -> ((Span) o)
                            .getStyle().set("margin-top", "auto").set("margin-bottom", "auto").set("padding-bottom", "2px"));
                }));

        avatarItem.addClickListener(e -> {
            SessionStorage.updateUserFromDB();
            walletSpan.setText(String.valueOf(SessionStorage.get().getUser().getWallet()));
        });

        return menuBar;
    }

    /**
     * Open Settings Dialog
     */
    private void showSettingsDialog() {
        Dialog dialog = new Dialog();
        dialog.setMinWidth("40vw");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.addClassName("dialog-layout");
        dialogLayout.addClassName("settings-layout");

        Label dialogHeading = new Label("User Settings");

        Button changeUsernameButton = new Button("Change Username");
        changeUsernameButton.addClickListener(e -> {
            dialog.close();
            showChangeUsernameDialog();
        });
        Button changeEmailButton = new Button("Change Email");
        changeEmailButton.addClickListener(e -> {
            dialog.close();
            showChangeEmailDialog();
        });
        Button changePasswordButton = new Button("Change Password");
        changePasswordButton.addClickListener(e -> {
            dialog.close();
            showChangePasswordDialog();
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(e -> dialog.close());
        HorizontalLayout dialogButtonLayout = new HorizontalLayout(cancelButton);

        dialogLayout.add(dialogHeading, changeUsernameButton, changeEmailButton, changePasswordButton, dialogButtonLayout);

        dialog.add(dialogLayout);
        dialog.open();
    }

    /**
     * Open Change-Username Dialog
     */
    private void showChangeUsernameDialog() {
        Dialog dialog = new Dialog();
        dialog.setMinWidth("40vw");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.addClassName("dialog-layout");

        Label dialogHeading = new Label("Change Username");

        TextField name = new TextField("Username");
        name.setPlaceholder(SessionStorage.get().getUser().getName());

        Button confirmButton = new Button("Confirm");
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmButton.addClickListener(e -> {
            String nameTrimmed = name.getValue().trim();
            if (nameTrimmed.isEmpty()) {
                Notification.show("Check your input");
            } else if (nameTrimmed.equals(SessionStorage.get().getUser().getName())) {
                Notification.show("Current Username equals your input!");
            } else {
                if (makeChangeUsernameRequest(nameTrimmed)) {
                    dialog.close();
                }
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(e -> {
            dialog.close();
            showSettingsDialog();
        });
        HorizontalLayout dialogButtonLayout = new HorizontalLayout(confirmButton, cancelButton);

        dialogLayout.add(dialogHeading, name, dialogButtonLayout);

        dialog.add(dialogLayout);
        dialog.open();
    }

    /**
     * Open Change-Email Dialog
     */
    private void showChangeEmailDialog() {
        Dialog dialog = new Dialog();
        dialog.setMinWidth("40vw");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.addClassName("dialog-layout");

        Label dialogHeading = new Label("Change Email");

        EmailField email = new EmailField("Email address");
        email.setErrorMessage("Please enter a valid email address");
        email.setPlaceholder(SessionStorage.get().getUser().getEmail());
        PasswordField password = new PasswordField("Password");

        Button confirmButton = new Button("Confirm");
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmButton.addClickListener(e -> {
            String emailValue = email.getValue().trim();
            String passwordValue = password.getValue();
            if (emailValue.isEmpty() || email.isInvalid() || passwordValue.isEmpty()) {
                Notification.show("Check your input");
            } else if (emailValue.equals(SessionStorage.get().getUser().getEmail())) {
                Notification.show("Current Email equals your input!");
            } else {
                if (makeChangeEmailRequest(emailValue, passwordValue)) {
                    dialog.close();
                }
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(e -> {
            dialog.close();
            showSettingsDialog();
        });
        HorizontalLayout dialogButtonLayout = new HorizontalLayout(confirmButton, cancelButton);

        dialogLayout.add(dialogHeading, email, password, dialogButtonLayout);

        dialog.add(dialogLayout);
        dialog.open();
    }

    /**
     * Open Change-Password Dialog
     */
    private void showChangePasswordDialog() {
        Dialog dialog = new Dialog();
        dialog.setMinWidth("40vw");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.addClassName("dialog-layout");

        Label dialogHeading = new Label("Change Password");

        PasswordField currentPassword = new PasswordField("Current Password");
        PasswordField newPassword = new PasswordField("New Password");

        Button confirmButton = new Button("Confirm");
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmButton.addClickListener(e -> {
            String newPasswordValue = newPassword.getValue();
            String currentPasswordValue = currentPassword.getValue();
            if (newPasswordValue.isEmpty() || currentPasswordValue.isEmpty()) {
                Notification.show("Check your input");
            } else {
                if (makeChangePasswordRequest(newPasswordValue, currentPasswordValue)) {
                    dialog.close();
                }
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(e -> {
            dialog.close();
            showSettingsDialog();
        });
        HorizontalLayout dialogButtonLayout = new HorizontalLayout(confirmButton, cancelButton);

        dialogLayout.add(dialogHeading, currentPassword, newPassword, dialogButtonLayout);

        dialog.add(dialogLayout);
        dialog.open();
    }

    /**
     * makes request to api to patch the username of the currently logged-in user
     *
     * @param name new username
     * @return true if request worked
     */
    private boolean makeChangeUsernameRequest(String name) {
        HttpResponse<UserObj> userObj = Unirest.patch(BASE_URL + "/users/" + SessionStorage.get().getUser().getId())
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .contentType("application/json")
                .body(new UserNameObj(name))
                .asObject(UserObj.class);

        Error error = userObj.mapError(Error.class);

        if (null == error) {
            SessionStorage.updateUserFromDB();
            UI.getCurrent().getPage().reload();
            return true;
        } else {
            Notification.show(error.getMessage());
            return false;
        }
    }

    /**
     * makes request to api to patch the email of the currently logged-in user
     *
     * @param email new email
     * @return true if request worked
     */
    private boolean makeChangeEmailRequest(String email, String currentPassword) {
        HttpResponse<UserEmailObj> userObj = Unirest.patch(BASE_URL + "/users/" + SessionStorage.get().getUser().getId())
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .contentType("application/json")
                .body(new UserEmailObj(email, currentPassword))
                .asObject(UserEmailObj.class);

        Error error = userObj.mapError(Error.class);

        if (null == error) {
            SessionStorage.set(null);
            Auth.redirectIfNotValid();
            return true;
        } else {
            Notification.show(error.getMessage());
            return false;
        }
    }

    /**
     * makes request to api to patch the password of the currently logged-in user
     *
     * @param password new password
     * @return true if request worked
     */
    private boolean makeChangePasswordRequest(String password, String currentPassword) {
        HttpResponse<UserPasswordObj> userObj = Unirest.patch(BASE_URL + "/users/" + SessionStorage.get().getUser().getId())
                .header("Authorization", "Bearer " + SessionStorage.get().getTokens().getAccess().getToken())
                .contentType("application/json")
                .body(new UserPasswordObj(password, currentPassword))
                .asObject(UserPasswordObj.class);

        Error error = userObj.mapError(Error.class);

        if (null == error) {
            SessionStorage.set(null);
            Auth.redirectIfNotValid();
            return true;
        } else {
            Notification.show(error.getMessage());
            return false;
        }
    }
}
