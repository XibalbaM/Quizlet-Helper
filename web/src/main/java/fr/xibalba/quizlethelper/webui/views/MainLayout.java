package fr.xibalba.quizlethelper.webui.views;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility;
import fr.xibalba.quizlethelper.webui.SpringContext;
import fr.xibalba.quizlethelper.webui.components.appnav.AppNav;
import fr.xibalba.quizlethelper.webui.components.appnav.AppNavItem;
import fr.xibalba.quizlethelper.data.entity.PatchNote;
import fr.xibalba.quizlethelper.data.entity.User;
import fr.xibalba.quizlethelper.data.service.PatchNoteService;
import fr.xibalba.quizlethelper.data.service.UserService;
import fr.xibalba.quizlethelper.webui.security.AuthenticatedUser;
import fr.xibalba.quizlethelper.webui.views.admin.admindashboard.AdminDashboardView;
import fr.xibalba.quizlethelper.webui.views.home.HomeView;
import fr.xibalba.quizlethelper.webui.views.admin.patchnotecreator.PatchNoteCreatorView;
import fr.xibalba.quizlethelper.webui.views.admin.projects.ProjectsView;
import fr.xibalba.quizlethelper.webui.views.admin.users.UsersView;
import fr.xibalba.quizlethelper.webui.views.projects.projectList.ProjectListView;

import java.util.List;
import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H2 viewTitle;

    private AuthenticatedUser authenticatedUser;
    private AccessAnnotationChecker accessChecker;

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;

        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();

        addPatchNote();
    }

    private void addPatchNote() {

        if (authenticatedUser.get().isPresent()) {

            List<PatchNote> patchNotes = SpringContext.getBean(PatchNoteService.class)
                    .getNewerThan(authenticatedUser.get().get().getLastSeenChangelog());
            if (patchNotes.size() > 0) {
                Dialog dialog = new Dialog();
                dialog.setMaxHeight("80%");
                dialog.setMaxWidth("50%");
                dialog.setCloseOnEsc(true);
                dialog.setCloseOnOutsideClick(true);
                dialog.setHeaderTitle("Changelogs");
                Scroller content = new Scroller();
                content.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
                content.setWidthFull();
                VerticalLayout layout = new VerticalLayout();
                layout.setWidthFull();
                layout.setAlignItems(FlexComponent.Alignment.START);
                layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.START);

                for (PatchNote patchNote : patchNotes) {
                    VerticalLayout patchNoteLayout = new VerticalLayout();
                    patchNoteLayout.setWidthFull();
                    patchNoteLayout.setAlignItems(FlexComponent.Alignment.START);
                    patchNoteLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.START);
                    patchNoteLayout.addClassNames("bg-contrast-10", "rounded-l");

                    H2 title = new H2(patchNote.getTitle());
                    title.getStyle().set("margin", "0");

                    Html description = new Html("<h1>Salut</h1>");

                    patchNoteLayout.add(title, description);
                    layout.add(patchNoteLayout);
                }

                content.setContent(layout);

                VerticalLayout dialogLayout = new VerticalLayout();
                dialogLayout.setWidthFull();
                dialogLayout.setAlignItems(FlexComponent.Alignment.START);
                dialogLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.START);
                dialogLayout.setPadding(true);
                dialogLayout.add(content);
                Button button = new Button("Fermer");
                button.addClickListener(e -> {
                    dialog.close();
                    SpringContext.getBean(UserService.class).update(authenticatedUser.get().get().setLastSeenChangelog(patchNotes.get(0).getId()));
                });
                dialogLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.END, button);
                dialogLayout.add(button);

                dialog.add(dialogLayout);
                dialog.open();
            }
        }
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("QuizletHelper");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private AppNav createNavigation() {

        AppNav nav = new AppNav();

        if (accessChecker.hasAccess(HomeView.class)) {
            nav.addItem(new AppNavItem("Home", HomeView.class, "la la-home"));

        }
        if (accessChecker.hasAccess(ProjectListView.class)) {
            nav.addItem(new AppNavItem("Projets", ProjectListView.class, "la la-chart-line"));
        }
        if (accessChecker.hasAccess(AdminDashboardView.class)) {
            nav.addItem(new AppNavItem("Panel admin", AdminDashboardView.class, "la la-chart-area"));

        }
        if (accessChecker.hasAccess(PatchNoteCreatorView.class)) {
            nav.addItem(new AppNavItem("Créateur de patch note", PatchNoteCreatorView.class, "la la-edit"));

        }
        if (accessChecker.hasAccess(UsersView.class)) {
            nav.addItem(new AppNavItem("Utilisateurs", UsersView.class, "la la-users-cog"));

        }
        if (accessChecker.hasAccess(ProjectsView.class)) {
            nav.addItem(new AppNavItem("Projets", ProjectsView.class, "la la-cogs"));
        }

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();

            Avatar avatar = new Avatar(user.getUsername());
            avatar.setThemeName("xsmall");
            avatar.getElement().setAttribute("tabindex", "-1");

            MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");

            MenuItem userName = userMenu.addItem("");
            Div div = new Div();
            div.add(avatar);
            div.add(user.getUsername());
            div.add(new Icon("lumo", "dropdown"));
            div.getElement().getStyle().set("display", "flex");
            div.getElement().getStyle().set("align-items", "center");
            div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
            userName.add(div);
            userName.getSubMenu().addItem("Se déconnecter", e -> {
                authenticatedUser.logout();
            });

            layout.add(userMenu);
        } else {
            Anchor loginLink = new Anchor("login", "Se connecter");

            Anchor registerLink = new Anchor("register", "Créer un compte");

            layout.add(loginLink, registerLink);
        }

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
