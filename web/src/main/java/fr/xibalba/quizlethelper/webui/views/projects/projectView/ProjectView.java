package fr.xibalba.quizlethelper.webui.views.projects.projectView;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.xibalba.quizlethelper.webui.SpringContext;
import fr.xibalba.quizlethelper.data.entity.Card;
import fr.xibalba.quizlethelper.data.entity.Category;
import fr.xibalba.quizlethelper.data.entity.Project;
import fr.xibalba.quizlethelper.data.entity.User;
import fr.xibalba.quizlethelper.data.service.CardService;
import fr.xibalba.quizlethelper.data.service.ProjectService;
import fr.xibalba.quizlethelper.webui.security.AuthenticatedUser;
import fr.xibalba.quizlethelper.webui.views.MainLayout;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.stream.Collectors;

@PageTitle("Projet")
@Route(value = "projects/view", layout = MainLayout.class)
@RolesAllowed("ROLE_USER")
public class ProjectView extends VerticalLayout implements HasUrlParameter<String> {

    private Project project;

    public void init() {

        TreeGrid<Card> tree = new TreeGrid<>();
        TreeData<Card> treeData = new TreeData<>();
        for (Card card : project.getCards()) {
            treeData.addItem(card.getParent(), card);
        }
        tree.setTreeData(treeData);
        tree.addHierarchyColumn(Card::getName).setHeader("Nom");
        tree.addComponentColumn(card -> {
            MultiSelectComboBox<String> comboBox = new MultiSelectComboBox<>();
            comboBox.setItems(project.getCategories().stream().map(Category::getName).collect(Collectors.toList()));
            comboBox.setValue(card.getCategories().stream().map(Category::getName).collect(Collectors.toList()));
            comboBox.addValueChangeListener(event -> {
                System.out.println(card);
                card.setCategories(event.getValue().stream().map(name -> project.getCategories().stream().filter(category -> category.getName().equals(name)).findFirst().orElse(null)).collect(Collectors.toList()));
                System.out.println(card);
                SpringContext.getBean(CardService.class).update(card);
                System.out.println(card);
            });
            return comboBox;
        }).setHeader("Catégories");

        add(new H1("Cartes"), tree);
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        try {
            this.project = SpringContext.getBean(ProjectService.class).get(Integer.parseInt(parameter)).orElseThrow();
            List<User> users = project.getUsers();
            if (!users.stream().map(User::getId).toList().contains(SpringContext.getBean(AuthenticatedUser.class).get().orElseThrow().getId())) {
                System.out.println(SpringContext.getBean(AuthenticatedUser.class).get().get());
                System.out.println(users);
                UI.getCurrent().navigate("projects/list");
            }
            init();
        } catch (Exception e) {
            UI.getCurrent().navigate("projects/list");
            e.printStackTrace();
        }
    }
}