package fr.xibalba.quizlethelper.webui.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import fr.xibalba.quizlethelper.data.entity.User;
import fr.xibalba.quizlethelper.data.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticatedUser {

    private final UserService userService;

    @Autowired
    public AuthenticatedUser(UserService userService) {
        this.userService = userService;
    }

    private Optional<Authentication> getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        return Optional.ofNullable(context.getAuthentication())
                .filter(authentication -> !(authentication instanceof AnonymousAuthenticationToken));
    }

    public Optional<User> get() {
        return getAuthentication().map(authentication -> userService.get(authentication.getName()));
    }

    public void logout() {
        UI.getCurrent().getPage().setLocation(SecurityConfiguration.LOGOUT_URL);
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null);
    }

}
