package fr.xibalba.quizlethelper.data.repository;

import fr.xibalba.quizlethelper.data.entity.PatchNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PatchNoteRepository extends JpaRepository<PatchNote, Integer> {

    @Query("SELECT p FROM PatchNote p WHERE p.id > ?1")
    PatchNote[] findByIdGreaterThan(int id);

    @Query("SELECT p.id FROM PatchNote p ORDER BY p.id DESC")
    Optional<Integer> findFirstByOrderByIdDesc();
}