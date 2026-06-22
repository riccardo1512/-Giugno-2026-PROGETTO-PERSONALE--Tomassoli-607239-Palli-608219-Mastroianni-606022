package it.uniroma3.siw.negozio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import it.uniroma3.siw.negozio.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    boolean existsByNameAndSurname(String name, String surname);
    boolean existsByNameAndSurnameAndIdNot(String name, String surname, Long id);

    /**
     * Requisito 8.1: Strategia di fetch
     * Utilizziamo la strategia JOIN FETCH per evitare il problema delle N+1 query
     * quando abbiamo bisogno di caricare l'autore e tutti i suoi CD in una singola
     * istruzione SQL, migliorando le prestazioni di caricamento.
     */
    @Query("SELECT DISTINCT a FROM Author a LEFT JOIN FETCH a.cds")
    List<Author> findAllWithCDs();
}
