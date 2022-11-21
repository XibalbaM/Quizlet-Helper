package fr.xibalba.quizlethelper.data.service;

import fr.xibalba.quizlethelper.data.entity.Card;
import fr.xibalba.quizlethelper.data.entity.Category;
import fr.xibalba.quizlethelper.data.entity.Project;
import fr.xibalba.quizlethelper.data.repository.CardRepository;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class CardService {

    private final CardRepository repository;

    @Autowired
    public CardService(CardRepository repository) {
        this.repository = repository;
    }

    public Optional<Card> get(int id) {
        return repository.findById(id);
    }

    public Card update(Card entity) {
        return repository.save(entity);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Page<Card> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public Card create(String path, String face1, String face2, Project project, @Nullable Card parent, Category... categories) {
        Card card = new Card();
        card.setName(path);
        card.setFace1(face1);
        card.setFace2(face2);
        card.setProject(project);
        card.setParent(parent);
        card.setCategories(Arrays.stream(categories).toList());
        return repository.save(card);
    }
}
