package fr.xibalba.quizlethelper.webui.views.admin.users;

import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
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
import fr.xibalba.quizlethelper.data.entity.User;
import fr.xibalba.quizlethelper.data.service.UserService;
import fr.xibalba.quizlethelper.webui.SpringContext;
import fr.xibalba.quizlethelper.webui.views.MainLayout;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.RolesAllowed;
import java.util.Arrays;
import java.util.List;

@PageTitle("Users")
@Route(value = "adminDashboard/users", layout = MainLayout.class)
@RolesAllowed("ROLE_ADMIN")
public class UsersView extends Div {

    private final UserService userService;

    private GridPro<User> grid;
    private GridListDataView<User> gridListDataView;

    private Grid.Column<User> idColumn;
    private Grid.Column<User> nameColumn;
    private Grid.Column<User> isAdminColumn;
    private Grid.Column<User> projectsColumn;
    private Grid.Column<User> cardsCreatedColumn;

    public UsersView(@Autowired UserService userService) {
        this.userService = userService;
        addClassName("users-view");
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

        List<User> clients = userService.findAll();
        gridListDataView = grid.setItems(clients);
    }

    private void addColumnsToGrid() {
        createIdColumn();
        createClientColumn();
        createAdminColumn();
        createProjectsColumn();
        createCardsColumn();
    }

    private void createIdColumn() {
        idColumn = grid.addColumn(new ComponentRenderer<>(client -> new Span(client.getId().toString())))
                .setComparator(User::getId).setHeader("Id");
    }

    private void createClientColumn() {
        nameColumn = grid.addColumn(new ComponentRenderer<>(client -> {
            HorizontalLayout hl = new HorizontalLayout();
            hl.setAlignItems(Alignment.CENTER);
            Avatar img = new Avatar(client.getUsername());
            Span span = new Span();
            span.setClassName("name");
            span.setText(client.getUsername());
            hl.add(img, span);
            return hl;
        })).setComparator(User::getUsername).setHeader("Utilisateur");
    }

    private void createAdminColumn() {
        isAdminColumn = grid.addColumn(new ComponentRenderer<>(client -> {

            Checkbox checkbox = new Checkbox();
            checkbox.setValue(client.isAdmin());
            checkbox.addValueChangeListener(event -> {
                client.setAdmin(event.getValue());
                SpringContext.getBean(UserService.class).update(client);
            });

            return checkbox;
        })).setComparator(User::isAdmin).setHeader("Admin");
    }

    private void createProjectsColumn() {
        projectsColumn = grid.addColumn(new ComponentRenderer<>(client -> {

            Span span = new Span();
            span.setClassName("name");
            span.setText(client.getProjects().size() + "");
            return span;
        })).setComparator(user -> user.getProjects().size()).setHeader("Projets");
    }

    private void createCardsColumn() {
        cardsCreatedColumn = grid.addColumn(new ComponentRenderer<>(client -> {

            Span span = new Span();
            span.setClassName("name");
            span.setText(client.getCardsCreated() + "");
            return span;
        })).setComparator(user -> user.getCardsCreated()).setHeader("Cartes créées");
    }

    private void addFiltersToGrid() {
        HeaderRow filterRow = grid.appendHeaderRow();

        NumberField idFilter = new NumberField();
        idFilter.setPlaceholder("Filtre");
        idFilter.setClearButtonVisible(true);
        idFilter.setWidth("100%");
        idFilter.setValueChangeMode(ValueChangeMode.EAGER);
        idFilter.addValueChangeListener(event -> gridListDataView
                .addFilter(client -> StringUtils.containsIgnoreCase(client.getId().toString(), String.valueOf(idFilter.getValue().intValue()))));
        filterRow.getCell(idColumn).setComponent(idFilter);

        TextField clientFilter = new TextField();
        clientFilter.setPlaceholder("Filtre");
        clientFilter.setClearButtonVisible(true);
        clientFilter.setWidth("100%");
        clientFilter.setValueChangeMode(ValueChangeMode.EAGER);
        clientFilter.addValueChangeListener(event -> gridListDataView
                .addFilter(client -> StringUtils.containsIgnoreCase(client.getUsername(), clientFilter.getValue())));
        filterRow.getCell(nameColumn).setComponent(clientFilter);

        ComboBox<String> isAdminFilter = new ComboBox<>();
        isAdminFilter.setItems(Arrays.asList("Oui", "Non"));
        isAdminFilter.setPlaceholder("Filtre");
        isAdminFilter.setClearButtonVisible(true);
        isAdminFilter.setWidth("100%");
        isAdminFilter.addValueChangeListener(event -> gridListDataView
                .addFilter(client -> {
                    if (event.getValue() == null) {
                        return true;
                    }
                    if (event.getValue().equals("Oui")) {
                        return client.isAdmin();
                    } else {
                        return !client.isAdmin();
                    }
                }));
        filterRow.getCell(isAdminColumn).setComponent(isAdminFilter);

        NumberField projectsFilter = new NumberField();
        projectsFilter.setPlaceholder("Filtre");
        projectsFilter.setClearButtonVisible(true);
        projectsFilter.setWidth("100%");
        projectsFilter.setValueChangeMode(ValueChangeMode.EAGER);
        projectsFilter.addValueChangeListener(event -> gridListDataView
                .addFilter(client -> StringUtils.containsIgnoreCase(client.getProjects().size() + "", String.valueOf(projectsFilter.getValue().intValue()))));
        filterRow.getCell(projectsColumn).setComponent(projectsFilter);

        NumberField cardsCreatedFilter = new NumberField();
        cardsCreatedFilter.setPlaceholder("Filtre");
        cardsCreatedFilter.setClearButtonVisible(true);
        cardsCreatedFilter.setWidth("100%");
        cardsCreatedFilter.setValueChangeMode(ValueChangeMode.EAGER);
        cardsCreatedFilter.addValueChangeListener(event -> gridListDataView
                .addFilter(client -> StringUtils.containsIgnoreCase(client.getCardsCreated() + "", String.valueOf(cardsCreatedFilter.getValue().intValue()))));
        filterRow.getCell(cardsCreatedColumn).setComponent(cardsCreatedFilter);
    }
}