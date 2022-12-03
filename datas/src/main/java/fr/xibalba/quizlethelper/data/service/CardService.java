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
import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    private final CardRepository repository;
    private final ProjectService projectService;

    @Autowired
    public CardService(CardRepository repository, ProjectService projectService) {
        this.repository = repository;
        this.projectService = projectService;
    }

    public Optional<Card> get(int id) {
        return repository.findById(id);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Card update(Card card) {

        return repository.save(card);
    }

    public Card setCategories(Integer id, List<Category> categories) {

        System.out.println("id =" + id);
        System.out.println("Cards are" + repository.findAll());

        Card card = repository.findById(id).get();
        card.setCategories(categories);
        return this.update(card);
    }

    public long count() {
        return repository.count();
    }

    public Card create(String name, String face1, String face2, Project project, @Nullable Card parent, Category... categories) {
        Card card = new Card();
        card.setName(name);
        card.setFace1(face1);
        card.setFace2(face2);
        card.setParent(parent);
        card.setCategories(Arrays.stream(categories).toList());
        projectService.update(project);
        return repository.save(card);
    }

    public List<Card> findAllByProject(Project project) {

        return repository.findAllByProjectId(project.getId());
    }
}
