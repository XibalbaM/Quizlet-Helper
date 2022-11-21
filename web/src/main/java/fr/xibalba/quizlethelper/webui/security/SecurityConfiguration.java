package fr.xibalba.quizlethelper.webui.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import fr.xibalba.quizlethelper.webui.views.login.LoginView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurity {

    public static final String LOGOUT_URL = "/";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/images/*.png").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/register").permitAll();
        super.configure(http);
        setLoginView(http, LoginView.class, LOGOUT_URL);
    }
}
