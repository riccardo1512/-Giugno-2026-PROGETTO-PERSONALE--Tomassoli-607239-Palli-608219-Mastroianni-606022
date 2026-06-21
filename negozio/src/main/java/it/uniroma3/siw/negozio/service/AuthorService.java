package it.uniroma3.siw.negozio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import it.uniroma3.siw.negozio.model.Author;
import it.uniroma3.siw.negozio.repository.AuthorRepository;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final CDService cdService;

    public AuthorService(AuthorRepository authorRepository, CDService cdService) {
        this.authorRepository = authorRepository;
        this.cdService = cdService;
    }

    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    public Optional<Author> findById(Long id) {
        return authorRepository.findById(id);
    }

    public Author save(Author author) {
        return authorRepository.save(author);
    }

    public void deleteById(Long id) {
        Optional<Author> authorOpt = authorRepository.findById(id);
        if (authorOpt.isPresent()) {
            Author author = authorOpt.get();
            if (author.getCds() != null && !author.getCds().isEmpty()) {
                throw new IllegalStateException("Impossibile eliminare l'autore: ha ancora dei CD associati.");
            }
            authorRepository.delete(author);
        }
    }

    public void deleteWithCDs(Long id) {
        Optional<Author> authorOpt = authorRepository.findById(id);
        if (authorOpt.isPresent()) {
            Author author = authorOpt.get();
            if (author.getCds() != null) {
                // Eliminiamo tutti i CD di questo autore usando il cdService
                // (che si occuperà di eliminare anche i ReservationItem associati)
                // Creiamo una copia della lista per evitare ConcurrentModificationException
                for (it.uniroma3.siw.negozio.model.CD cd : new java.util.ArrayList<>(author.getCds())) {
                    cdService.deleteById(cd.getId());
                }
            }
            authorRepository.delete(author);
        }
    }
}