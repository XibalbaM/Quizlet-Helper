package fr.xibalba.quizlethelper.data.repository;

import fr.xibalba.quizlethelper.data.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Integer> {
}