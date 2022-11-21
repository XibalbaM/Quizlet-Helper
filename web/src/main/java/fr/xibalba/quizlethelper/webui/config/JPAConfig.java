package fr.xibalba.quizlethelper.webui.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class JPAConfig {

    @Bean
    public DataSource getDataSource() {

        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5432/Quizlet-Helper")
                .username("Quizlet")
                .password("Quizlet").build();
    }
}