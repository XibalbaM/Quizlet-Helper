package fr.xibalba.quizlethelper.data.service;

import fr.xibalba.quizlethelper.data.entity.Project;
import fr.xibalba.quizlethelper.data.entity.User;
import fr.xibalba.quizlethelper.data.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    //TODO: REDO ALL THE DATA PACKAGE

    private final ProjectRepository repository;

    @Autowired
    public ProjectService(ProjectRepository repository) {
        this.repository = repository;
    }

    public Optional<Project> get(int id) {
        return repository.findById(id);
    }

    public Optional<Project> getByCode(int code) {
        return repository.findByCode(code);
    }

    public Project update(Project entity) {
        return repository.save(entity);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Page<Project> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<Project> findAll() {
        return repository.findAll();
    }

    public int count() {
        return (int) repository.count();
    }

    public Project create(String name, User owner, User... members) {

        //TODO
        return null;
    }
}