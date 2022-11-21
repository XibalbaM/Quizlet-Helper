package fr.xibalba.quizlethelper.webui.views.register;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import fr.xibalba.quizlethelper.data.entity.User;
import fr.xibalba.quizlethelper.data.service.UserService;
import fr.xibalba.quizlethelper.webui.SpringContext;
import fr.xibalba.quizlethelper.webui.security.AuthenticatedUser;
import fr.xibalba.quizlethelper.webui.views.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Route(value = "register", layout = MainLayout.class)
@PageTitle("Créer un compte")
@AnonymousAllowed
@Component
public class RegisterView extends VerticalLayout implements BeforeEnterObserver {

    public RegisterView() throws Exception {

        RegisterForm registerForm = new RegisterForm();

        setSizeFull();

        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        setSpacing(false);

        add(registerForm);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {

        if (SpringContext.getBean(AuthenticatedUser.class).get().isPresent()) {

            UI.getCurrent().navigate("");
        }
    }

    @Component
    private static class RegisterForm extends VerticalLayout {

        private final TextField username;
        private final PasswordField password;
        private final PasswordField confirmPassword;
        private final Button register;

        @Autowired
        private UserService service;

        public RegisterForm() {

            setAlignItems(Alignment.CENTER);
            setJustifyContentMode(JustifyContentMode.CENTER);

            H1 title = new H1("Créer un compte");

            username = new TextField("Nom d'utilisateur");
            username.setRequired(true);
            username.addValueChangeListener(event -> onValueChange());

            password = new PasswordField("Mot de passe");
            password.setRequired(true);
            password.addValueChangeListener(event -> onValueChange());

            confirmPassword = new PasswordField("Répéter le mot de passe");
            confirmPassword.setRequired(true);
            confirmPassword.addValueChangeListener(event -> onValueChange());

            register = new Button("Créer le compte");
            register.setEnabled(false);
            register.addClickListener(event -> {

                User user = service.create(username.getValue(), password.getValue());

                UsernamePasswordAuthenticationToken authReq
                        = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
                Authentication auth = SpringContext.getBean(AuthenticationManager.class).authenticate(authReq);

                SecurityContext sc = SecurityContextHolder.getContext();
                sc.setAuthentication(auth);
                VaadinSession.getCurrent().setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);


            });

            add(title, username, password, confirmPassword, register);
        }

        private void onValueChange() {

            boolean enabled = true;

            if (username.getValue().length() < 3 || username.getValue().length() > 30) {

                enabled = false;
                username.setErrorMessage("Please provide an username between 3 and 30 characters");
                username.setInvalid(true);
            } else if(service.get(username.getValue()) != null) {

                enabled = false;
                username.setErrorMessage("This username is already taken");
                username.setInvalid(true);
            } else {

                username.setInvalid(false);
            }

            if (password.getValue().length() < 8 && password.getValue().length() > 0) {

                enabled = false;
                password.setErrorMessage("Please provide a password of at least 8 characters");
                password.setInvalid(true);
            } else {

                password.setInvalid(false);
            }

            if (!password.getValue().equals(confirmPassword.getValue())) {

                enabled = false;
                confirmPassword.setErrorMessage("Passwords must match");
                confirmPassword.setInvalid(true);
            } else {

                confirmPassword.setInvalid(false);
            }

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {

                enabled = false;
            }

            register.setEnabled(enabled);
        }
    }
}