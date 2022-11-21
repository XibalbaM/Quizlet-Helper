package fr.xibalba.quizlethelper.webui.views.admin.projects;

import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.xibalba.quizlethelper.data.entity.Project;
import fr.xibalba.quizlethelper.data.entity.User;
import fr.xibalba.quizlethelper.data.service.ProjectService;
import fr.xibalba.quizlethelper.data.service.UserService;
import fr.xibalba.quizlethelper.webui.SpringContext;
import fr.xibalba.quizlethelper.webui.views.MainLayout;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@PageTitle("Projects")
@Route(value = "adminDashboard/projects", layout = MainLayout.class)
@RolesAllowed("ROLE_ADMIN")
public class ProjectsView extends Div {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;

    private GridPro<Project> grid;
    private GridListDataView<Project> gridListDataView;

    private Grid.Column<Project> idColumn;
    private Grid.Column<Project> nameColumn;
    private Grid.Column<Project> ownerColumn;
    private Grid.Column<Project> usersColumn;
    private Grid.Column<Project> cardsColumn;
    private Grid.Column<Project> categoriesColumn;
    private Grid.Column<Project> deleteColumn;

    public ProjectsView() {
        addClassName("projects-view");
        setSizeFull();
        createGrid();
        add(grid);
    }

    private void createGrid() {
        createGridComponent();
        addColumnsToGrid();
        addFiltersToGrid();
    }

    private void createGridComponent() {
        grid = new GridPro<>();
        grid.setSelectionMode(SelectionMode.NONE);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);
        grid.setHeight("100%");

