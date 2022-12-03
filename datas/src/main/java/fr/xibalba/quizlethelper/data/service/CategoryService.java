package fr.xibalba.quizlethelper.data.service;

import fr.xibalba.quizlethelper.data.entity.Card;
import fr.xibalba.quizlethelper.data.entity.Category;
import fr.xibalba.quizlethelper.data.entity.Project;
import fr.xibalba.quizlethelper.data.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository repository;
    private final ProjectService projectService;

    @Autowired
    public CategoryService(CategoryRepository repository, ProjectService projectService) {
        this.repository = repository;
        this.projectService = projectService;
    }

    public Optional<Category> get(int id) {
        return repository.findById(id);
    }

    public int count() {
        return (int) repository.count();
    }

    public Category create(String name, Project project, Card... cards) {
        Category category = new Category();
        category.setName(name);
        category.setCards(Arrays.stream(cards).toList());
        projectService.update(project);
        return repository.save(category);
    }

    public List<Category> findAllByProject(Project project) {

        return repository.findAllByProjectId(project.getId());
    }
}
