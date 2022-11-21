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
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    @Autowired
    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public Optional<Category> get(int id) {
        return repository.findById(id);
    }

    public Category update(Category entity) {
        return repository.save(entity);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Page<Category> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public Category create(String name, Project project, Card... cards) {
        Category category = new Category();
        category.setName(name);
        category.setProject(project);
        category.setCards(Arrays.stream(cards).toList());
        return repository.save(category);
    }
}
