package fr.xibalba.quizlethelper.data.repository;

import fr.xibalba.quizlethelper.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);
}