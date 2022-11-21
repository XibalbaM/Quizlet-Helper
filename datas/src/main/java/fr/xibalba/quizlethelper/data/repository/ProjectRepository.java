package fr.xibalba.quizlethelper.data.repository;

import fr.xibalba.quizlethelper.data.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    //Get project from code
    Optional<Project> findByCode(int code);
}