package fr.xibalba.quizlethelper.data.service;

import fr.xibalba.quizlethelper.data.entity.PatchNote;
import fr.xibalba.quizlethelper.data.repository.PatchNoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatchNoteService {

    private final PatchNoteRepository repository;

    @Autowired
    public PatchNoteService(PatchNoteRepository repository) {
        this.repository = repository;
    }

    public Optional<PatchNote> get(int id) {
        return repository.findById(id);
    }

    public PatchNote[] getNewerThan(int id) {
        return repository.findByIdGreaterThan(id);
    }

    public int getLatestId() {
        return repository.findFirstByOrderByIdDesc().orElse(0);
    }

    public PatchNote update(PatchNote entity) {
        return repository.save(entity);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Page<PatchNote> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public PatchNote create(String title, String content) {
        PatchNote patchNote = new PatchNote();
        patchNote.setTitle(title);
        patchNote.setContent(content);
        return repository.save(patchNote);
    }
}
