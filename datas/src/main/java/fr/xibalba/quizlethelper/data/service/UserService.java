package fr.xibalba.quizlethelper.data.service;

import fr.xibalba.quizlethelper.data.entity.User;
import fr.xibalba.quizlethelper.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService{

    private final UserRepository repository;
    private final PatchNoteService patchNoteService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository, PatchNoteService patchNoteService, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.patchNoteService = patchNoteService;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> get(int id) {
        return repository.findById(id);
    }

    public User get(String username) {
        return repository.findByUsername(username);
    }

    public User update(User entity) {
        return repository.save(entity);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Page<User> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public int count() {
        return (int) repository.count();
    }

    public User create(String username, String password) {
        return create(username, password, false);
    }

    public User create(String username, String password, boolean admin) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setAdmin(admin);
        user.setLastSeenChangelog(patchNoteService.count() + 1);
        return repository.save(user);
    }
}
