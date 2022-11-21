package fr.xibalba.quizlethelper.data.repository;

import fr.xibalba.quizlethelper.data.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}