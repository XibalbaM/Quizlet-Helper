package fr.xibalba.quizlethelper.webui.security;

import fr.xibalba.quizlethelper.data.entity.User;
import fr.xibalba.quizlethelper.data.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.BeanIds;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class CustomWebSecurityConfigurer {

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    AuthenticationManager authenticationManager(@Autowired UserService service) {
        return authentication -> {
            String name = authentication.getName();
            String password = authentication.getCredentials().toString();

            User byUsername = service.get(name);
            if (byUsername != null && byUsername.getPassword().equals(password)) {
                return new UsernamePasswordAuthenticationToken(
                        name, password, authentication.getAuthorities());
            } else {
                throw new UsernameNotFoundException("Invalid username/password.");
            }
        };
    }

    AuthenticationManagerResolver<HttpServletRequest> resolver(@Autowired UserService service) {
        return request -> authenticationManager(service);
    }
}