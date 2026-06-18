package it.uniroma3.siw.negozio.repository;

import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.negozio.model.Song;

public interface SongRepository extends CrudRepository<Song, Long> {

}