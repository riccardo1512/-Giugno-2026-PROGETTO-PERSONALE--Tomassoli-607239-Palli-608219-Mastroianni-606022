package it.uniroma3.siw.negozio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import it.uniroma3.siw.negozio.model.CD;

import it.uniroma3.siw.negozio.model.Author;

public interface CDRepository extends JpaRepository<CD, Long> {
    boolean existsByNameAndAuthor(String name, Author author);
    boolean existsByNameAndAuthorAndIdNot(String name, Author author, Long id);
}
