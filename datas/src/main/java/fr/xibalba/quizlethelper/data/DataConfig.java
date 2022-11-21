package fr.xibalba.quizlethelper.data;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = "fr.xibalba.quizlethelper.data.entity")
@EnableJpaRepositories(basePackages = "fr.xibalba.quizlethelper.data.repository")
@Configuration
public class DataConfig {
}
