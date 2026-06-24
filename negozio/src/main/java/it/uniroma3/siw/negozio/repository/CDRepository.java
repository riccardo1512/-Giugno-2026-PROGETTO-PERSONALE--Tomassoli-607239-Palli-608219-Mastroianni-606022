package it.uniroma3.siw.negozio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import it.uniroma3.siw.negozio.model.CD;

import it.uniroma3.siw.negozio.model.Author;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface CDRepository extends JpaRepository<CD, Long> {
    boolean existsByNameAndAuthor(String name, Author author);
    boolean existsByNameAndAuthorAndIdNot(String name, Author author, Long id);

    @Query("SELECT c FROM CD c JOIN FETCH c.author")
    List<CD> findAllWithAuthor();

    @EntityGraph(attributePaths = {"reviews", "author"})
    @Query("SELECT c FROM CD c WHERE c.id = :id")
    Optional<CD> findByIdWithReviews(@Param("id") Long id);
}
