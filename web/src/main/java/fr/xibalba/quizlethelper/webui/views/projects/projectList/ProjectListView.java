package fr.xibalba.quizlethelper.webui.views.projects.projectList;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import fr.xibalba.quizlethelper.data.entity.Project;
import fr.xibalba.quizlethelper.data.entity.User;
import fr.xibalba.quizlethelper.data.service.ProjectService;
import fr.xibalba.quizlethelper.data.service.UserService;
import fr.xibalba.quizlethelper.webui.SpringContext;
import fr.xibalba.quizlethelper.webui.security.AuthenticatedUser;
import fr.xibalba.quizlethelper.webui.views.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@PageTitle("Projets")
@Route(value = "projects/list", layout = MainLayout.class)
@RolesAllowed("ROLE_USER")
public class ProjectListView extends VerticalLayout {

    public ProjectListView(@Autowired AuthenticatedUser authenticatedUser) {
        HorizontalLayout titleLayout = new HorizontalLayout();
        titleLayout.setSizeFull();
        titleLayout.setWidthFull();
        titleLayout.add(new H1("Projets"));
        Button addProjectButton = new Button("Créer un projet");
        addProjectButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addProjectButton.addClickListener(e -> {
            authenticatedUser.get().ifPresent(user -> {
                ConfirmDialog dialog = new ConfirmDialog("Créer un projet", "Voulez-vous créer un projet ?", "Créer", e1 -> {
                    getUI().ifPresent(ui -> ui.navigate("projects/view/" + SpringContext.getBean(ProjectService.class).create("Nouveau projet",
                            user.getId(), user.getId()).getId()));
                }, "Annuler", e1 -> {});
                dialog.open();
            });
        });
        titleLayout.setAlignSelf(Alignment.END, addProjectButton);
        titleLayout.add(addProjectButton);
        Button joinProjectButton = new Button("Rejoindre un projet");
        joinProjectButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        joinProjectButton.addClickListener(e -> {
            authenticatedUser.get().ifPresent(user -> {
                ConfirmDialog dialog = new ConfirmDialog("Rejoindre un projet", "Entrez le code du projet à rejoindre", "Rejoindre", e1 -> {
                    SpringContext.getBean(ProjectService.class).getByCode(e1.getSource().getChildren().filter(NumberField.class::isInstance).findFirst().map(NumberField.class::cast).map(NumberField::getValue).map(Double::intValue).orElse(0)).ifPresent(project -> {
                        System.out.println(project);
                        if (project.getUsers().stream().map(User::getId).noneMatch(id -> id.equals(user.getId()))) {
                            project.getUsers().add(user);
                            user.getProjects().add(project);
                            SpringContext.getBean(ProjectService.class).update(project);
                            SpringContext.getBean(UserService.class).update(user);
                        }
                        getUI().ifPresent(ui -> ui.navigate("projects/view/" + project.getId()));
                    });
                }, "Annuler", e1 -> {});
                NumberField codeField = new NumberField("Code du projet");
                codeField.setMin(0);
                codeField.setMax(999999);
                codeField.setStep(1);
                codeField.setClearButtonVisible(true);
                dialog.add(codeField);
                dialog.open();
            });
        });
        titleLayout.setAlignSelf(Alignment.END, joinProjectButton);
        titleLayout.add(joinProjectButton);

        add(titleLayout);

        User user = authenticatedUser.get().get();
        List<Project> projects = user.getProjects();

        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(Alignment.CENTER);
        layout.setJustifyContentMode(JustifyContentMode.CENTER);
        layout.setSizeFull();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSizeFull();
        for (int i = 0; i < projects.size(); i++) {

            horizontalLayout.add(new ProjectCard(projects.get(i)));

            if (i % 3 == 2) {
                layout.add(horizontalLayout);
                horizontalLayout = new HorizontalLayout();
                horizontalLayout.setSizeFull();
            }
        }
        layout.add(horizontalLayout);
        add(layout);
    }

    private static class ProjectCard extends Button {

        public ProjectCard(Project project) {

            addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            addClassNames(LumoUtility.Background.CONTRAST_10, LumoUtility.Border.ALL, LumoUtility.BorderRadius.LARGE,
                    LumoUtility.BoxShadow.SMALL);
            setText(project.getName() + " par " + project.getOwner().getUsername() + " (id: " + project.getId() + ")");
            addClickListener(event -> {
                UI.getCurrent().navigate("/projects/view/" + project.getId());
            });
        }
    }
}