package fr.xibalba.quizlethelper.webui.views.admin.patchnotecreator;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.richtexteditor.RichTextEditor;
import com.vaadin.flow.component.richtexteditor.RichTextEditorVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.xibalba.quizlethelper.webui.SpringContext;
import fr.xibalba.quizlethelper.data.service.PatchNoteService;
import fr.xibalba.quizlethelper.webui.views.MainLayout;
import javax.annotation.security.RolesAllowed;

@PageTitle("Création de patch note")
@Route(value = "patchNoteCreator", layout = MainLayout.class)
@RolesAllowed("ROLE_ADMIN")
public class PatchNoteCreatorView extends VerticalLayout {

    private RichTextEditor editor;

    public PatchNoteCreatorView() {
        addClassNames("flex", "flex-grow", "h-full");

        editor = new RichTextEditor();
        editor.addClassNames("border-r", "border-contrast-10", "flex-grow");
        editor.addThemeVariants(RichTextEditorVariant.LUMO_NO_BORDER);

        Button button = new Button("Valider");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.addClickListener(e -> {
            Dialog dialog = new Dialog();
            dialog.setHeaderTitle("Création d'un patch note");

            VerticalLayout layout = new VerticalLayout();

            TextField title = new TextField();
            layout.add(new Label("Choisissez un titre pour votre patch note: "), title);

            Button confirm = new Button("Créer");
            confirm.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            confirm.addClickListener(e2 -> {
                PatchNoteService service = SpringContext.getBean(PatchNoteService.class);
                service.create(title.getValue(), editor.getHtmlValue());
                dialog.close();
            });

            Button cancel = new Button("Annuler");
            cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            cancel.addClickListener(e2 -> dialog.close());

            HorizontalLayout div = new HorizontalLayout(cancel, confirm);
            div.setAlignSelf(Alignment.START, cancel);
            div.setAlignSelf(Alignment.END, confirm);
            layout.add(div);

            dialog.add(layout);

            dialog.open();
        });

        add(editor, button);
    }
}
