package it.uniroma3.siw.negozio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import it.uniroma3.siw.negozio.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    boolean existsByNameAndSurname(String name, String surname);
    boolean existsByNameAndSurnameAndIdNot(String name, String surname, Long id);
}
