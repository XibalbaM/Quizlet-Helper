package fr.xibalba.quizlethelper.data;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

@EntityScan(basePackages = "fr.xibalba.quizlethelper.data.entity")
@EnableJpaRepositories(basePackages = "fr.xibalba.quizlethelper.data.repository")
@Configuration
public class DataConfig {

    @Bean
    public DataSource getDataSource() {

        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5432/Quizlet-Helper")
                .username("Quizlet")
                .password("Quizlet").build();
    }
}