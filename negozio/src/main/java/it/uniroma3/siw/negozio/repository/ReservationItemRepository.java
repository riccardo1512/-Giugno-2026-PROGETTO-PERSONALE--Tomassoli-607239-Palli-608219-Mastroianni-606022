package it.uniroma3.siw.negozio.repository;

import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.negozio.model.ReservationItem;

public interface ReservationItemRepository extends CrudRepository<ReservationItem, Long> {

}