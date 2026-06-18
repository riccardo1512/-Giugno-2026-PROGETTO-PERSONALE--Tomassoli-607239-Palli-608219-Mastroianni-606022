package it.uniroma3.siw.negozio.repository;

import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.negozio.model.Author;

public interface AuthorRepository extends CrudRepository<Author, Long> {

}