        List<Project> clients = projectService.findAll();
        gridListDataView = grid.setItems(clients);
    }

    private void addColumnsToGrid() {
        createIdColumn();
        createNameColumn();
        createOwnerColumn();
        createUsersColumn();
        createCardsColumn();
        createCategoriesColumn();
        createDeleteColumn();
    }

    private void createIdColumn() {
        idColumn = grid.addColumn(new ComponentRenderer<>(project -> new Span(project.getId().toString())))
                .setComparator(Project::getId).setHeader("Id");
    }

    private void createNameColumn() {
        nameColumn = grid.addColumn(new ComponentRenderer<>(project -> {
            HorizontalLayout hl = new HorizontalLayout();
            hl.setAlignItems(Alignment.CENTER);
            Avatar img = new Avatar(project.getName());
            TextField span = new TextField();
            span.setClassName("name");
            span.setValue(project.getName());
            span.setValueChangeMode(ValueChangeMode.EAGER);
            span.addValueChangeListener(event -> {
                if (StringUtils.isNotBlank(event.getValue())) {
                    project.setName(event.getValue());
                    SpringContext.getBean(ProjectService.class).update(project);
                    img.setName(event.getValue());
                }
            });
            hl.add(img, span);
            return hl;
        })).setComparator(Project::getName).setHeader("Nom");
    }

    private void createOwnerColumn() {
        ownerColumn = grid.addColumn(new ComponentRenderer<>(project -> {
            HorizontalLayout hl = new HorizontalLayout();
            hl.setAlignItems(Alignment.CENTER);
            Avatar img = new Avatar(project.getOwner().getUsername());
            TextField span = new TextField();
            span.setClassName("name");
            span.setValue(project.getOwner().getUsername());
            span.setValueChangeMode(ValueChangeMode.EAGER);
            span.addValueChangeListener(event -> {
                User user = userService.get(event.getValue());
                if (StringUtils.isNotBlank(event.getValue()) && !event.getValue().equals(project.getOwner().getUsername()) && user != null) {
                    project.setOwner(user);
                    SpringContext.getBean(ProjectService.class).update(project);
                    img.setName(event.getValue());
                }
            });
            hl.add(img, span);
            return hl;
        })).setComparator(project -> project.getOwner().getUsername()).setHeader("Propriétaire");
    }

    private void createUsersColumn() {
        usersColumn = grid.addColumn(new ComponentRenderer<>(project -> {
            Span span = new Span();
            span.setClassName("name");
            span.setText(project.getUsers().size() + "");
            return span;
        })).setComparator(project -> project.getUsers().size()).setHeader("Membres");
    }

    private void createCardsColumn() {
        cardsColumn = grid.addColumn(new ComponentRenderer<>(project -> {
            Span span = new Span();
            span.setClassName("name");
            span.setText(project.getCards().size() + "");
            return span;
        })).setComparator(project -> project.getCards().size()).setHeader("Cartes");
    }

    private void createCategoriesColumn() {
        categoriesColumn = grid.addColumn(new ComponentRenderer<>(project -> {
            Span span = new Span();
            span.setClassName("name");
            span.setText(project.getCategories().size() + "");
            return span;
        })).setComparator(project -> project.getCategories().size()).setHeader("Catégories");
    }

    private void createDeleteColumn() {
        deleteColumn = grid.addComponentColumn(project -> {
            Button button = new Button("Supprimer");
            button.addClickListener(event -> {
                ConfirmDialog dialog = new ConfirmDialog("Supprimer le projet", "Êtes-vous sûr de vouloir supprimer le projet " + project.getName() + " ?", "Supprimer", event1 -> {
                    SpringContext.getBean(ProjectService.class).delete(project.getId());
                    gridListDataView.removeItem(project);
                }, "Annuler", event12 -> {});
                dialog.open();
            });
            return button;
        }).setHeader("Supprimer");
    }

    private void addFiltersToGrid() {
        HeaderRow filterRow = grid.appendHeaderRow();

        NumberField idFilter = new NumberField();
        idFilter.setPlaceholder("Filtre");
        idFilter.setClearButtonVisible(true);
        idFilter.setWidth("100%");
        idFilter.setValueChangeMode(ValueChangeMode.EAGER);
        idFilter.addValueChangeListener(event -> gridListDataView
                .addFilter(project -> StringUtils.containsIgnoreCase(project.getId().toString(), String.valueOf(idFilter.getValue().intValue()))));
        filterRow.getCell(idColumn).setComponent(idFilter);

        TextField clientFilter = new TextField();
        clientFilter.setPlaceholder("Filtre");
        clientFilter.setClearButtonVisible(true);
        clientFilter.setWidth("100%");
        clientFilter.setValueChangeMode(ValueChangeMode.EAGER);
        clientFilter.addValueChangeListener(event -> gridListDataView
                .addFilter(project -> StringUtils.containsIgnoreCase(project.getName(), clientFilter.getValue())));
        filterRow.getCell(nameColumn).setComponent(clientFilter);

        TextField ownerFilter = new TextField();
        ownerFilter.setPlaceholder("Filtre");
        ownerFilter.setClearButtonVisible(true);
        ownerFilter.setWidth("100%");
        ownerFilter.setValueChangeMode(ValueChangeMode.EAGER);
        ownerFilter.addValueChangeListener(event -> gridListDataView
                .addFilter(project -> StringUtils.containsIgnoreCase(project.getOwner().getUsername(), ownerFilter.getValue())));
        filterRow.getCell(ownerColumn).setComponent(ownerFilter);

        TextField usersFilter = new TextField();
        usersFilter.setPlaceholder("Filtre");
        usersFilter.setClearButtonVisible(true);
        usersFilter.setWidth("100%");
        usersFilter.setValueChangeMode(ValueChangeMode.EAGER);
        usersFilter.addValueChangeListener(event -> gridListDataView
                .addFilter(project -> StringUtils.containsIgnoreCase(project.getUsers().size() + "", usersFilter.getValue())));
        filterRow.getCell(usersColumn).setComponent(usersFilter);

        TextField cardsFilter = new TextField();
        cardsFilter.setPlaceholder("Filtre");
        cardsFilter.setClearButtonVisible(true);
        cardsFilter.setWidth("100%");
        cardsFilter.setValueChangeMode(ValueChangeMode.EAGER);
        cardsFilter.addValueChangeListener(event -> gridListDataView
                .addFilter(project -> StringUtils.containsIgnoreCase(project.getCards().size() + "", cardsFilter.getValue())));
        filterRow.getCell(cardsColumn).setComponent(cardsFilter);

        TextField categoriesFilter = new TextField();
        categoriesFilter.setPlaceholder("Filtre");
        categoriesFilter.setClearButtonVisible(true);
        categoriesFilter.setWidth("100%");
        categoriesFilter.setValueChangeMode(ValueChangeMode.EAGER);
        categoriesFilter.addValueChangeListener(event -> gridListDataView
                .addFilter(project -> StringUtils.containsIgnoreCase(project.getCategories().size() + "", categoriesFilter.getValue())));
        filterRow.getCell(categoriesColumn).setComponent(categoriesFilter);
    }
}