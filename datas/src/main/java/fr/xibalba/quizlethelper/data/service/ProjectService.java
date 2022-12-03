package fr.xibalba.quizlethelper.data.service;

import fr.xibalba.quizlethelper.data.entity.Project;
import fr.xibalba.quizlethelper.data.entity.User;
import fr.xibalba.quizlethelper.data.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ProjectService {

    private final ProjectRepository repository;
    private final UserService userService;

    @Autowired
    public ProjectService(ProjectRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public Optional<Project> get(int id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Project> getByCode(int code) {
        return repository.findByCode(code);
    }

    public Project update(Project project) {
        return repository.save(project);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Project> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public int count() {
        return (int) repository.count();
    }

    @Transactional
    public Project create(String name, int ownerId, Integer... membersIds) {

        Project project = new Project();
        project.setOwner(userService.get(ownerId).get());
        project.setName(name);
        project.setUsers(Arrays.stream(membersIds).map(integer -> userService.get(integer).get()).toList());

        int code = 0;
        Random random = new Random();
        while (code == 0 || repository.findByCode(code).isPresent()) {
            code = random.nextInt(999999);
        }
        project.setCode(code);

        userService.update(project.getOwner());
        project.getUsers().forEach(userService::update);
        return repository.save(project);
    }
}