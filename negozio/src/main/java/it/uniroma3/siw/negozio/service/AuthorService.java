package it.uniroma3.siw.negozio.service;

import org.springframework.stereotype.Service;

import it.uniroma3.siw.negozio.model.Author;
import it.uniroma3.siw.negozio.repository.AuthorRepository;

@Service
public class AuthorService {

    private AuthorRepository authorRepository;

    public Iterable<Author> findAll() {
        return this.authorRepository.findAll();
    }

    public Author findById(Long id) {
        return this.authorRepository.findById(id).orElse(null);
    }

    public Author save(Author author) {
        return this.authorRepository.save(author);
    }

    public void deleteById(Long id) {
        this.authorRepository.deleteById(id);
    }
}